
package de.kontext_e.jqassistant.plugin.spotbugs.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p&gt;Java class for BugCollectionType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
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
 * </pre&gt;
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
    "history",
	"file"
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
	@XmlElement(name = "file")
	protected List<FileType> file;

    /**
     * Gets the value of the project property.
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
     * Sets the value of the project property.
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
     * <p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE&gt;set</CODE&gt; method for the bugInstance property.
     * 
     * <p&gt;
     * For example, to add a new item, do as follows:
     * <pre&gt;
     *    getBugInstance().add(newItem);
     * </pre&gt;
     * 
     * 
     * <p&gt;
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

	public List<FileType> getFile() {
    	if(file == null) {
    		file = new ArrayList<>();
		}
		return file;
	}

    /**
     * Gets the value of the errors property.
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
     * Sets the value of the errors property.
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
     * Gets the value of the findBugsSummary property.
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
     * Sets the value of the findBugsSummary property.
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
     * Gets the value of the classFeatures property.
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
     * Sets the value of the classFeatures property.
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
     * Gets the value of the history property.
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
     * Sets the value of the history property.
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
     * Gets the value of the version property.
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
     * Sets the value of the version property.
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
     * Gets the value of the sequence property.
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
     * Sets the value of the sequence property.
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
     * Gets the value of the timestamp property.
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
     * Sets the value of the timestamp property.
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
     * Gets the value of the analysisTimestamp property.
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
     * Sets the value of the analysisTimestamp property.
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
     * Gets the value of the release property.
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
     * Sets the value of the release property.
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
