package bank.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bank.Bank;

/*
 * Klasse SocketDriver ist verantwortlich f??r die Kommunikation auf der Seite
 * des Clients.
 */
public class SocketDriver implements bank.BankDriver {

	bank.Bank bank = null;
	Socket connection = null;
	ObjectInputStream dataIn = null;
	ObjectOutputStream dataOut = null;

	public void connect(String[] args) throws IOException {
		int port = 6789; // default port
		if (args.length > 1)
			port = Integer.parseInt(args[1]);
		else {
			System.out.println("No port specified. Using default port " + port);
		}
		connection = new Socket(args[0], port);
		System.out.println("Connection to " + connection.getInetAddress()
				+ " established on port " + connection.getLocalPort());
		dataIn = new ObjectInputStream(connection.getInputStream());
		dataOut = new ObjectOutputStream(connection.getOutputStream());
		bank = new SocketBank(this);
	}

	public void disconnect() throws IOException {
		dataOut.writeObject(null);
		dataIn.close();
		dataOut.close();
		connection.close();
		System.out.println("Connection to " + connection.getInetAddress()
				+ " closed.");
	}

	public Bank getBank() {
		return bank;
	}

}
