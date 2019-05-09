package chat;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ServerLauncher {
	public static void main(String[] args) {
		ServerController serverController = new ServerController();
		new ChatServer(65000, serverController);
		System.out.println(Thread.currentThread().getName());
		
		// Kontrollera utskrift vid start, den IP som skrivs ut måste justeras i ClientLauncher
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
