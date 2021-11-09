import javax.jms.*;
import javax.naming.*;

class Serv {
    public static void main(String[] args) throws NamingException {
        Context ctx = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
        //Destination queue = (Destination) ctx.lookup("queue.var.mom.jms.QUEUE34");
        System.out.println("Hello Server " + ctx);
    }
}