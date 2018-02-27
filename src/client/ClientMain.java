package client;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;



public class ClientMain {
	public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 30000);
        ClientMonitor mon = new ClientMonitor();
        new ClientReadThread(s, mon);
        new ClientWriteThread(s, mon);
        Scanner scan = new Scanner(System.in);
       String username = scan.nextLine();
       String password = scan.nextLine();
       mon.storeMessage("L"+username+"*"+password);
       while(true) {
    	   	String message = scan.nextLine();
    	   	mon.storeMessage("m"+ message+"*" +new SimpleDateFormat("HH:mm:ss").format(new Date()));
       }
    }
}
