package bank.HTML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import bank.InactiveException;
import bank.OverdrawException;
import bank.Socket.ServerBank;

public class HTMLServer implements Runnable {

	private static ServerSocket socket = null;
	private Socket connection = null;
	private BufferedReader br = null;
	private PrintStream ps = null;
	private static ServerBank bank = null;

	public static enum Type {
		ACCOUNT_DEPOSIT, ACCOUNT_GET_BALLANCE, ACCOUNT_GET_OWNER,
		ACCOUNT_IS_ACTIVE, ACCOUNT_WITHDRAW, CLOSE_ACCOUNT, CREATE_ACCOUNT,
		GET_ACCOUNT, GET_ACCOUNT_NUMBERS, TRANSFER;
	}

	public HTMLServer(Socket s) {
		connection = s;
		try {
			br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			ps = new PrintStream(connection.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = 8080; // default port number
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		try {
			socket = new ServerSocket(8080);
			bank = new ServerBank();
			System.out.println("Server ready. Accepting connections on port "
					+ port);
			while (true) {
				Socket s = socket.accept();
				Thread t = new Thread(new HTMLServer(s));
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Client connected.");
		int type = 0;
		String in = null;
		while (type >= 0) {
			try {
				in = br.readLine();
				System.out.println(in);
				if (in.equals("Begin command:")) {
					Type cmd = Type.values()[br.read()];
					System.out.println(cmd);

					switch (cmd) {
					case CREATE_ACCOUNT:
						ps.println(bank.createAccount(br.readLine()));
						break;
					case GET_ACCOUNT_NUMBERS:
						Set<String> numbers = bank.getAccountNumbers();
						System.out.println(numbers.size());
						for (String acc : numbers) {
							ps.println(acc);
							System.out.println("acc " + acc);
						}
						ps.println("*");
						break;
					case GET_ACCOUNT:
						String s = br.readLine();
						System.out.println("get acc" + s);
						ps.println(bank.getAccount(s));
						break;
					case ACCOUNT_GET_BALLANCE:
						Double bal = bank.getAccount(br.readLine())
								.getBalance();
						System.out.println(bal);
						ps.println(bal);
						break;
					case ACCOUNT_DEPOSIT:
						bank.getAccount(br.readLine()).deposit(
								Double.parseDouble(br.readLine()));
						ps.println("*");
						break;
					case ACCOUNT_GET_OWNER:
						ps.println(bank.getAccount(br.readLine()).getOwner());
						break;
					case ACCOUNT_WITHDRAW:
						bank.getAccount(br.readLine()).withdraw(
								Double.parseDouble(br.readLine()));
						break;
					case ACCOUNT_IS_ACTIVE:
						ps.println(bank.getAccount(br.readLine()).isActive());
						break;
					case TRANSFER:
						bank.transfer(bank.getAccount(br.readLine()),
								bank.getAccount(br.readLine()),
								Double.parseDouble(br.readLine()));
						break;
					case CLOSE_ACCOUNT:
						ps.println(bank.closeAccount(br.readLine()));
						break;
					default:
						System.out.println("default > " + cmd);
						break;
					}
				}
			} catch (IOException e) {
				sendError("IOException", e);
			} catch (InactiveException e) {
				sendError("InactiveException", e);
			} catch (IllegalArgumentException e) {
				sendError("IllegalArgumentException", e);
			} catch (OverdrawException e) {
				sendError("OverdrawException", e);
			}
		}
		System.out.println("Client disconnected.");
	}

	public void createResponse() {
		ps.println("HTTP/1.0 200 OK");
		ps.println("Server: " + this.getClass().getName());
		ps.println("Content-type: ");
		ps.flush();
		System.out.println("Command sent");
	}

	private void sendError(String kind, Exception e) {
		ps.println("HTTP/1.0 200 OK");
		ps.println("Server: " + this.getClass().getName());
		ps.println("Content-type: x-bankException/" + kind);
		ps.println(e.getMessage());
		ps.flush();
	}
}
