
package de.kontext_e.jqassistant.plugin.spotbugs.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p&gt;Java class for ClassStatsType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="ClassStatsType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sourceFile" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="interface" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="bugs" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="priority_2" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassStatsType", propOrder = {
    "value"
})
public class ClassStatsType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "sourceFile")
    protected String sourceFile;
    @XmlAttribute(name = "interface")
    protected String _interface;
    @XmlAttribute(name = "size")
    protected String size;
    @XmlAttribute(name = "bugs")
    protected String bugs;
    @XmlAttribute(name = "priority_2")
    protected String priority2;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the sourceFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the value of the sourceFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceFile(String value) {
        this.sourceFile = value;
    }

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterface(String value) {
        this._interface = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Gets the value of the bugs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBugs() {
        return bugs;
    }

    /**
     * Sets the value of the bugs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBugs(String value) {
        this.bugs = value;
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
