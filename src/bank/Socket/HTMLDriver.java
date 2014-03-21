package bank.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import bank.Bank;

/*
 * Klasse SocketDriver ist verantwortlich für die Kommunikation auf der Seite
 * des Clients.
 */
public class HTMLDriver implements bank.BankDriver {

	bank.Bank bank = null;
	Socket connection = null;
	ObjectInputStream dataIn = null;
	ObjectOutputStream dataOut = null;

	public void connect(String[] args) throws IOException {
		if (args.length < 2)
			System.out.println("Please provide host IP-Address and port.");
		else {
			connection = new Socket(args[0], Integer.parseInt(args[1]));
			System.out.println("Connection to " + connection.getInetAddress()
					+ " established on port " + connection.getLocalPort());
			dataIn = new ObjectInputStream(connection.getInputStream());
			dataOut = new ObjectOutputStream(connection.getOutputStream());
			//bank = new SocketBank(this);

		}
	}

	public void disconnect() throws IOException {
		dataOut.writeObject(null);
		dataIn.close();
		dataOut.close();
		connection.close();
		System.out.println("Connection to " + connection.getInetAddress()
				+ " closed.");
	}

	private void getResponse(PrintStream ps) {
		ps.println("GET / HTTP/1.0");
		ps.println("User-Agent: " + getClass().toString());
		ps.println("Host: 127.0.0.1");
		ps.println("Accept: */*");
		String s = null;
		try {
			s = (String) dataIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(s);
	}

	public Bank getBank() {
		return bank;
	}

}
