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
 * <p>Java-Klasse für packageType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="packageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="class" type="{}classType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sourcefile" type="{}sourcefileType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "packageType", propOrder = {
    "clazz",
    "sourcefile",
    "counter"
})
public class PackageType {

    @XmlElement(name = "class")
    protected List<ClassType> clazz;
    protected List<SourcefileType> sourcefile;
    protected List<CounterType> counter;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the clazz property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazz property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazz().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassType }
     *
     *
     */
    public List<ClassType> getClazz() {
        if (clazz == null) {
            clazz = new ArrayList<ClassType>();
        }
        return this.clazz;
    }

    /**
     * Gets the value of the sourcefile property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourcefile property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourcefile().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SourcefileType }
     *
     *
     */
    public List<SourcefileType> getSourcefile() {
        if (sourcefile == null) {
            sourcefile = new ArrayList<SourcefileType>();
        }
        return this.sourcefile;
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
     * {@link CounterType }
     * 
     * 
     */
    public List<CounterType> getCounter() {
        if (counter == null) {
            counter = new ArrayList<CounterType>();
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
