package server;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMonitor {
	
	private Vector<ChatMember> chatMembers;
	private Queue<Message> messages;
	private ExecutorService exe;
	private Database db;
	private static final int IMAGE = 2;
	private static final int LOGIN = 3;
	private static final int SIGNUP = 4;
	private static final int DELETEOLDEST = 6;
	
	
	
	public ServerMonitor() {
		chatMembers = new Vector<ChatMember>();
		messages = new LinkedList<Message>();
		exe = Executors.newFixedThreadPool(10);
		db = new Database();
		db.openConnection("project.db");
	}
	
	
	public synchronized void addChatMember(String name, OutputStream out) {
		chatMembers.add(new ChatMember(name, out));
		
	}
	
	public synchronized void removeChatMember(String name) {
		ChatMember tmp = new ChatMember(name, null);
		chatMembers.remove(tmp);
		
	}
	
	public synchronized void addMessage(Message m) {
		messages.add(m);
		db.addMessage(m.getName(), m.getMessage(), m.getTime());
		notifyAll();
	}
	public synchronized void getMessage() {
		while(messages.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Message mes = messages.poll();
		Vector<OutputStream> outv = getOthersOutput(mes.getName());
		
	
		
		for(OutputStream o: outv) {
			exe.submit(new ServerWriteThread(o, mes));
		}
	}

	public synchronized OutputStream getCurrentOutput(String name) {
		int index = chatMembers.indexOf(new ChatMember(name, null));
		ChatMember tmp = chatMembers.get(index);
		return tmp.getOutput();
	}
	private synchronized Vector<OutputStream> getOthersOutput(String name){
		Vector<OutputStream> v = new Vector<OutputStream>();
		for(ChatMember cm : chatMembers) {
			if(!cm.getName().equals(name)) {
				
				v.add(cm.getOutput());
			}
		}
		return v;
	}
	
	private synchronized Vector<OutputStream> getAllOutput(){
		Vector<OutputStream> v = new Vector<OutputStream>();
		for(ChatMember cm : chatMembers) {
			v.add(cm.getOutput());	
		}
		return v;
	}


	public synchronized void login(String userName, String password) {
		
		OutputStream o = getCurrentOutput(userName);
		if(db.login(userName, password)) {
			Message m = new Message(LOGIN,"1");
			exe.submit(new ServerWriteThread(o, m));
		}else {
			removeChatMember(userName);
			Message m = new Message(LOGIN, "0");
			exe.submit(new ServerWriteThread(o, m));
		}
		
	}


	public synchronized void signUp(String userName, String password, OutputStream os) {
		if(db.addUser(userName, password)) {
			Message m = new Message(SIGNUP, "1");
			addChatMember(userName, os);
			exe.submit(new ServerWriteThread(os, m));
		}else{
			Message m = new Message(SIGNUP, "0");
			exe.submit(new ServerWriteThread(os, m));
		}
	}


	public synchronized void getAllMessages(OutputStream os) {
		try {
			List<Message> toSend = db.getAllMessages();
			for(Message m : toSend) {
				exe.submit(new ServerWriteThread(os, m));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	public synchronized void addImage(String name, byte[] imageAr, String ending) {
		Message m = new Message(name, IMAGE, imageAr, ending);
		messages.add(m);
		notifyAll();
		
	}


	public synchronized long getOldestTime() {
		return db.getOldestTime();
	}
		
		
		
	
	public synchronized void deleteOldest() {
			Vector<OutputStream>  v = getAllOutput();
			Message m = new Message(DELETEOLDEST, "");
			for(OutputStream os : v) {
			exe.submit(new ServerWriteThread(os, m ));
			}
			db.removeOldest();
	}
	

}

