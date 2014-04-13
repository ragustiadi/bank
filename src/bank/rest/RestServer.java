package bank.rest;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.jersey.api.core.ResourceConfig;

public class RestServer {

	public static void main(String[] args) throws IOException {
		final String baseUri = "http://localhost:9998/";

		// @Singleton annotations will be respected 
		final ResourceConfig rc = new ApplicationAdapter(new RestApplication());

		System.out.println("Starting grizzly...");
		HttpServer httpServer = GrizzlyServerFactory.createHttpServer(baseUri,
				rc);

		System.out.println("Grizzly started on URL " + baseUri);

		System.in.read();
		httpServer.shutdown();
	}

}
