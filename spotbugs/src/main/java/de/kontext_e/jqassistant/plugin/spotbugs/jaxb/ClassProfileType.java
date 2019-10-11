
package de.kontext_e.jqassistant.plugin.spotbugs.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p&gt;Java class for ClassProfileType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="ClassProfileType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="totalMilliseconds" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="invocations" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="avgMicrosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="maxMicrosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="standardDeviationMircosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
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
     * Gets the value of the totalMilliseconds property.
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
     * Sets the value of the totalMilliseconds property.
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
     * Gets the value of the invocations property.
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
     * Sets the value of the invocations property.
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
     * Gets the value of the avgMicrosecondsPerInvocation property.
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
     * Sets the value of the avgMicrosecondsPerInvocation property.
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
     * Gets the value of the maxMicrosecondsPerInvocation property.
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
     * Sets the value of the maxMicrosecondsPerInvocation property.
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
     * Gets the value of the standardDeviationMircosecondsPerInvocation property.
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
     * Sets the value of the standardDeviationMircosecondsPerInvocation property.
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
