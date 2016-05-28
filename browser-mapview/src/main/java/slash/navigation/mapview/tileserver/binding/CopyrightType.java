//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 03:34:09 PM CET 
//


package slash.navigation.mapview.tileserver.binding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for copyrightType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="copyrightType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Google"/>
 *     &lt;enumeration value="OpenStreetMap"/>
 *     &lt;enumeration value="OutdoorActive"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "copyrightType")
@XmlEnum
public enum CopyrightType {

    @XmlEnumValue("Google")
    GOOGLE("Google"),
    @XmlEnumValue("OpenStreetMap")
    OPEN_STREET_MAP("OpenStreetMap"),
    @XmlEnumValue("OutdoorActive")
    OUTDOOR_ACTIVE("OutdoorActive");
    private final String value;

    CopyrightType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CopyrightType fromValue(String v) {
        for (CopyrightType c: CopyrightType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
