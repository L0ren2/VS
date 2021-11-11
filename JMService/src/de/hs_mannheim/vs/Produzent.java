package de.hs_mannheim.vs;
import javax.jms.*;
import javax.naming.*;

class Produzent {
	String destination;
	Connection connection;
	Session session;
	MessageProducer msgprod;
	
	Produzent(String destination) {
		this.destination = destination;
		this.connection = null;
		this.session = null;
		this.msgprod = null;
		init();
	}
	
	private void init() {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = cf.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = (Destination) ctx.lookup(destination);
			msgprod = session.createProducer(dest);
			connection.start();
		} catch (Exception e) {
			e.printStackTrace();
			destroy();
		} 
	}
	
	public void send(String message) {
		try {
			Message msg = session.createTextMessage(message);
			msgprod.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() {
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