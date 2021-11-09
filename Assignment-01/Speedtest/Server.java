import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;


public class Server {
	static final int PORT = 5555;
	static final int BUFFERSIZE = 10240;

	public static void main(String[] args) { 
		try {
			//implicit IP: 127.0.0.1
			DatagramSocket socket = new DatagramSocket(PORT);
			byte[] buffer = new byte[BUFFERSIZE]; 
			byte[] message = new byte[32];
			DatagramPacket packetIn = new DatagramPacket(buffer, buffer.length);
			DatagramPacket packetOut = new DatagramPacket(buffer, buffer.length);
			DatagramPacket msg = new DatagramPacket(message, message.length);

			while(true) {
				//ping
				socket.receive(packetIn);
				packetOut.setData(packetIn.getData());
				packetOut.setLength(packetIn.getLength());
				packetOut.setSocketAddress(packetIn.getSocketAddress());
				socket.send(packetOut);
				
				//upload
				socket.receive(packetIn);
				msg.setSocketAddress(packetIn.getSocketAddress());
				socket.send(msg);
				
				//download
				packetOut.setSocketAddress(packetIn.getSocketAddress());
				packetOut.setData(packetIn.getData());
				packetOut.setLength(packetIn.getLength());
				msg.setSocketAddress(packetIn.getSocketAddress());
				socket.send(msg);
				socket.send(packetOut);
			}
		} catch(Exception e) {
				e.printStackTrace(); 
		} 
	}
}
