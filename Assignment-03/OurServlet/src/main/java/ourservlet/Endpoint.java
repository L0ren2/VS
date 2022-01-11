package ourservlet;

import java.io.IOException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/mqtt")
public class Endpoint {
	private Session session;
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		session.getBasicRemote().sendText("{ 'text' : " + "'Connection established'" + "}");
		System.out.print("Connected to Endpoint at /mqtt with ");
		System.out.println("session: " + session);
	}
	@OnMessage
	public void onMessage(String message, Session session) {}
	
	public void sendMessage(String message) throws IOException {
		System.out.println("Session send: " + this.session);
		System.out.printf("Sending: %s\n", message);
		this.session.getBasicRemote().sendText("{ 'text' : '" + message + "' }");
	}
	@OnError
	public void onError(Throwable t) {
		System.out.println("Some error occured.");
		t.printStackTrace();
	}
	@OnClose
	public void onClose(Session session) throws IOException {
		System.out.println("Closed session.");
		this.session.close();
		session.close();
	}
}

