
package bank.soap.client;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "CustomException", targetNamespace = "http://soap.bank/")
public class CustomException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private CustomException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public CustomException_Exception(String message, CustomException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public CustomException_Exception(String message, CustomException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: bank.soap.client.CustomException
     */
    public CustomException getFaultInfo() {
        return faultInfo;
    }

}