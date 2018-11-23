
package de.kontext_e.jqassistant.plugin.spotbugs.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p&gt;Java class for MethodType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="MethodType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SourceLine" type="{}SourceLineType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="classname" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="signature" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="isStatic" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="role" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MethodType", propOrder = {
    "sourceLine"
})
public class MethodType {

    @XmlElement(name = "SourceLine", required = true)
    protected SourceLineType sourceLine;
    @XmlAttribute(name = "classname")
    protected String classname;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "signature")
    protected String signature;
    @XmlAttribute(name = "isStatic")
    protected String isStatic;
    @XmlAttribute(name = "role")
    protected String role;

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
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

    /**
     * Gets the value of the isStatic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsStatic() {
        return isStatic;
    }

    /**
     * Sets the value of the isStatic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsStatic(String value) {
        this.isStatic = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

}
