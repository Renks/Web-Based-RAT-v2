package com.renks.webrat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class RSSocketServer {

	ServerSocket sockServ = null;
	DataInputStream in = null;
	DataOutputStream dos = null;
	Socket conn = null;

	Scanner sc = null;

	public RSSocketServer(int port) {

		try {
			sockServ = new ServerSocket(port);
			System.out.println("Listening for clients:");
			conn = sockServ.accept();

			System.out.println("Got connection: " + conn.getInetAddress().getHostAddress());

			dos = new DataOutputStream(conn.getOutputStream());
			dos.write("Welcome to our fancy Server.".getBytes());
			dos.flush();
			in = new DataInputStream(new BufferedInputStream(conn.getInputStream()));

			new Thread(() -> getMessage()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sc = new Scanner(System.in);

		String line = "";
		while (!(line = sc.nextLine()).equals("exit server")) {
			System.out.println("Sending to Client: " + line);

			try {
				dos.write(line.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Closing the connection

		try {
			dos.close();
			conn.close();
			sockServ.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getMessage() {
		/////////////////// READING/////////////////////
		try {
			// ByteArray to store bytes sent by server
			byte[] buffer = new byte[1024];
			// Variable to store length of bytes
			int read = 0;
			while ((read = in.read(buffer, 0, buffer.length)) != -1) {
				// String Builder for storing message
				StringBuilder sb = new StringBuilder();

				// read is equal to number of characters in msg received from server
				for (int i = 0; i < read; i++) {
					// Printing out the message by casting the byte to char, we could have used
					sb.append((char) buffer[i]);
				}
				// Print client's message
				System.out.println("Client says: " + sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		@SuppressWarnings("unused")
		RSSocketServer myServer = new RSSocketServer(8889);

	}

}
