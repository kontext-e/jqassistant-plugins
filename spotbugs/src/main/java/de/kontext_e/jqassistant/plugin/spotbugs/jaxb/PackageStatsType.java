
package de.kontext_e.jqassistant.plugin.spotbugs.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p&gt;Java class for PackageStatsType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="PackageStatsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ClassStats" type="{}ClassStatsType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="package" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="total_bugs" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="total_types" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="total_size" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="priority_2" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PackageStatsType", propOrder = {
    "classStats"
})
public class PackageStatsType {

    @XmlElement(name = "ClassStats")
    protected List<ClassStatsType> classStats;
    @XmlAttribute(name = "package")
    protected String _package;
    @XmlAttribute(name = "total_bugs")
    protected String totalBugs;
    @XmlAttribute(name = "total_types")
    protected String totalTypes;
    @XmlAttribute(name = "total_size")
    protected String totalSize;
    @XmlAttribute(name = "priority_2")
    protected String priority2;

    /**
     * Gets the value of the classStats property.
     * 
     * <p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE&gt;set</CODE&gt; method for the classStats property.
     * 
     * <p&gt;
     * For example, to add a new item, do as follows:
     * <pre&gt;
     *    getClassStats().add(newItem);
     * </pre&gt;
     * 
     * 
     * <p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link ClassStatsType }
     * 
     * 
     */
    public List<ClassStatsType> getClassStats() {
        if (classStats == null) {
            classStats = new ArrayList<>();
        }
        return this.classStats;
    }

    /**
     * Gets the value of the package property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Sets the value of the package property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackage(String value) {
        this._package = value;
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
     * Gets the value of the totalTypes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalTypes() {
        return totalTypes;
    }

    /**
     * Sets the value of the totalTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalTypes(String value) {
        this.totalTypes = value;
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
