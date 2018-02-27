package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
	public ServerMain() {
		ServerMonitor serverMon = new ServerMonitor();
		DeleteOldestThread delThread = new DeleteOldestThread(serverMon);
		delThread.start();
		new GetMessageThread(serverMon);
		try (ServerSocket listener = new ServerSocket(30000)){
			while(true) {
				new ServerReadThread(listener.accept(), serverMon);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		new ServerMain();
	}
}
