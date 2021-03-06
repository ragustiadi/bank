
package bank.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "transfer", namespace = "http://soap.bank/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transfer", namespace = "http://soap.bank/", propOrder = {
    "a",
    "b",
    "amount"
})
public class Transfer {

    @XmlElement(name = "a", namespace = "")
    private String a;
    @XmlElement(name = "b", namespace = "")
    private String b;
    @XmlElement(name = "amount", namespace = "")
    private double amount;

    /**
     * 
     * @return
     *     returns String
     */
    public String getA() {
        return this.a;
    }

    /**
     * 
     * @param a
     *     the value for the a property
     */
    public void setA(String a) {
        this.a = a;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getB() {
        return this.b;
    }

    /**
     * 
     * @param b
     *     the value for the b property
     */
    public void setB(String b) {
        this.b = b;
    }

    /**
     * 
     * @return
     *     returns double
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * 
     * @param amount
     *     the value for the amount property
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

}
