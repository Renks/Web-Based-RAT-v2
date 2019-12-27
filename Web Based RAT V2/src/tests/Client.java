package tests;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws IOException {
		Socket client = new Socket("127.0.0.1", 61234);

		DataInputStream dis = new DataInputStream(client.getInputStream());
		// Reading file
		File filePath = new File("E:\\songzz\\Eminem - Revival - (www.SongsLover.club)\\cover.jpg");
		//File filePath = new File("E:\\Cartography Map Projections.txt"); // txt file
		int size = (int) filePath.length();
		byte[] buffer = new byte[size];
		System.out.println("Size of file: "+ size);

		FileOutputStream fos = new FileOutputStream("E:\\Cartography Map Projections(1).txt");
		dis.read(buffer, 0, buffer.length); // Stores the information in buffer
		fos.write(buffer, 0, buffer.length); // Retrieves the information from buffer

		fos.close();
		dis.close();
		client.close();

	}

}
