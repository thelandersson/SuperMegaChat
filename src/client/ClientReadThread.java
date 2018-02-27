package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;


public class ClientReadThread extends Thread{
	private boolean connected = true;
	private ClientMonitor mon;
	private InputStream input;
	
	
	private static final int MESSAGE = 1;
	private static final int IMAGE = 2;
	private static final int LOGIN = 3;
	private static final int SIGNUP = 4;
	private static final int DELETEOLDEST = 6;
	
	private static int number = 1;


	



	public ClientReadThread(Socket s, ClientMonitor mon) {
		try {
			input = s.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mon = mon;
		this.start();
	}

	public void run() {
		/*
		 * Läser på formen int type, int size, byte[] meddelande
		 */
		while (connected) {
			try {

				int firstByte = input.read();
			
				switch(firstByte) {
				case MESSAGE :
					//meddelanden kommer på formen: user*message*time
					String message = readStringFromServer();
					Logger.log(message);
					break;
				case IMAGE :
					String ending = readStringFromServer();
					
//					byte[] imageSize = new byte[4];
//					input.read(imageSize);
//					int size = ByteBuffer.wrap(imageSize).asIntBuffer().get();
					
					byte[] imageSize = new byte[4];
					input.read(imageSize);
					int size = ByteBuffer.wrap(imageSize).asIntBuffer().get();

					byte[] imageAr = new byte[size];
					int read = 0;
					int result = 0;
					while (read < size && result != -1) {
						result = input.read(imageAr, read, size - read);
						if (result != -1) {
							read = read + result;
						}
					}
					
					 BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
					 //skapa en relative path
					 String path = "./receivedimage" + number+ "." + ending;
					 ImageIO.write(image, ending, new File(path));
					 number ++;
					 Logger.log("File received");
					break;
				case LOGIN : 
					message = readStringFromServer();
					mon.storeLogin(message);
					
					break;
				case SIGNUP :
					message = readStringFromServer();
					mon.storeLogin(message);
					
					break;
				case DELETEOLDEST :
					Logger.log("delete oldest");
					
				}
				

			} catch (IOException e) {
				connected = false;
			}
		}

	}
	private String readStringFromServer() {
		String fromServer = "";
		try {
			int size = input.read();
			byte[] buffer = new byte[size];
			int read = 0;
			int result = 0;
			while (read<size && result!=-1) {
				result = input.read(buffer,read,size-read);
				if (result!=-1) {
					read = read+result;
				}
			}
			fromServer = new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fromServer;
	}
}
