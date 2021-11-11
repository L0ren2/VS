package de.hs_mannheim.vs;
import javax.jms.*;
import javax.naming.*;

class Konsument {
	String destination;
	Connection connection;
	Session session;
	MessageConsumer msgcons;
	Message msg;
	
	Konsument(String destination) {
		this.destination = destination;
		this.connection = null;
		this.session = null;
		this.msgcons = null;
		init();
	}
	
	Message recieve(int timeout) {
		try {
			msg = msgcons.receive(timeout);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	private void init() {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = cf.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = (Destination) ctx.lookup(destination);
			msgcons = session.createConsumer(dest);
			connection.start();
		} catch (Exception e) {
			e.printStackTrace();
			destroy();
		}
	}
	
	public void destroy() {
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
