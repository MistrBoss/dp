
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for quickUjo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="quickUjo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.server.dovolena.sa.cz/}abstractUjo">
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
@XmlType(name = "quickUjo")
@XmlSeeAlso({
    SoapUjo.class
})
public abstract class QuickUjo
    extends AbstractUjo
{


}
