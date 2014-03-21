package bank.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HTMLServer implements Runnable {

	private static ServerSocket server = null;
	private Socket socket = null;
	private ObjectInputStream dataIn = null;
	private ObjectOutputStream dataOut = null;

	public static void main(String[] args) {
		try {
			server = new ServerSocket(8080);
			System.out.println("Server ready. Accepting connections...");
			while (true) {
				Socket s = server.accept();
				Thread t = new Thread(new HTMLServer(s));
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HTMLServer(Socket s) {
		socket = s;
		try {
			dataIn = new ObjectInputStream(socket.getInputStream());
			dataOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Client connected.");
		String hello = "hello client";
		try {

			PrintStream ps = new PrintStream(socket.getOutputStream());
			//	ps.print("asdf");
			createResponse(ps);
			ps.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (socket.isBound()) {

		}
		System.out.println("Client disconnected.");
	}

	public void createResponse(PrintStream ps) {
		ps.println("HTTP/1.0 200 OK");
		ps.println("Server: " + this.getClass().getName());
		ps.println("Content-type: x-bankcommand");

		try {
			dataOut.writeObject(new String("hello client"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Command sent");
	}

}
