
package de.kontext_e.jqassistant.plugin.spotbugs.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p&gt;Java class for FindBugsSummaryType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="FindBugsSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PackageStats" type="{}PackageStatsType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="FindBugsProfile" type="{}FindBugsProfileType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="timestamp" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="total_classes" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="referenced_classes" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="total_bugs" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="total_size" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="num_packages" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="java_version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="vm_version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="cpu_seconds" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="clock_seconds" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="peak_mbytes" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="alloc_mbytes" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="gc_seconds" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="priority_2" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindBugsSummaryType", propOrder = {
    "packageStats",
    "findBugsProfile"
})
public class FindBugsSummaryType {

    @XmlElement(name = "PackageStats")
    protected List<PackageStatsType> packageStats;
    @XmlElement(name = "FindBugsProfile", required = true)
    protected FindBugsProfileType findBugsProfile;
    @XmlAttribute(name = "timestamp")
    protected String timestamp;
    @XmlAttribute(name = "total_classes")
    protected String totalClasses;
    @XmlAttribute(name = "referenced_classes")
    protected String referencedClasses;
    @XmlAttribute(name = "total_bugs")
    protected String totalBugs;
    @XmlAttribute(name = "total_size")
    protected String totalSize;
    @XmlAttribute(name = "num_packages")
    protected String numPackages;
    @XmlAttribute(name = "java_version")
    protected String javaVersion;
    @XmlAttribute(name = "vm_version")
    protected String vmVersion;
    @XmlAttribute(name = "cpu_seconds")
    protected String cpuSeconds;
    @XmlAttribute(name = "clock_seconds")
    protected String clockSeconds;
    @XmlAttribute(name = "peak_mbytes")
    protected String peakMbytes;
    @XmlAttribute(name = "alloc_mbytes")
    protected String allocMbytes;
    @XmlAttribute(name = "gc_seconds")
    protected String gcSeconds;
    @XmlAttribute(name = "priority_2")
    protected String priority2;

    /**
     * Gets the value of the packageStats property.
     * 
     * <p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE&gt;set</CODE&gt; method for the packageStats property.
     * 
     * <p&gt;
     * For example, to add a new item, do as follows:
     * <pre&gt;
     *    getPackageStats().add(newItem);
     * </pre&gt;
     * 
     * 
     * <p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link PackageStatsType }
     * 
     * 
     */
    public List<PackageStatsType> getPackageStats() {
        if (packageStats == null) {
            packageStats = new ArrayList<>();
        }
        return this.packageStats;
    }

    /**
     * Gets the value of the findBugsProfile property.
     * 
     * @return
     *     possible object is
     *     {@link FindBugsProfileType }
     *     
     */
    public FindBugsProfileType getFindBugsProfile() {
        return findBugsProfile;
    }

    /**
     * Sets the value of the findBugsProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link FindBugsProfileType }
     *     
     */
    public void setFindBugsProfile(FindBugsProfileType value) {
        this.findBugsProfile = value;
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
     * Gets the value of the totalClasses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalClasses() {
        return totalClasses;
    }

    /**
     * Sets the value of the totalClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalClasses(String value) {
        this.totalClasses = value;
    }

    /**
     * Gets the value of the referencedClasses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencedClasses() {
        return referencedClasses;
    }

    /**
     * Sets the value of the referencedClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencedClasses(String value) {
        this.referencedClasses = value;
    }

    /**
     * Gets the value of the totalBugs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalBugs() {
        return totalBugs;
    }

    /**
     * Sets the value of the totalBugs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalBugs(String value) {
        this.totalBugs = value;
    }

    /**
     * Gets the value of the totalSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalSize() {
        return totalSize;
    }

    /**
     * Sets the value of the totalSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalSize(String value) {
        this.totalSize = value;
    }

    /**
     * Gets the value of the numPackages property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumPackages() {
        return numPackages;
    }

    /**
     * Sets the value of the numPackages property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumPackages(String value) {
        this.numPackages = value;
    }

    /**
     * Gets the value of the javaVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     * Sets the value of the javaVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaVersion(String value) {
        this.javaVersion = value;
    }

    /**
     * Gets the value of the vmVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVmVersion() {
        return vmVersion;
    }

    /**
     * Sets the value of the vmVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVmVersion(String value) {
        this.vmVersion = value;
    }

    /**
     * Gets the value of the cpuSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpuSeconds() {
        return cpuSeconds;
    }

    /**
     * Sets the value of the cpuSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpuSeconds(String value) {
        this.cpuSeconds = value;
    }

    /**
     * Gets the value of the clockSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClockSeconds() {
        return clockSeconds;
    }

    /**
     * Sets the value of the clockSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClockSeconds(String value) {
        this.clockSeconds = value;
    }

    /**
     * Gets the value of the peakMbytes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeakMbytes() {
        return peakMbytes;
    }

    /**
     * Sets the value of the peakMbytes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeakMbytes(String value) {
        this.peakMbytes = value;
    }

    /**
     * Gets the value of the allocMbytes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllocMbytes() {
        return allocMbytes;
    }

    /**
     * Sets the value of the allocMbytes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllocMbytes(String value) {
        this.allocMbytes = value;
    }

    /**
     * Gets the value of the gcSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGcSeconds() {
        return gcSeconds;
    }

    /**
     * Sets the value of the gcSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGcSeconds(String value) {
        this.gcSeconds = value;
    }

    /**
     * Gets the value of the priority2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority2() {
        return priority2;
    }

    /**
     * Sets the value of the priority2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority2(String value) {
        this.priority2 = value;
    }

}
