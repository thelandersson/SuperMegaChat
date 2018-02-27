package server;

public class Message {
	private String name;
	private String message;
	private String time;
	private int type;
	private byte[] image;
	private String ending;
	
	public Message(int type, String n, String m, String t) {
		name =n;
		message = m;
		this.type = type;
		time = t;
	}
	
	public Message(int t, String m) {
		type = t;
		message = m;
	}
	
	public Message(String n, int t, byte[] i, String e) {
		name = n;
		type = t;
		image = i;
		ending = e;
	}
	public byte[] getImage() {
		return image;
	}
	
	public String getEnding() {
		return ending;
	}
	
	
	public boolean sameName(String n) {
		return n.equals(name);
	}
	
	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return name + "*" + message + "*" + time;
	}

	public String getTime() {
		
		return time;
	}

}
