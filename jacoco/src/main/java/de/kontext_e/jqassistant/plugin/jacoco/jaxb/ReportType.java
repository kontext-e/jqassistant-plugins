//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.11 um 01:30:27 PM CET 
//


package de.kontext_e.jqassistant.plugin.jacoco.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für reportType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="reportType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sessioninfo" type="{}sessioninfoType"/&gt;
 *         &lt;element name="package" type="{}packageType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="counter" type="{}counterType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reportType", propOrder = {
    "sessioninfo",
    "_package",
    "counter"
})
public class ReportType {

    @XmlElement(required = true)
    protected SessioninfoType sessioninfo;
    @XmlElement(name = "package")
    protected List<PackageType> _package;
    protected List<CounterType> counter;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Ruft den Wert der sessioninfo-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link de.kontext_e.jqassistant.plugin.jacoco.jaxb.SessioninfoType }
     *
     */
    public SessioninfoType getSessioninfo() {
        return sessioninfo;
    }

    /**
     * Legt den Wert der sessioninfo-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link de.kontext_e.jqassistant.plugin.jacoco.jaxb.SessioninfoType }
     *
     */
    public void setSessioninfo(SessioninfoType value) {
        this.sessioninfo = value;
    }

    /**
     * Gets the value of the package property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the package property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPackage().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PackageType }
     *
     *
     */
    public List<PackageType> getPackage() {
        if (_package == null) {
            _package = new ArrayList<>();
        }
        return this._package;
    }

    /**
     * Gets the value of the counter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the counter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCounter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link de.kontext_e.jqassistant.plugin.jacoco.jaxb.CounterType }
     * 
     * 
     */
    public List<CounterType> getCounter() {
        if (counter == null) {
            counter = new ArrayList<>();
        }
        return this.counter;
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

}
