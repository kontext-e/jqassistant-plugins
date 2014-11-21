//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.10 um 02:18:03 PM CET 
//


package de.kontext_e.jqassistant.plugin.findbugs.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BugCollectionType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BugCollectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Project" type="{}ProjectType"/&gt;
 *         &lt;element name="BugInstance" type="{}BugInstanceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Errors" type="{}ErrorsType"/&gt;
 *         &lt;element name="FindBugsSummary" type="{}FindBugsSummaryType"/&gt;
 *         &lt;element name="ClassFeatures" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="History" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sequence" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="timestamp" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="analysisTimestamp" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="release" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BugCollectionType", propOrder = {
    "project",
    "bugInstance",
    "errors",
    "findBugsSummary",
    "classFeatures",
    "history"
})
public class BugCollectionType {

    @XmlElement(name = "Project", required = true)
    protected ProjectType project;
    @XmlElement(name = "BugInstance")
    protected List<BugInstanceType> bugInstance;
    @XmlElement(name = "Errors", required = true)
    protected ErrorsType errors;
    @XmlElement(name = "FindBugsSummary", required = true)
    protected FindBugsSummaryType findBugsSummary;
    @XmlElement(name = "ClassFeatures", required = true)
    protected String classFeatures;
    @XmlElement(name = "History", required = true)
    protected String history;
    @XmlAttribute(name = "version")
    protected String version;
    @XmlAttribute(name = "sequence")
    protected String sequence;
    @XmlAttribute(name = "timestamp")
    protected String timestamp;
    @XmlAttribute(name = "analysisTimestamp")
    protected String analysisTimestamp;
    @XmlAttribute(name = "release")
    protected String release;

    /**
     * Ruft den Wert der project-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProjectType }
     *
     */
    public ProjectType getProject() {
        return project;
    }

    /**
     * Legt den Wert der project-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link ProjectType }
     *
     */
    public void setProject(ProjectType value) {
        this.project = value;
    }

    /**
     * Gets the value of the bugInstance property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bugInstance property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBugInstance().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BugInstanceType }
     *
     *
     */
    public List<BugInstanceType> getBugInstance() {
        if (bugInstance == null) {
            bugInstance = new ArrayList<BugInstanceType>();
        }
        return this.bugInstance;
    }

    /**
     * Ruft den Wert der errors-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link ErrorsType }
     *
     */
    public ErrorsType getErrors() {
        return errors;
    }

    /**
     * Legt den Wert der errors-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link ErrorsType }
     *
     */
    public void setErrors(ErrorsType value) {
        this.errors = value;
    }

    /**
     * Ruft den Wert der findBugsSummary-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link FindBugsSummaryType }
     *
     */
    public FindBugsSummaryType getFindBugsSummary() {
        return findBugsSummary;
    }

    /**
     * Legt den Wert der findBugsSummary-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link FindBugsSummaryType }
     *     
     */
    public void setFindBugsSummary(FindBugsSummaryType value) {
        this.findBugsSummary = value;
    }

    /**
     * Ruft den Wert der classFeatures-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassFeatures() {
        return classFeatures;
    }

    /**
     * Legt den Wert der classFeatures-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassFeatures(String value) {
        this.classFeatures = value;
    }

    /**
     * Ruft den Wert der history-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHistory() {
        return history;
    }

    /**
     * Legt den Wert der history-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHistory(String value) {
        this.history = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Ruft den Wert der sequence-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Legt den Wert der sequence-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequence(String value) {
        this.sequence = value;
    }

    /**
     * Ruft den Wert der timestamp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Legt den Wert der timestamp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    /**
     * Ruft den Wert der analysisTimestamp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisTimestamp() {
        return analysisTimestamp;
    }

    /**
     * Legt den Wert der analysisTimestamp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisTimestamp(String value) {
        this.analysisTimestamp = value;
    }

    /**
     * Ruft den Wert der release-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelease() {
        return release;
    }

    /**
     * Legt den Wert der release-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelease(String value) {
        this.release = value;
    }

}
