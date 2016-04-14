/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.build.gradle;
import static com.android.builder.core.BuilderConstants.FD_ANDROID_RESULTS;
import static com.android.builder.core.BuilderConstants.FD_ANDROID_TESTS;
import static com.android.builder.core.BuilderConstants.FD_REPORTS;

import com.android.build.gradle.internal.dsl.TestOptions;
import com.android.build.gradle.internal.scope.ConventionMappingHelper;
import com.android.build.gradle.internal.tasks.AndroidReportTask;
import com.android.build.gradle.internal.tasks.DeviceProviderInstrumentTestTask;
import com.android.build.gradle.internal.test.report.ReportType;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.execution.TaskGraphExecuter;

import java.io.File;
import java.util.concurrent.Callable;

import groovy.lang.Closure;

/**
 * Gradle plugin class for 'reporting' projects.
 *
 * This is mostly used to aggregate reports from subprojects.
 *
 */
class ReportingPlugin implements org.gradle.api.Plugin<Project> {

    private TestOptions extension;

    @Override
    public void apply(final Project project) {
        // make sure this project depends on the evaluation of all sub projects so that
        // it's evaluated last.
        project.evaluationDependsOnChildren();

        extension = project.getExtensions().create("android", TestOptions.class);

        final AndroidReportTask mergeReportsTask = project.getTasks().create("mergeAndroidReports",
                AndroidReportTask.class);
        mergeReportsTask.setGroup(JavaBasePlugin.VERIFICATION_GROUP);
        mergeReportsTask.setDescription("Merges all the Android test reports from the sub "
                + "projects.");
        mergeReportsTask.setReportType(ReportType.MULTI_PROJECT);

        ConventionMappingHelper.map(mergeReportsTask, "resultsDir", new Callable<File>() {
            @Override
            public File call() throws Exception {
                String resultsDir = extension.getResultsDir();
                if (resultsDir == null) {
                    return new File(project.getBuildDir(), FD_ANDROID_RESULTS);
                } else {
                    return project.file(resultsDir);
                }
            }
        });

        ConventionMappingHelper.map(mergeReportsTask, "reportsDir", new Callable<File>() {
            @Override
            public File call() throws Exception {
                String reportsDir = extension.getReportDir();
                if (reportsDir == null) {
                    return new File(new File(project.getBuildDir(),  FD_REPORTS), FD_ANDROID_TESTS);
                } else {
                    return project.file(reportsDir);
                }
            }
        });

        // gather the subprojects
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project prj) {
                for (Project p : prj.getSubprojects()) {
                    TaskCollection<AndroidReportTask> tasks = p.getTasks().withType(
                            AndroidReportTask.class);
                    for (AndroidReportTask task : tasks) {
                        mergeReportsTask.addTask(task);
                    }

                    TaskCollection<DeviceProviderInstrumentTestTask> tasks2 =
                            p.getTasks().withType(DeviceProviderInstrumentTestTask.class);
                    for (DeviceProviderInstrumentTestTask task : tasks2) {
                        mergeReportsTask.addTask(task);
                    }
                }
            }
        });

        // If gradle is launched with --continue, we want to run all tests and generate an
        // aggregate report (to help with the fact that we may have several build variants).
        // To do that, the "mergeAndroidReports" task (which does the aggregation) must always
        // run even if one of its dependent task (all the testFlavor tasks) fails, so we make
        // them ignore their error.
        // We cannot do that always: in case the test task is not going to run, we do want the
        // individual testFlavor tasks to fail.
        if (mergeReportsTask != null
                && project.getGradle().getStartParameter().isContinueOnFailure()) {
            project.getGradle().getTaskGraph().whenReady(new Closure(this) {
                void doCall(TaskGraphExecuter taskGraph) {
                    if (taskGraph.hasTask(mergeReportsTask)) {
                        mergeReportsTask.setWillRun();
                    }
                }
            });
        }
    }
}
