package bank.websockets;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.tyrus.server.Server;

import bank.commands.Command;


@ServerEndpoint(value = "/bank")
public class WebsocketsServer {
	
	private static bank.Bank bank;
	private static List<Session> sessions = new CopyOnWriteArrayList<Session>();

	static void main(String[] args) {
		bank = new bank.common.ServerBank();
		Server server = new Server(WebsocketsServer.class);
		System.out.println("Server configured");
		try {
			server.start();
			System.out.println("Server started");
		} catch (DeploymentException e) {
			System.err.println("Server could not start");
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		sessions.remove(session);
	}

	@OnMessage
	public Object onMessage(Command message, Session session) throws IOException {
		Command cmd = (Command) message;
		Object ret = cmd.execute(bank);
		return ret;
	}

	@OnError
	public void onError(Throwable exception, Session session) {
		System.err.println(exception.getMessage());
		System.err.println("On session " + session.getId());
	}
	
}
