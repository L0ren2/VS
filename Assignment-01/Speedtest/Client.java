import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;
import java.util.*;

public class Client { 

	static final int k = 1000;
	static final int PORT = 5555;
	static final int buffsize = 10240;
	static final double[] timeStart = new double[k];
	static final double[] timeEnd = new double[k];
	static final double[] timeStart2 = new double[k];
	static final double[] timeEnd2 = new double[k];
	static final double[] timeStartPing = new double[k];
	static final double[] timeEndPing = new double[k];

	private static final char[] String = null;
	static double ping = 0;
	static double up = 0;
	static double down = 0;

	
	public static void main(String[] args) { 
		speedTest();
	 } 


 	public static void speedTest(){
		try { 
			InetAddress ia = InetAddress.getByName("localhost");
			DatagramSocket sock = new DatagramSocket();
			byte[] buffer = new byte[buffsize];
			byte[] message = new byte[32];
			DatagramPacket packetOut = new DatagramPacket(buffer, buffer.length,ia, PORT);
			DatagramPacket packetIn = new DatagramPacket(buffer, buffer.length,ia, PORT);
			DatagramPacket msg = new DatagramPacket(message, message.length,ia, PORT);

			for(int i = 0; i < k; i++) {
				//ping
				timeStartPing[i] = System.nanoTime();
				sock.send(msg);
				sock.receive(packetIn);
				timeEndPing[i] = System.nanoTime();
				ping += (timeEndPing[i] - timeStartPing[i]);
				
				//upload
				timeStart[i] = System.nanoTime();
				sock.send(packetOut);
				sock.receive(msg);
				timeEnd[i] = System.nanoTime();
				up += (timeEnd[i] - timeStart[i]);
				
				//download
				timeStart2[i] = System.nanoTime();
				sock.receive(packetIn);
				timeEnd2[i] = System.nanoTime();
				down += (timeEnd2[i] - timeStart2[i]);
			}
			up /= k;
			down /= k;
			ping /= k; // median
			ping /= 1000000; // convert from nanoseconds to milliseconds
			System.out.println("Ping: " + ping + " ms");
			System.out.println("Upload: " + 1000 * (10240 / up) + " MB/s");
			System.out.println("Download: " + 1000 * (10240 / down) + " MB/s");
			sock.close(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
  	} 
}

