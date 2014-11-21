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
 * <p>Java-Klasse für BugInstanceType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BugInstanceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Class" type="{}ClassType"/&gt;
 *         &lt;element name="Field" type="{}FieldType"/&gt;
 *         &lt;element name="SourceLine" type="{}SourceLineType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="abbrev" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="category" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BugInstanceType", propOrder = {
    "clazz",
    "field",
    "sourceLine"
})
public class BugInstanceType {

    @XmlElement(name = "Class", required = true)
    protected ClassType clazz;
    @XmlElement(name = "Field", required = true)
    protected FieldType field;
    @XmlElement(name = "SourceLine", required = true)
    protected SourceLineType sourceLine;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "priority")
    protected String priority;
    @XmlAttribute(name = "abbrev")
    protected String abbrev;
    @XmlAttribute(name = "category")
    protected String category;

    /**
     * Ruft den Wert der clazz-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link de.kontext_e.jqassistant.plugin.findbugs.jaxb.ClassType }
     *
     */
    public ClassType getClazz() {
        return clazz;
    }

    /**
     * Legt den Wert der clazz-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link de.kontext_e.jqassistant.plugin.findbugs.jaxb.ClassType }
     *
     */
    public void setClazz(ClassType value) {
        this.clazz = value;
    }

    /**
     * Ruft den Wert der field-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link FieldType }
     *
     */
    public FieldType getField() {
        return field;
    }

    /**
     * Legt den Wert der field-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *
     */
    public void setField(FieldType value) {
        this.field = value;
    }

    /**
     * Ruft den Wert der sourceLine-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link de.kontext_e.jqassistant.plugin.findbugs.jaxb.SourceLineType }
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
     *     {@link de.kontext_e.jqassistant.plugin.findbugs.jaxb.SourceLineType }
     *     
     */
    public void setSourceLine(SourceLineType value) {
        this.sourceLine = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Ruft den Wert der priority-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Legt den Wert der priority-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Ruft den Wert der abbrev-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbbrev() {
        return abbrev;
    }

    /**
     * Legt den Wert der abbrev-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbbrev(String value) {
        this.abbrev = value;
    }

    /**
     * Ruft den Wert der category-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Legt den Wert der category-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

}
