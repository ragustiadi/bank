package bank.websockets;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import bank.Bank;
import bank.BankDriver2;


public class WebsocketsDriver implements BankDriver2 {
	
	private WebsocketsClientBank bank;

	@Override
	public void connect(String[] args) throws IOException {
		URI uri;
		try {
			uri = new URI("ws://localhost:2222/websockets/bank");
			System.out.println("connecting to " + uri);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		try {
			ClientManager client = ClientManager.createClient();
			WebsocketsClientBank temp = new WebsocketsClientBank();
			Session s = client.connectToServer(temp, uri);
			System.out.println("create session");
			temp.setSession(s);
			bank = temp;
			System.out.println("create bank");
		} catch (DeploymentException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void disconnect() throws IOException {
		bank.getSession().close();
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		bank.setUpdateHandler(handler);
	}

}
