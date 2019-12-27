package tests;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		
		ServerSocket server = new ServerSocket(61234);
		
		Socket conn = server.accept();
		
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		// Reading file
		File filePath = new File("E:\\songzz\\Eminem - Revival - (www.SongsLover.club)\\cover.jpg");
		//File filePath = new File("E:\\Cartography Map Projections.txt"); Trying txt files
		
		int size = (int) filePath.length();
		byte[] buffer = new byte[size];
		
		FileInputStream fis = new FileInputStream(filePath);
		fis.read(buffer, 0, buffer.length);
		fis.close();
		
		// Sending file
		dos.write(buffer, 0 , buffer.length);
		
		
		dos.flush();
		dos.close();
		server.close();
		
	}

}
