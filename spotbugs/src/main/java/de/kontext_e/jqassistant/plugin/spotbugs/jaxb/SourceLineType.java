
package de.kontext_e.jqassistant.plugin.spotbugs.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p&gt;Java class for SourceLineType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="SourceLineType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="classname" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="end" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sourcefile" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sourcepath" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="startBytecode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="endBytecode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceLineType", propOrder = {
    "value"
})
public class SourceLineType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "classname")
    protected String classname;
    @XmlAttribute(name = "start")
    protected String start;
    @XmlAttribute(name = "end")
    protected String end;
    @XmlAttribute(name = "sourcefile")
    protected String sourcefile;
    @XmlAttribute(name = "sourcepath")
    protected String sourcepath;
    @XmlAttribute(name = "startBytecode")
    protected String startBytecode;
    @XmlAttribute(name = "endBytecode")
    protected String endBytecode;

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
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStart(String value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnd(String value) {
        this.end = value;
    }

    /**
     * Gets the value of the sourcefile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcefile() {
        return sourcefile;
    }

    /**
     * Sets the value of the sourcefile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcefile(String value) {
        this.sourcefile = value;
    }

    /**
     * Gets the value of the sourcepath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcepath() {
        return sourcepath;
    }

    /**
     * Sets the value of the sourcepath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcepath(String value) {
        this.sourcepath = value;
    }

    /**
     * Gets the value of the startBytecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartBytecode() {
        return startBytecode;
    }

    /**
     * Sets the value of the startBytecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartBytecode(String value) {
        this.startBytecode = value;
    }

    /**
     * Gets the value of the endBytecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndBytecode() {
        return endBytecode;
    }

    /**
     * Sets the value of the endBytecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndBytecode(String value) {
        this.endBytecode = value;
    }

}
