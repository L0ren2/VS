package de.hs_mannheim.vs;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.jms.*;
import javax.naming.*;

class Adapter {
	static final int PORT = 8888;
	volatile static ArrayList<Socket> clients;
	static Produzent queueProduzent;
	static Konsument queueKonsument;
	static String prodDest = "send";
	static String consDest = "recieve";
	volatile static ServerSocket servsock;
	volatile static Socket sock;
	static Thread housekeeper;
	static Thread queuewatcher;
	static Thread clientwatcher;
	
	static void housekeeping() {
		try {
			servsock = new ServerSocket(PORT);
			while(true) {
				sock = servsock.accept();
				Socket s = sock;
				clients.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(servsock != null)
					servsock.close();
				for(Socket client : clients) {
					if(client != null)
						client.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static void watchQueue() {
		try {
			Message msg = null;
			String text = "BIN";
			while(true) {
				msg = queueKonsument.recieve(0);
				//if message is a TextMessage and message isn't "" and message
				if(msg != null && msg instanceof TextMessage && (!((TextMessage) msg).getText().equals(""))) {
					text = ((TextMessage) msg).getText();
				}
				for(Socket client : clients) {
					OutputStream cos = client.getOutputStream();
					PrintWriter pwr = new PrintWriter(cos, true);
					pwr.print(text + "\r\n\r\n");
					pwr.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	static void watchClients() {
		while(true) {
			//iterate through clients
			clients.forEach(client -> {
				//check for new msgs
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String message = reader.readLine();
					while(message != null) {
						//send msgs to queue
						queueProduzent.send(message);
						message = reader.readLine();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	public static void start(String[] args) throws NamingException {
		if(args.length > 1) {
			prodDest = args[0].toString();
			consDest = args[1].toString();
		}
		clients = new ArrayList<Socket>();
    	queueProduzent = new Produzent(prodDest);
    	queueKonsument = new Konsument(consDest);
    	housekeeper = new Thread(() -> housekeeping());
    	clientwatcher = new Thread(() -> watchClients());
    	queuewatcher = new Thread(() -> watchQueue());
    	housekeeper.start();
    	clientwatcher.start();
    	queuewatcher.start();
    }
}
