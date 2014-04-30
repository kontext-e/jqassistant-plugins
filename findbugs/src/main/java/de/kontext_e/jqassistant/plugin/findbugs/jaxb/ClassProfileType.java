//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.10 um 02:18:03 PM CET 
//


package de.kontext_e.jqassistant.plugin.findbugs.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java-Klasse für ClassProfileType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ClassProfileType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalMilliseconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="invocations" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="avgMicrosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="maxMicrosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="standardDeviationMircosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassProfileType", propOrder = {
    "value"
})
public class ClassProfileType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "totalMilliseconds")
    protected String totalMilliseconds;
    @XmlAttribute(name = "invocations")
    protected String invocations;
    @XmlAttribute(name = "avgMicrosecondsPerInvocation")
    protected String avgMicrosecondsPerInvocation;
    @XmlAttribute(name = "maxMicrosecondsPerInvocation")
    protected String maxMicrosecondsPerInvocation;
    @XmlAttribute(name = "standardDeviationMircosecondsPerInvocation")
    protected String standardDeviationMircosecondsPerInvocation;

    /**
     * Ruft den Wert der value-Eigenschaft ab.
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
     * Legt den Wert der value-Eigenschaft fest.
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
     * Ruft den Wert der name-Eigenschaft ab.
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
     * Legt den Wert der name-Eigenschaft fest.
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
     * Ruft den Wert der totalMilliseconds-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalMilliseconds() {
        return totalMilliseconds;
    }

    /**
     * Legt den Wert der totalMilliseconds-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalMilliseconds(String value) {
        this.totalMilliseconds = value;
    }

    /**
     * Ruft den Wert der invocations-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvocations() {
        return invocations;
    }

    /**
     * Legt den Wert der invocations-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvocations(String value) {
        this.invocations = value;
    }

    /**
     * Ruft den Wert der avgMicrosecondsPerInvocation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvgMicrosecondsPerInvocation() {
        return avgMicrosecondsPerInvocation;
    }

    /**
     * Legt den Wert der avgMicrosecondsPerInvocation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvgMicrosecondsPerInvocation(String value) {
        this.avgMicrosecondsPerInvocation = value;
    }

    /**
     * Ruft den Wert der maxMicrosecondsPerInvocation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxMicrosecondsPerInvocation() {
        return maxMicrosecondsPerInvocation;
    }

    /**
     * Legt den Wert der maxMicrosecondsPerInvocation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxMicrosecondsPerInvocation(String value) {
        this.maxMicrosecondsPerInvocation = value;
    }

    /**
     * Ruft den Wert der standardDeviationMircosecondsPerInvocation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardDeviationMircosecondsPerInvocation() {
        return standardDeviationMircosecondsPerInvocation;
    }

    /**
     * Legt den Wert der standardDeviationMircosecondsPerInvocation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardDeviationMircosecondsPerInvocation(String value) {
        this.standardDeviationMircosecondsPerInvocation = value;
    }

}
