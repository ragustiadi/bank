package bank.soap;

import javax.xml.ws.Endpoint;

public class SoapServer {
	public static void main(String[] args) {
		String address = "http://127.0.0.1:9876/hs";
		Endpoint.publish(address, new BankServiceImpl());
		System.out.println("Server ready");
	}
}