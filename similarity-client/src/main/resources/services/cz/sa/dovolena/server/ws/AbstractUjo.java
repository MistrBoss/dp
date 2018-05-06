
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractUjo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractUjo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.server.dovolena.sa.cz/}superAbstractUjo">
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
@XmlType(name = "abstractUjo")
@XmlSeeAlso({
    QuickUjo.class
})
public abstract class AbstractUjo
    extends SuperAbstractUjo
{


}
