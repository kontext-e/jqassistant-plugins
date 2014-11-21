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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für FieldType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FieldType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SourceLine" type="{}SourceLineType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="classname" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="signature" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="isStatic" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldType", propOrder = {
    "sourceLine"
})
public class FieldType {

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

    /**
     * Ruft den Wert der sourceLine-Eigenschaft ab.
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
     * Legt den Wert der sourceLine-Eigenschaft fest.
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
     * Ruft den Wert der classname-Eigenschaft ab.
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
     * Legt den Wert der classname-Eigenschaft fest.
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
     * Ruft den Wert der signature-Eigenschaft ab.
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
     * Legt den Wert der signature-Eigenschaft fest.
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
     * Ruft den Wert der isStatic-Eigenschaft ab.
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
     * Legt den Wert der isStatic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsStatic(String value) {
        this.isStatic = value;
    }

}
