package de.hs_mannheim.vs;
import javax.jms.*;
import javax.naming.*;

class AlterVerwalter implements Runnable {
	Connection connection;
	Session session;
	MessageProducer msgprod;
	AlterVerwalter(){
		this.connection = null;
		this.session = null;
		this.msgprod = null;
	}
	@Override
	public void run() {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = cf.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = (Destination) ctx.lookup("recieve");
			msgprod = session.createProducer(dest);
			connection.start();
			String text = "";
			TextMessage msg = session.createTextMessage(text);
			msgprod.send(msg);
			System.out.println("Sent: " + text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(msgprod != null)
					msgprod.close();
				if(session != null)
					session.close();
				if(connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static void createProdThread () {
		Thread t = new Thread(new AlterVerwalter());
		t.setDaemon(false);
		t.start();
	}
	
	public static void main(String[] args) {
		createProdThread();
	}
}
