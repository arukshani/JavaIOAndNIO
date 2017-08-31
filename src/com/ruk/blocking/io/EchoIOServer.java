package com.ruk.blocking.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * Simple Blocking IO Server
 *
 */
public class EchoIOServer {

	public static void main(String[] args) throws IOException {

		int portNumber = 4444;
		System.out.println("Waiting on port : " + portNumber + "...");
		boolean listening = true;
		//bind server socket to port
		ServerSocket serverSocket = new ServerSocket(portNumber);
		try {
			while (listening) { //long running server
				
				/*Wait for the client to make a connection and when it does, create a new socket to handle the request*/
				Socket clientSocket = serverSocket.accept();

				//Handle each connection in a new thread to manage concurrent users
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							//Get input and output stream from the socket
							PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
							BufferedReader in = new BufferedReader(
									new InputStreamReader(clientSocket.getInputStream()));
							
							//Process client request and send back response
							String request, response;
							while ((request = in.readLine()) != null) {
								response = processRequest(request);
								out.println(response);
								if ("Done".equals(request)) {
									break;
								}
							}
							clientSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
			}
		} finally {
			serverSocket.close();
		}

	}

	public static String processRequest(String request) {
		System.out.println("Server receive message from > " + request);
		return request;
	}
}
