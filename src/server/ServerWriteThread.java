package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Vector;

public class ServerWriteThread extends Thread{
	private OutputStream out;
	private Message message;
	
	
	private static final int MESSAGE = 1;
	private static final int IMAGE = 2;
	private static final int LOGIN = 3;
	private static final int SIGNUP = 4;
	private static final int DELETEOLDEST = 6;
	

	public ServerWriteThread(OutputStream o, Message m) {
		out = o;
		message = m;
	}

	
	public void run() {
		/*
		 * den skriver emot meddelanden på formen: int type, int size, byte[] meddelande
		 * Skicka allt med metoden writetoclient
		 */
			try {
				int type = message.getType();

				switch(type) {
				case MESSAGE:
					String toSend =  message.toString();
					writeToClient(MESSAGE, toSend);
					break;
					
				case IMAGE:
					writeImageToClient(message);
					break;
				case LOGIN:
					toSend = message.getMessage();
					writeToClient(LOGIN, toSend);
					break;
				case SIGNUP:
					toSend = message.getMessage();
					writeToClient(SIGNUP, toSend);
					break;
				case DELETEOLDEST:
					out.write(DELETEOLDEST);
					out.flush();
					break;
				}

				
				
			}catch(Exception e) {
				
			}
				
		}
	
	private void writeImageToClient(Message m) {
		try {
		out.write(m.getType());
		String ending = m.getEnding();
		out.write(ending.length());
		out.write(ending.getBytes());
		out.flush();
	//	byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array(); 
		byte[] image = m.getImage();
		byte[] size = ByteBuffer.allocate(4).putInt(image.length).array(); 
		out.write(size);
		out.write(image);
		
//		out.write(sizeOfImage);
//		out.write(toSend);
		out.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	private void writeToClient(int type, String message) {
		int size = message.length();
		byte[] toSend = message.getBytes();
		try {
			out.write(type);
			out.write(size);
			out.write(toSend);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
