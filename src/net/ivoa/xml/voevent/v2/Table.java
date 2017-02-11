//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.12 at 12:19:09 ���� CST 
//


package net.ivoa.xml.voevent.v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 *  What/Table definition. This small Table has Fields for the column
 *         definitions, and Data to hold the table data, with TR for row and TD for value of a table
 *         cell.
 * 
 * <p>Java class for Table complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Table">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Reference" type="{http://www.ivoa.net/xml/VOEvent/v2.0}Reference" minOccurs="0"/>
 *         &lt;element name="Param" type="{http://www.ivoa.net/xml/VOEvent/v2.0}Param" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Field" type="{http://www.ivoa.net/xml/VOEvent/v2.0}Field" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.ivoa.net/xml/VOEvent/v2.0}Data"/>
 *       &lt;/choice>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Table", propOrder = {
    "descriptionOrReferenceOrParam"
})
public class Table {

    @XmlElements({
        @XmlElement(name = "Data", type = Data.class),
        @XmlElement(name = "Param", type = Param.class),
        @XmlElement(name = "Field", type = Field.class),
        @XmlElement(name = "Reference", type = Reference.class),
        @XmlElement(name = "Description", type = String.class)
    })
    protected List<Object> descriptionOrReferenceOrParam;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String type;

    /**
     * Gets the value of the descriptionOrReferenceOrParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the descriptionOrReferenceOrParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescriptionOrReferenceOrParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * {@link Param }
     * {@link Field }
     * {@link Reference }
     * {@link String }
     * 
     * 
     */
    public List<Object> getDescriptionOrReferenceOrParam() {
        if (descriptionOrReferenceOrParam == null) {
            descriptionOrReferenceOrParam = new ArrayList<Object>();
        }
        return this.descriptionOrReferenceOrParam;
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
     * Gets the value of the type property.
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
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
