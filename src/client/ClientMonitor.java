package client;

import java.util.LinkedList;
import java.util.Queue;

import com.sun.swing.internal.plaf.synth.resources.synth;

import server.Message;

public class ClientMonitor {
	private Queue<String> messages;
	private String login;
	
	public ClientMonitor() {
		messages = new LinkedList<>();
		login = "2";
	}
	
	public synchronized void storeMessage(String msg) {
		messages.add(msg);
		notifyAll();
	}
	
	public synchronized String fetchNextMessage() {
		while(messages.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String nextMessage = messages.poll();
		return nextMessage;
	}
	
	public synchronized void storeLogin(String answer) {
		login = answer;
		notifyAll();
	}
	
	public synchronized String getLogin() {
		while(login.equals("2")) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//reset login variable
		String tempLogin = login;
		login = "2";
		return tempLogin;
	}
	
	

}
