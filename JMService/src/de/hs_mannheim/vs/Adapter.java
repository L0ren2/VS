package de.hs_mannheim.vs;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.jms.*;
import javax.naming.*;

class Adapter {
	static final int PORT = 8888;
	static ArrayList<Socket> clients;
	volatile static ArrayList<Message> messages;
	static Produzent queueProduzent;
	static Konsument queueKonsument;
	static String prodDest = "send";
	static String consDest = "recieve";
	static ServerSocket servsock;
	static Socket sock;
	static Thread housekeeper;
	static Thread queuewatcher;
	static Thread clientwatcher;
	
	static void housekeeping() {
		try {
			while(true) {
				servsock = new ServerSocket(PORT);
				clients = new ArrayList<Socket>();
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
			while(true) {
				Message msg = null;
				msg = queueKonsument.recieve(0);
				String text = "BIN";
				//if message is a TextMessage and message isn't "" and message
				if(msg != null && msg instanceof TextMessage && ((TextMessage) msg).getText() != "") {
					text = ((TextMessage) msg).getText();
				}
				for(Socket client : clients) {
					OutputStream cos = client.getOutputStream();
					PrintWriter pwr = new PrintWriter(cos);
					pwr.print(text + "\r\n\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private static class MessageHandler implements Runnable {
		InputStream clientInputStream;
		MessageHandler(InputStream is) {
			clientInputStream = is;
		}
		
		@Override
		public void run() {
			BufferedReader messageReader = new BufferedReader(new InputStreamReader(clientInputStream));
			String message;
			try {
				do {
					message = messageReader.readLine();	
					queueProduzent.send(message);
				} while(message != null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	static void watchClients() {
		try {
			while (true) {
				while(clients == null) {/*wait for housekeeper to initialize*/}
				for(int i = 0; i < clients.size(); i++) {
					Socket client = clients.get(i);
					if(client == null) {
						clients.remove(i);
						continue;
					}
					InputStream clientInputStream = client.getInputStream();
					int messageSize = clientInputStream.available();
					if(messageSize > 0) {
						MessageHandler msgh = new MessageHandler(clientInputStream);
						Thread messageHandlerThread = new Thread(msgh);
						messageHandlerThread.start();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void start(String[] args) throws NamingException {
		if(args.length > 1) {
			prodDest = args[0].toString();
			consDest = args[1].toString();
		}
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
