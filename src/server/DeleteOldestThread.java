package server;



public class DeleteOldestThread extends Thread{
	
	
	private ServerMonitor serverMon;
	private final int day = 86400000;
	private final int test = 60000;
	public DeleteOldestThread( ServerMonitor sm) {
		
		serverMon = sm;
	}
	
	public void run() {
		while(true) {
			while(serverMon.getOldestTime() == 0) {
				try {
					DeleteOldestThread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while(serverMon.getOldestTime() + test > System.currentTimeMillis()) {
				try {
					DeleteOldestThread.sleep(serverMon.getOldestTime() +test -System.currentTimeMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			serverMon.deleteOldest();
		
		}
		
	}
}
