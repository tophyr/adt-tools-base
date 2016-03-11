/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.build.gradle.integration.instant;

import static com.android.build.gradle.integration.common.truth.TruthHelper.assertThat;

import com.android.build.gradle.integration.common.fixture.GradleTestProject;
import com.android.build.gradle.internal.incremental.ColdswapMode;
import com.android.build.gradle.internal.incremental.InstantRunVerifierStatus;
import com.android.builder.model.InstantRun;
import com.android.tools.fd.client.InstantRunArtifact;
import com.android.tools.fd.client.InstantRunBuildInfo;

import java.io.File;
import java.util.List;

/**
 * Helper class for testing cold-swap scenarios.
 */
class ColdSwapTester {
    private final GradleTestProject mProject;
    private final Steps mSteps;

    private ColdSwapTester(GradleTestProject project, Steps steps) {
        mProject = project;
        mSteps = steps;
    }

    /**
     * @deprecated Cold-swap is a no-op on Dalvik, most likely you don't need to call this method.
     */
    static void testDalvik(GradleTestProject project, Steps steps) throws Exception {
        ColdSwapTester tester = new ColdSwapTester(project, steps);
        tester.doTest(15, ColdswapMode.AUTO);
    }

    static void testMultiDex(GradleTestProject project, Steps steps) throws Exception {
        ColdSwapTester tester = new ColdSwapTester(project, steps);
        tester.doTest(21, ColdswapMode.MULTIDEX);
    }

    /** @deprecated Multi-apk is disable for now. */
    static void testMultiApk(GradleTestProject project, Steps steps) throws Exception {
        ColdSwapTester tester = new ColdSwapTester(project, steps);
        tester.doTest(23, ColdswapMode.MULTIAPK);
    }

    private void doTest(int apiLevel, ColdswapMode coldswapMode) throws Exception {
        InstantRun instantRunModel =
                InstantRunTestUtils.doInitialBuild(mProject, apiLevel, coldswapMode);

        mSteps.checkApk(mProject.getApk("debug"));

        InstantRunBuildInfo initialContext = InstantRunTestUtils.loadContext(instantRunModel);
        String startBuildId = initialContext.getTimeStamp();

        mSteps.makeChange();

        mProject.execute(
                InstantRunTestUtils.getInstantRunArgs(apiLevel, coldswapMode),
                instantRunModel.getIncrementalAssembleTaskName());

        InstantRunBuildInfo coldSwapContext = InstantRunTestUtils.loadContext(instantRunModel);

        assertThat(coldSwapContext.getTimeStamp()).named("build id").isNotEqualTo(startBuildId);

        mSteps.checkVerifierStatus(
                InstantRunVerifierStatus.valueOf(coldSwapContext.getVerifierStatus()));

        mSteps.checkArtifacts(coldSwapContext.getArtifacts());
    }

    interface Steps {
        void checkApk(File apk) throws Exception;

        void makeChange() throws Exception;

        void checkVerifierStatus(InstantRunVerifierStatus status) throws Exception;

        void checkArtifacts(List<InstantRunArtifact> artifacts) throws Exception;
    }
}