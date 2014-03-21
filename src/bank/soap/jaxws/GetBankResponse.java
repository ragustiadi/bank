
package bank.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getBankResponse", namespace = "http://soap.bank/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBankResponse", namespace = "http://soap.bank/")
public class GetBankResponse {

    @XmlElement(name = "return", namespace = "")
    private bank.Bank _return;

    /**
     * 
     * @return
     *     returns Bank
     */
    public bank.Bank getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(bank.Bank _return) {
        this._return = _return;
    }

}
