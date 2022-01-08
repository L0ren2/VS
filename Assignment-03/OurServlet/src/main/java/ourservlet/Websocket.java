package ourservlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Websocket {
	//TODO probier javax.websocket aus
	.
	private volatile ServerSocket socket;
	private volatile Socket client;
	private volatile OutputStream output;
	public Websocket(int Port) throws IOException {
		socket = new ServerSocket(Port);
	}
	public void connect() throws IOException {
		// accept the socket connection asynchronously, so the main thread isnt blocked
		System.out.println("Blocking at accept");
		client = socket.accept();
		output = client.getOutputStream();
	
	}
	public void close() throws IOException {
		if(output != null) {
			output.flush();
			output.close();
		} else if(client != null) {
			client.close();
		}
		if(socket != null) {
			socket.close();
		}
	}
	/**
	 * Send a String to the connected Socket. Blocks until a connection to a socket has been established.
	 * @param message the message
	 */
	public void send(String message) throws IOException, InterruptedException {
		while(output == null) {
			Thread.sleep(20);
		}
		output.write(message.getBytes());
		output.flush();
	}
}


