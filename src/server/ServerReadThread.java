package server;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.Socket;
import java.nio.ByteBuffer;


import javax.imageio.ImageIO;

public class ServerReadThread extends Thread {
	private InputStream input;

	private ServerMonitor serverMon;
	private String name;

	private Socket socket;

	private static final int QUIT = 0;
	private static final int MESSAGE = 1;
	private static final int IMAGE = 2;
	private static final int LOGIN = 3;
	private static final int SIGNUP = 4;
	private static final int GETMESSAGES = 5;

	public ServerReadThread(Socket socket, ServerMonitor serverMon) {
		this.socket = socket;
		this.serverMon = serverMon;
		
		try {
			input = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.start();
	}

	public void run() {
		/*
		 * Den läser medelanden på formen: int type, int size, byte[] meddelande Anropa
		 * readstringfromclient får att hämta string
		 */

		boolean quit = false;

		do {
			try {
				int firstByte = input.read();
				// (firstByte);
				if (firstByte == QUIT) {
					serverMon.removeChatMember(name);
					quit = true;
				} else if (firstByte == MESSAGE) {
					String fromClient = readStringFromClient();
					String[] split = fromClient.split("[*]");
					String text = split[0];
					String time = split[1];
					serverMon.addMessage(new Message(MESSAGE, name, text, time));
				} else if (firstByte == IMAGE) {
					// byte[] imageSize = new byte[4];
					// input.read(imageSize);
					// int size = ByteBuffer.wrap(imageSize).asIntBuffer().get();
					readImageFromClient();

				} else if (firstByte == LOGIN) {
					
					String userLogin = readStringFromClient();
					
					String[] split = userLogin.split("[*]");
					String userName = split[0];
					String password = split[1];
					
					name = userName;
					serverMon.addChatMember(name, socket.getOutputStream());
					serverMon.login(userName, password);
				} else if (firstByte == SIGNUP) {
					String userLogin = readStringFromClient();
					String[] split = userLogin.split("[*]");
					String userName = split[0];
					String password = split[1];
					serverMon.signUp(userName, password, socket.getOutputStream());
				} else if (firstByte == GETMESSAGES) {
					serverMon.getAllMessages(socket.getOutputStream());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (!quit);
	}

	private String readStringFromClient() {
		String fromClient = "";
		try {
			int size = input.read();
			byte[] buffer = new byte[size];
			int read = 0;
			int result = 0;
			while (read < size && result != -1) {
				result = input.read(buffer, read, size - read);
				if (result != -1) {
					read = read + result;
				}
			}
			fromClient = new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fromClient;

	}

	private void readImageFromClient() throws IOException {
		// byte[] imageAr = null;

		String ending = readStringFromClient();
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
		
	
		
		serverMon.addImage(name, imageAr, ending);
		

		//BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
		//ImageIO.write(image, ending, new File("/Users/Kingoli94/Desktop/testimage2.png"));

		// int sizeOfImage = input.read();
		//
	
		// imageAr = new byte[sizeOfImage];
		// int read = 0;
		// int result = 0;
		// while (read < sizeOfImage && result != -1) {
		// result = input.read(imageAr, read, sizeOfImage - read);
		// if (result != -1) {
		// read = read + result;
		// }
		// }
		// BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
		// ImageIO.write(image, ending, new
		// File("/Users/Kingoli94/Desktop/testimage2.png"));
	}

}
