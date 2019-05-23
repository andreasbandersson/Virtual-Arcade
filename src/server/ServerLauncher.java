package server;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ServerLauncher {
	public static void main(String[] args) {
		ServerController serverController = new ServerController();
		new Server(60000, serverController);
		
		// Kontrollera utskrift vid start, den IP som skrivs ut måste justeras i ClientLauncher
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
}
