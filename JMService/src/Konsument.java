import javax.jms.*;
import javax.naming.*;

class Konsument implements Runnable {
	Connection connection;
	Session session;
	MessageConsumer msgcons;
	Konsument(){
		this.connection = null;
		this.session = null;
		this.msgcons = null;
	}
	@Override
	public void run() {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = cf.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = (Destination) ctx.lookup("test");
			msgcons = session.createConsumer(dest);
			connection.start();
			Message msg = msgcons.receive(1000);
			if(msg instanceof TextMessage) {
				String str = ((TextMessage) msg).getText();
				System.out.println(str);
			} else {
				System.out.println("Recieved: " + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(msgcons != null)
					msgcons.close();
				if(session != null)
					session.close();
				if(connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	static void createKonsThread() {
		Thread t = new Thread(new Konsument());
		t.setDaemon(false);
		t.start();
	}
	
	
	public static void main(String[] args) {
		createKonsThread();
	}
}
