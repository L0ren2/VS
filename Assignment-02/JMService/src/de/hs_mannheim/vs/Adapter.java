package de.hs_mannheim.vs;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.jms.*;
import javax.naming.*;

class Adapter {
	static final int PORT = 8888;
	static Produzent queueProduzent;
	static Konsument queueKonsument;
	static String prodDest = "send";
	static String consDest = "recieve";
	volatile static Message msg = null; 
	volatile static ServerSocket servsock;
	volatile static Socket sock;
	volatile static boolean hasMessageArrived;
	static Thread housekeeper;
	static Thread queuewatcher;
	
	static void housekeeping() {
		try {
			servsock = new ServerSocket(PORT);
			while(true) {
				sock = servsock.accept();
				Socket s = sock;
				Thread t = new Thread(() -> readMessage(s));
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(servsock != null)
					servsock.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static void watchQueue() {
		try {
			while(true) {
				msg = queueKonsument.recieve(0);
				hasMessageArrived = true;
				System.out.println("Got a message");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	static boolean clientAlive(Socket client) { return client != null && client.isConnected(); };
	static void readMessage(Socket client) {
		Message msg_localCopy = null;
		while(clientAlive(client)) {
			try {
				BufferedReader reader = null;
				InputStream clientinput = client.getInputStream();
				if(clientAlive(client)) {
					reader = new BufferedReader(new InputStreamReader(clientinput));
				}
				String message = null;
				if(reader != null && clientAlive(client) && clientinput.available() > 0)
					message = reader.readLine();
				while(message != null) {
					//send msgs to queue
					queueProduzent.send(message);
					message = null;
					if(reader != null && clientAlive(client) && clientinput.available() > 0)
						message = reader.readLine();
				}
				if(msg != msg_localCopy && hasMessageArrived && clientAlive(client)) {
					msg_localCopy = msg;
					String text = "BIN";
					//if message is a TextMessage and message isn't "" and message
					if(msg != null && msg instanceof TextMessage && (!((TextMessage) msg).getText().equals(""))) {
						text = ((TextMessage) msg).getText();
					}
					OutputStream cos = null;
					if(clientAlive(client)) {
						cos = client.getOutputStream();
					}
					if(cos != null && clientAlive(client)) {
						PrintWriter pwr = new PrintWriter(cos, true);
						pwr.print(text + "\r\n\r\n");
						pwr.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void start(String[] args) throws NamingException {
		if(args.length > 1) {
			prodDest = args[0].toString();
			consDest = args[1].toString();
		}
		hasMessageArrived = false;
    	queueProduzent = new Produzent(prodDest);
    	queueKonsument = new Konsument(consDest);
    	housekeeper = new Thread(() -> housekeeping());
    	queuewatcher = new Thread(() -> watchQueue());
    	housekeeper.start();
    	queuewatcher.start();
    }
}
