package bank.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import org.glassfish.grizzly.http.server.ServerConfiguration;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;

public class HttpServer {

	public static void main(String[] args) {
		org.glassfish.grizzly.http.server.HttpServer server = org.glassfish.grizzly.http.server.HttpServer.createSimpleServer("/bank", "localhost", 8080);
		try {
			System.out.println(server.getHttpHandler().getName());
			System.out.println(server.getServerConfiguration().getName());
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (true)
			;
	}

}
