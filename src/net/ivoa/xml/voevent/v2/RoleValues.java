//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.12 at 12:19:09 ���� CST 
//


package net.ivoa.xml.voevent.v2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for roleValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="roleValues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="observation"/>
 *     &lt;enumeration value="prediction"/>
 *     &lt;enumeration value="utility"/>
 *     &lt;enumeration value="test"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "roleValues")
@XmlEnum
public enum RoleValues {

    @XmlEnumValue("observation")
    OBSERVATION("observation"),
    @XmlEnumValue("prediction")
    PREDICTION("prediction"),
    @XmlEnumValue("utility")
    UTILITY("utility"),
    @XmlEnumValue("test")
    TEST("test");
    private final String value;

    RoleValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RoleValues fromValue(String v) {
        for (RoleValues c: RoleValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
