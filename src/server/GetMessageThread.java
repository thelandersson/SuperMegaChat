package server;

public class GetMessageThread extends Thread{
	
	private ServerMonitor serverMon;
	
	public GetMessageThread(ServerMonitor s) {
		serverMon = s;
		this.start();
	}
	
	public void run() {
		while(true) {
			serverMon.getMessage();
		}
	}

}
