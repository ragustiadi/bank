package bank.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bank.Bank;

/*
 * Klasse SocketDriver ist verantwortlich f??r die Kommunikation auf der Seite
 * des Clients.
 */
public class HttpDriver implements bank.BankDriver {

	private HttpBank bank = null;
	private HttpURLConnection connection = null;
	private ObjectOutputStream dataOut = null;
	private DataOutputStream dos = null;

	@Override
	public void connect(String[] args) throws IOException {
		//bank = new HttpBank(this);
		URL url = new URL("http://localhost:8000/bank");

		connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.addRequestProperty("asdf", "qwer");
		dos = new DataOutputStream(connection.getOutputStream());
		connection.connect();

		dos.writeBytes("qqwertzuiop");
		dos.flush();
		dos.close();
		connection.disconnect();
		System.out.println("flush");
	}

	@Override
	public void disconnect() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Bank getBank() {

		try {
			dos.writeBytes("ASDF");
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bank;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

}
