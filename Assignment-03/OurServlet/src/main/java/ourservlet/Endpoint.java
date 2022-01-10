package ourservlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/mqtt")
public class Endpoint {
	private Session session;
	public Endpoint() throws IOException {

	}
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
	}
	@OnMessage
	public void onMessage(String message, Session session) {
		
	}
	public void sendMessage(String message) throws IOException {
		System.out.printf("Sending: %s", message);
		this.session.getBasicRemote().sendText(message);
	}
	@OnError
	public void onError(Throwable t) {
		System.out.println("Some error occured.");
		t.printStackTrace();
	}
	@OnClose
	public void onClose(Session session) {
		System.out.println("Closed session.");
	}
	/*public void connect() throws IOException {
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

	public void send(String message) throws IOException, InterruptedException {
		while(output == null) {
			Thread.sleep(20);
		}
		output.write(message.getBytes());
		output.flush();
	}*/
}


