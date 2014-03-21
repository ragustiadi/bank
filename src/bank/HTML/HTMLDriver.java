package bank.HTML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import bank.Bank;
import bank.HTML.HTMLServer.Type;

/*
 * Klasse SocketDriver ist verantwortlich für die Kommunikation auf der Seite
 * des Clients.
 */
public class HTMLDriver implements bank.BankDriver {

	bank.Bank bank = null;
	Socket connection = null;
	PrintStream ps = null;
	BufferedReader br = null;

	public void connect(String[] args) throws IOException {
		if (args.length < 2)
			System.out.println("Please provide host IP-Address and port.");
		else {
			connection = new Socket(args[0], Integer.parseInt(args[1]));
			System.out.println("Connection to " + connection.getInetAddress()
					+ " established on port " + connection.getLocalPort());
			ps = new PrintStream(connection.getOutputStream());
			br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			bank = new HTMLBank(this);
		}
	}

	public void disconnect() throws IOException {
		ps.flush();
		ps.close();
		br.close();
		connection.close();
		System.out.println("Connection to " + connection.getInetAddress()
				+ " closed.");
	}

	public void get(Type type, String... args) {
		ps.println("POST / HTTP/1.0");
		ps.println("User-Agent: " + getClass().toString());
		ps.println("Host: " + connection.getInetAddress());
		ps.println("Accept: */*");
		ps.println("Begin command:");
		ps.write(type.ordinal());
		ps.println();
		for (String arg : args)
			ps.println(arg);
		ps.flush();
	}

	public String read() {
		return "";
	}

	public Bank getBank() {
		return bank;
	}

}
