
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for soapUjo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="soapUjo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.server.dovolena.sa.cz/}quickUjo">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soapUjo")
@XmlSeeAlso({
    SFileSource.class,
    SUser.class
})
public abstract class SoapUjo
    extends QuickUjo
{


}
