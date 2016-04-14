
package com.android.sdklib.repositoryv2.generated.common.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.android.sdklib.repositoryv2.meta.Library;


/**
 * DO NOT EDIT
 * This file was generated by xjc from sdk-common-01.xsd. Any changes will be lost upon recompilation of the schema.
 * See the schema file for instructions on running xjc.
 * 
 * 
 *                 a library provided by this addon
 *             
 * 
 * <p>Java class for libraryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="libraryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="localJarPath" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="manifestEntryRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "libraryType", propOrder = {
    "description"
})
@SuppressWarnings({
    "override",
    "unchecked"
})
public class LibraryType
    extends Library
{

    @XmlElement(required = true)
    protected String description;
    @XmlAttribute(name = "localJarPath")
    protected String localJarPath;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "manifestEntryRequired")
    protected Boolean manifestEntryRequired;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the localJarPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalJarPath() {
        return localJarPath;
    }

    /**
     * Sets the value of the localJarPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalJarPath(String value) {
        this.localJarPath = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the manifestEntryRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isManifestEntryRequired() {
        if (manifestEntryRequired == null) {
            return true;
        } else {
            return manifestEntryRequired;
        }
    }

    /**
     * Sets the value of the manifestEntryRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setManifestEntryRequired(Boolean value) {
        this.manifestEntryRequired = value;
    }

    public ObjectFactory createFactory() {
        return new ObjectFactory();
    }

}
