import java.net.*;
import java.io.*;

public class Server {
	static final int PORT = 1337; // choose some port that is not in use
    static final int bufferSize = 10240; // max 10KB message
	public static void main(String[] args) {
		try {
            // open a socket that listenes on PORT
			DatagramSocket sock = new DatagramSocket(PORT, InetAddress.getByName("0.0.0.0"));
            sock.setBroadcast(true);
            byte[] packbuffer = new byte[bufferSize];
            DatagramPacket packet = new DatagramPacket(packbuffer, packbuffer.length);
            sock.receive(packet);
            // piece the recieved message back together
            String msg = "";
            for(int i = 0; i < packet.getData().length; i++) {
                msg += (char) packet.getData()[i];
            }
			System.out.println(packet.getAddress().getHostAddress() + " sent: " + msg);

			sock.close();
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}
}
