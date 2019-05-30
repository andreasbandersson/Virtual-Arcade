package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The server side's main-method. Launches the server.
 * @author Måns Grundberg
 *
 */

public class ServerLauncher {
	public static void main(String[] args) {
		ServerController serverController = new ServerController();
		new Server(50000, serverController);
		
		// Kontrollera utskrift vid start, den IP som skrivs ut måste justeras i ClientController
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
}
