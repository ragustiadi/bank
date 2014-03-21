
package bank.soap.jaxws;

import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getAccountNumbersResponse", namespace = "http://soap.bank/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAccountNumbersResponse", namespace = "http://soap.bank/")
public class GetAccountNumbersResponse {

    @XmlElement(name = "return", namespace = "")
    private Set<String> _return;

    /**
     * 
     * @return
     *     returns Set<String>
     */
    public Set<String> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Set<String> _return) {
        this._return = _return;
    }

}
