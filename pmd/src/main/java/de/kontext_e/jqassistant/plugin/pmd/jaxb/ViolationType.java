//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.01.29 um 12:04:41 PM CET 
//


package de.kontext_e.jqassistant.plugin.pmd.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java-Klasse für violationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="violationType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="beginline" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="endline" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="begincolumn" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="endcolumn" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="rule" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ruleset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="package" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="externalInfoUrl" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "violationType", propOrder = {
    "value"
})
public class ViolationType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "beginline")
    protected Short beginline;
    @XmlAttribute(name = "endline")
    protected Short endline;
    @XmlAttribute(name = "begincolumn")
    protected Byte begincolumn;
    @XmlAttribute(name = "endcolumn")
    protected Short endcolumn;
    @XmlAttribute(name = "rule")
    protected String rule;
    @XmlAttribute(name = "ruleset")
    protected String ruleset;
    @XmlAttribute(name = "package")
    protected String _package;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "method")
    protected String method;
    @XmlAttribute(name = "externalInfoUrl")
    @XmlSchemaType(name = "anyURI")
    protected String externalInfoUrl;
    @XmlAttribute(name = "priority")
    protected Byte priority;

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
     * Ruft den Wert der beginline-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getBeginline() {
        return beginline;
    }

    /**
     * Legt den Wert der beginline-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setBeginline(Short value) {
        this.beginline = value;
    }

    /**
     * Ruft den Wert der endline-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getEndline() {
        return endline;
    }

    /**
     * Legt den Wert der endline-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setEndline(Short value) {
        this.endline = value;
    }

    /**
     * Ruft den Wert der begincolumn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getBegincolumn() {
        return begincolumn;
    }

    /**
     * Legt den Wert der begincolumn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setBegincolumn(Byte value) {
        this.begincolumn = value;
    }

    /**
     * Ruft den Wert der endcolumn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getEndcolumn() {
        return endcolumn;
    }

    /**
     * Legt den Wert der endcolumn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setEndcolumn(Short value) {
        this.endcolumn = value;
    }

    /**
     * Ruft den Wert der rule-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRule() {
        return rule;
    }

    /**
     * Legt den Wert der rule-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRule(String value) {
        this.rule = value;
    }

    /**
     * Ruft den Wert der ruleset-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleset() {
        return ruleset;
    }

    /**
     * Legt den Wert der ruleset-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleset(String value) {
        this.ruleset = value;
    }

    /**
     * Ruft den Wert der package-Eigenschaft ab.
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
     * Legt den Wert der package-Eigenschaft fest.
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
     * Ruft den Wert der clazz-Eigenschaft ab.
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
     * Legt den Wert der clazz-Eigenschaft fest.
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
     * Ruft den Wert der method-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Legt den Wert der method-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

    /**
     * Ruft den Wert der externalInfoUrl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalInfoUrl() {
        return externalInfoUrl;
    }

    /**
     * Legt den Wert der externalInfoUrl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalInfoUrl(String value) {
        this.externalInfoUrl = value;
    }

    /**
     * Ruft den Wert der priority-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getPriority() {
        return priority;
    }

    /**
     * Legt den Wert der priority-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setPriority(Byte value) {
        this.priority = value;
    }

}
