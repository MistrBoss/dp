
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for userRole.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="userRole">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SYSTEM"/>
 *     &lt;enumeration value="ADMIN"/>
 *     &lt;enumeration value="USER"/>
 *     &lt;enumeration value="SELLER"/>
 *     &lt;enumeration value="DISCOUNT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "userRole")
@XmlEnum
public enum UserRole {

    SYSTEM,
    ADMIN,
    USER,
    SELLER,
    DISCOUNT;

    public String value() {
        return name();
    }

    public static UserRole fromValue(String v) {
        return valueOf(v);
    }

}
