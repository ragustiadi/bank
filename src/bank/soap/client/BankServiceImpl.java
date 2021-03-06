
package bank.soap.client;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "BankServiceImpl", targetNamespace = "http://soap.bank/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BankServiceImpl {


    /**
     * 
     * @param amount
     * @param b
     * @param a
     * @throws IOException_Exception
     * @throws OverdrawException_Exception
     * @throws InactiveException_Exception
     * @throws CustomException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "transfer", targetNamespace = "http://soap.bank/", className = "bank.soap.client.Transfer")
    @ResponseWrapper(localName = "transferResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.TransferResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/transferRequest", output = "http://soap.bank/BankServiceImpl/transferResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/transfer/Fault/IOException"),
        @FaultAction(className = OverdrawException_Exception.class, value = "http://soap.bank/BankServiceImpl/transfer/Fault/OverdrawException"),
        @FaultAction(className = InactiveException_Exception.class, value = "http://soap.bank/BankServiceImpl/transfer/Fault/InactiveException"),
        @FaultAction(className = CustomException_Exception.class, value = "http://soap.bank/BankServiceImpl/transfer/Fault/CustomException")
    })
    public void transfer(
        @WebParam(name = "a", targetNamespace = "")
        String a,
        @WebParam(name = "b", targetNamespace = "")
        String b,
        @WebParam(name = "amount", targetNamespace = "")
        double amount)
        throws CustomException_Exception, IOException_Exception, InactiveException_Exception, OverdrawException_Exception
    ;

    /**
     * 
     * @param number
     * @return
     *     returns java.lang.String
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getOwner", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetOwner")
    @ResponseWrapper(localName = "getOwnerResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetOwnerResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/getOwnerRequest", output = "http://soap.bank/BankServiceImpl/getOwnerResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/getOwner/Fault/IOException")
    })
    public String getOwner(
        @WebParam(name = "number", targetNamespace = "")
        String number)
        throws IOException_Exception
    ;

    /**
     * 
     * @param owner
     * @return
     *     returns java.lang.String
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createAccount", targetNamespace = "http://soap.bank/", className = "bank.soap.client.CreateAccount")
    @ResponseWrapper(localName = "createAccountResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.CreateAccountResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/createAccountRequest", output = "http://soap.bank/BankServiceImpl/createAccountResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/createAccount/Fault/IOException")
    })
    public String createAccount(
        @WebParam(name = "owner", targetNamespace = "")
        String owner)
        throws IOException_Exception
    ;

    /**
     * 
     * @param number
     * @return
     *     returns boolean
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "closeAccount", targetNamespace = "http://soap.bank/", className = "bank.soap.client.CloseAccount")
    @ResponseWrapper(localName = "closeAccountResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.CloseAccountResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/closeAccountRequest", output = "http://soap.bank/BankServiceImpl/closeAccountResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/closeAccount/Fault/IOException")
    })
    public boolean closeAccount(
        @WebParam(name = "number", targetNamespace = "")
        String number)
        throws IOException_Exception
    ;

    /**
     * 
     * @return
     *     returns java.util.List<java.lang.Object>
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAccountNumbers", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetAccountNumbers")
    @ResponseWrapper(localName = "getAccountNumbersResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetAccountNumbersResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/getAccountNumbersRequest", output = "http://soap.bank/BankServiceImpl/getAccountNumbersResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/getAccountNumbers/Fault/IOException")
    })
    public List<Object> getAccountNumbers()
        throws IOException_Exception
    ;

    /**
     * 
     * @param number
     * @return
     *     returns java.lang.String
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAccount", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetAccount")
    @ResponseWrapper(localName = "getAccountResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetAccountResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/getAccountRequest", output = "http://soap.bank/BankServiceImpl/getAccountResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/getAccount/Fault/IOException")
    })
    public String getAccount(
        @WebParam(name = "number", targetNamespace = "")
        String number)
        throws IOException_Exception
    ;

    /**
     * 
     * @param number
     * @return
     *     returns boolean
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "isActive", targetNamespace = "http://soap.bank/", className = "bank.soap.client.IsActive")
    @ResponseWrapper(localName = "isActiveResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.IsActiveResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/isActiveRequest", output = "http://soap.bank/BankServiceImpl/isActiveResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/isActive/Fault/IOException")
    })
    public boolean isActive(
        @WebParam(name = "number", targetNamespace = "")
        String number)
        throws IOException_Exception
    ;

    /**
     * 
     * @param amount
     * @param number
     * @throws IOException_Exception
     * @throws CustomException_Exception
     * @throws InactiveException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "deposit", targetNamespace = "http://soap.bank/", className = "bank.soap.client.Deposit")
    @ResponseWrapper(localName = "depositResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.DepositResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/depositRequest", output = "http://soap.bank/BankServiceImpl/depositResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/deposit/Fault/IOException"),
        @FaultAction(className = InactiveException_Exception.class, value = "http://soap.bank/BankServiceImpl/deposit/Fault/InactiveException"),
        @FaultAction(className = CustomException_Exception.class, value = "http://soap.bank/BankServiceImpl/deposit/Fault/CustomException")
    })
    public void deposit(
        @WebParam(name = "number", targetNamespace = "")
        String number,
        @WebParam(name = "amount", targetNamespace = "")
        double amount)
        throws CustomException_Exception, IOException_Exception, InactiveException_Exception
    ;

    /**
     * 
     * @param amount
     * @param number
     * @throws IOException_Exception
     * @throws OverdrawException_Exception
     * @throws CustomException_Exception
     * @throws InactiveException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "withdraw", targetNamespace = "http://soap.bank/", className = "bank.soap.client.Withdraw")
    @ResponseWrapper(localName = "withdrawResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.WithdrawResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/withdrawRequest", output = "http://soap.bank/BankServiceImpl/withdrawResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/withdraw/Fault/IOException"),
        @FaultAction(className = OverdrawException_Exception.class, value = "http://soap.bank/BankServiceImpl/withdraw/Fault/OverdrawException"),
        @FaultAction(className = InactiveException_Exception.class, value = "http://soap.bank/BankServiceImpl/withdraw/Fault/InactiveException"),
        @FaultAction(className = CustomException_Exception.class, value = "http://soap.bank/BankServiceImpl/withdraw/Fault/CustomException")
    })
    public void withdraw(
        @WebParam(name = "number", targetNamespace = "")
        String number,
        @WebParam(name = "amount", targetNamespace = "")
        double amount)
        throws CustomException_Exception, IOException_Exception, InactiveException_Exception, OverdrawException_Exception
    ;

    /**
     * 
     * @param number
     * @return
     *     returns double
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getBalance", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetBalance")
    @ResponseWrapper(localName = "getBalanceResponse", targetNamespace = "http://soap.bank/", className = "bank.soap.client.GetBalanceResponse")
    @Action(input = "http://soap.bank/BankServiceImpl/getBalanceRequest", output = "http://soap.bank/BankServiceImpl/getBalanceResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://soap.bank/BankServiceImpl/getBalance/Fault/IOException")
    })
    public double getBalance(
        @WebParam(name = "number", targetNamespace = "")
        String number)
        throws IOException_Exception
    ;

}
