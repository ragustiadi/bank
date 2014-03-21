package bank.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;

import bank.Socket.ServerBank;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServlet {

	private static ServerBank bank = null;

	public static enum Type {
		ACCOUNT_DEPOSIT, ACCOUNT_GET_BALLANCE, ACCOUNT_GET_OWNER,
		ACCOUNT_IS_ACTIVE, ACCOUNT_WITHDRAW, CLOSE_ACCOUNT, CREATE_ACCOUNT,
		GET_ACCOUNT, GET_ACCOUNT_NUMBERS, TRANSFER;
	}

	public HttpServlet(int port) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(port),
					10);
			bank = new ServerBank();
			server.createContext("/bank", new Handler());
			server.setExecutor(null);
			server.start();
			System.out.println("Server accepting connections on port " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = 8000;
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		HttpServlet servlet = new HttpServlet(port);
	}

	private class Handler implements HttpHandler {

		@Override
		public void handle(HttpExchange arg0) throws IOException {
			System.out.println("Handle");
			BufferedReader dataIn = new BufferedReader(new InputStreamReader(
					arg0.getRequestBody()));
			System.out.println(dataIn.readLine());
			ObjectInputStream ois = new ObjectInputStream(arg0.getRequestBody());
			try {
				System.out.println(ois.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			BufferedOutputStream dis = new BufferedOutputStream(
					arg0.getResponseBody());
			dis.write(32);
			dis.flush();
			dis.close();
		}

	}

}