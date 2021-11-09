import javax.jms.*;
import javax.naming.*;

class Produzent implements Runnable {
	Connection connection;
	Session session;
	MessageProducer msgprod;
	Produzent(){
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
			Destination dest = (Destination) ctx.lookup("test");
			msgprod = session.createProducer(dest);
			connection.start();
			String text = "Message from Produzent";
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
		Thread t = new Thread(new Produzent());
		t.setDaemon(false);
		t.start();
	}
	
	public static void main(String[] args) {
		createProdThread();
	}
}