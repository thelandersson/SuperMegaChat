package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ClientWriteThread extends Thread {
	private OutputStream out;
	private Socket socket;
	private ClientMonitor mon;
	
	private static final int QUIT = 0;
	private static final int MESSAGE = 1;
	private static final int IMAGE = 2;

	private static final int LOGIN = 3;
	private static final int SIGNUP = 4;
	private static final int GETMESSAGES = 5;


	public ClientWriteThread(Socket s, ClientMonitor mon) {
		try {
			socket = s;
			out = socket.getOutputStream();
			this.mon = mon;
			this.start();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void run() {
		/*
		 * skickar allt på formen int type, int size, byte[] meddelande 
		 */
		String message = "";
		boolean run = true;
		do {
			try {
				message = mon.fetchNextMessage();
				String type = message.substring(0, 1);
				type = type.toUpperCase();
				String toSend = "";
				switch (type) {

				case "Q":
					run = false;
					out.write(QUIT);
					out.flush();

					break;
				case "M":
					toSend = message.substring(1, message.length());
					writeToServer(MESSAGE, toSend);
					break;
				case "I" :
					String path  = message.substring(1, message.length());
					File file = new File(path);
					BufferedImage image = ImageIO.read(file);

			        ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        //"/Users/Kingoli94/Desktop/testimage.png"
			        int index = path.indexOf('.');
			        String ending = path.substring(index +1);
			       
			        ImageIO.write(image, ending, baos);			        
			       

			        writeImageToServer(IMAGE, baos,  ending);
					break;
				case "L" :
					//Är detta rätt?
					toSend = message.substring(1, message.length());
					
					writeToServer(LOGIN, toSend);
					break;
					
				case "S" :
					//Är detta rätt?
					toSend = message.substring(1, message.length());
					writeToServer(SIGNUP, toSend);
					break;
					
				case "G" :
					out.write(GETMESSAGES);
					out.flush();
					break;
					
				}
			} catch (Exception e) {
			
			}
		} while (run);
		try {
			socket.close();
		} catch (IOException e) {

		}

	}
	
	private void writeImageToServer(int type, ByteArrayOutputStream baos, String ending) {
//		 int sizeOfImage = baos.size();
//		 byte[] toSend = baos.toByteArray();
		 int sizeOfEnding = ending.length();
		 try {
				out.write(type);
				out.write(sizeOfEnding);
				out.write(ending.getBytes());
				out.flush();
				byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array(); 
				out.write(size);
				out.write(baos.toByteArray());
				
//				out.write(sizeOfImage);
//				out.write(toSend);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private void writeToServer(int type, String message) {
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
