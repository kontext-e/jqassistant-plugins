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
 * <p>Java-Klasse für FindBugsSummaryType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FindBugsSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PackageStats" type="{}PackageStatsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FindBugsProfile" type="{}FindBugsProfileType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="timestamp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_classes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="referenced_classes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_bugs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_size" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="num_packages" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="vm_version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cpu_seconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="clock_seconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="peak_mbytes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="alloc_mbytes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gc_seconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="priority_2" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the packageStats property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPackageStats().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PackageStatsType }
     *
     *
     */
    public List<PackageStatsType> getPackageStats() {
        if (packageStats == null) {
            packageStats = new ArrayList<PackageStatsType>();
        }
        return this.packageStats;
    }

    /**
     * Ruft den Wert der findBugsProfile-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link com.buschmais.jqassistant.plugin.findbugs.impl.jaxb.FindBugsProfileType }
     *
     */
    public FindBugsProfileType getFindBugsProfile() {
        return findBugsProfile;
    }

    /**
     * Legt den Wert der findBugsProfile-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link com.buschmais.jqassistant.plugin.findbugs.impl.jaxb.FindBugsProfileType }
     *     
     */
    public void setFindBugsProfile(FindBugsProfileType value) {
        this.findBugsProfile = value;
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
     * Ruft den Wert der totalClasses-Eigenschaft ab.
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
     * Legt den Wert der totalClasses-Eigenschaft fest.
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
     * Ruft den Wert der referencedClasses-Eigenschaft ab.
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
     * Legt den Wert der referencedClasses-Eigenschaft fest.
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
     * Ruft den Wert der totalBugs-Eigenschaft ab.
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
     * Legt den Wert der totalBugs-Eigenschaft fest.
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
     * Ruft den Wert der totalSize-Eigenschaft ab.
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
     * Legt den Wert der totalSize-Eigenschaft fest.
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
     * Ruft den Wert der numPackages-Eigenschaft ab.
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
     * Legt den Wert der numPackages-Eigenschaft fest.
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
     * Ruft den Wert der vmVersion-Eigenschaft ab.
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
     * Legt den Wert der vmVersion-Eigenschaft fest.
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
     * Ruft den Wert der cpuSeconds-Eigenschaft ab.
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
     * Legt den Wert der cpuSeconds-Eigenschaft fest.
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
     * Ruft den Wert der clockSeconds-Eigenschaft ab.
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
     * Legt den Wert der clockSeconds-Eigenschaft fest.
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
     * Ruft den Wert der peakMbytes-Eigenschaft ab.
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
     * Legt den Wert der peakMbytes-Eigenschaft fest.
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
     * Ruft den Wert der allocMbytes-Eigenschaft ab.
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
     * Legt den Wert der allocMbytes-Eigenschaft fest.
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
     * Ruft den Wert der gcSeconds-Eigenschaft ab.
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
     * Legt den Wert der gcSeconds-Eigenschaft fest.
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
     * Ruft den Wert der priority2-Eigenschaft ab.
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
     * Legt den Wert der priority2-Eigenschaft fest.
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
