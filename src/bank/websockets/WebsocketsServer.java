package bank.websockets;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.tyrus.server.Server;

import bank.commands.Command;
import bank.jms.JmsServer.JmsBank;


@ServerEndpoint(value = "/bank", decoders = CommandDecoder.class, encoders = CommandEncoder.class)
public class WebsocketsServer {
	
	private static bank.jms.JmsServer.JmsBank bank;
	private static List<Session> sessions = new CopyOnWriteArrayList<Session>();

	public static void main(String[] args) throws IOException {
//		bank = new bank.common.ServerBank();
		
		bank = new JmsBank() {	// extend JmsBank to update listeners in websockets fashion
			@Override
			public void sendUpdates(String number) {
//				System.out.println("update " + sessions.size() + " listeners");
				Response response = new Response(number);
				response.setNotification(true);
				try {
					for (Session s : sessions)
						s.getBasicRemote().sendObject(response);
				} catch (IOException | EncodeException e) {
					System.err.println("1 or more listeners could not be notified");
				}
			}
		};
		
		// bind to ws://localhost:2222/websockets/bank
		Server server = new Server("localhost", 2222, "/websockets", WebsocketsServer.class);
		System.out.println("Server configured");
		try {
			server.start();
			System.out.println("Server started");
		} catch (DeploymentException e) {
			e.printStackTrace();
			System.err.println("Server could not start");
		}
		
		System.in.read();
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("added new connection");
		sessions.add(session);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("removed connection");
		sessions.remove(session);
	}

	@OnMessage
	public Object onMessage(Object message, Session session) throws IOException {
		Command cmd = (Command) message;
		Object ret = cmd.execute(bank);
		return new Response(ret);
	}

	@OnError
	public void onError(Throwable exception, Session session) {
		System.err.println(exception.getMessage());
		System.err.println("On session " + session.getId());
	}

}
