package bank.soap;

import javax.xml.ws.Endpoint;

public class Publisher {
	public static void main(String[] args) {

		Endpoint.publish("http://127.0.0.1:9876/hs", new BankServiceImpl());
		System.out.println("service published");
	}
}