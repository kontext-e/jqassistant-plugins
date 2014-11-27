
package de.kontext_e.jqassistant.plugin.findbugs.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p&gt;Java class for ClassType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="ClassType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SourceLine" type="{}SourceLineType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="classname" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassType", propOrder = {
    "sourceLine"
})
public class ClassType {

    @XmlElement(name = "SourceLine", required = true)
    protected SourceLineType sourceLine;
    @XmlAttribute(name = "classname")
    protected String classname;

    /**
     * Gets the value of the sourceLine property.
     * 
     * @return
     *     possible object is
     *     {@link SourceLineType }
     *     
     */
    public SourceLineType getSourceLine() {
        return sourceLine;
    }

    /**
     * Sets the value of the sourceLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceLineType }
     *     
     */
    public void setSourceLine(SourceLineType value) {
        this.sourceLine = value;
    }

    /**
     * Gets the value of the classname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassname() {
        return classname;
    }

    /**
     * Sets the value of the classname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassname(String value) {
        this.classname = value;
    }

}
