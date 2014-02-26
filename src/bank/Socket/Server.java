package bank.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Klasse Server startet auf der Serverseite und startet für jeden neuen Client
 * einen neuen Thread.
 */
public class Server implements Runnable {

	static ServerSocket socket = null;
	Socket connection = null;
	static ServerBank bank = new ServerBank();
	ObjectInputStream dataIn = null;
	ObjectOutputStream dataOut = null;
	static int numOfConnections = 0;

	public Server(Socket s) {
		connection = s;
		try {
			dataOut = new ObjectOutputStream(connection.getOutputStream());
			dataIn = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = 6789; // default port number
		if (args.length > 0)
			port = Integer.parseInt(args[0]); // overwrite default port if args are set
		try {
			socket = new ServerSocket(port);
			System.out.println("Server ready. Accepting connections on port "
					+ port);
			while (true) {
				Socket connection = socket.accept();
				Thread t = new Thread(new Server(connection));
				t.start();
				numOfConnections++;
				System.out.println("Number of clients: " + numOfConnections);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Thread for " + connection.getInetAddress()
				+ " started");
		bank.commands.Command cmd = null;
		while (!connection.isClosed()) {
			try {
				cmd = (bank.commands.Command) dataIn.readObject();
				if (cmd == null)
					connection.close();
				else
					cmd.execute(bank, dataOut);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			dataIn.close();
			dataOut.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Thread for " + connection.getInetAddress()
				+ " terminated");
		numOfConnections--;
	}
}
