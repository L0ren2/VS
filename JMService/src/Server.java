import java.util.*;
import javax.jms.*;
import javax.naming.*;

class Server {
	volatile boolean open = true;
	Connection connection;
	Session session;
	Destination dest;
	
    public static void main(String[] args) throws NamingException {
    	Scanner sc = new Scanner(System.in);
    	Server serv = null;
        Context ctx = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
        try {
        	//ServerSocket servsock = new ServerSocket(8888);
        	//Socket sock = servsock.accept();
	        serv = new Server();
        	serv.connection = factory.createConnection();
	        serv.session = serv.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        serv.dest = (Destination) ctx.lookup("test");
	        serv.connection.start();
	        
	        String str = "";
	        while(str != "q") {
	        	str = sc.nextLine();
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	try {
        		if(sc != null)
        			sc.close();
        		if(serv.session != null) {
        			serv.session.close();
        		}
        		if(serv.connection != null) {
        			serv.connection.close();
        		}
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
}
