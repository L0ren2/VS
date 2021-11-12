package de.hs_mannheim.vs;
import java.io.*;
import java.net.*;
import java.util.*;

class Client {
	static Socket sock = null;
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
    	try {
			sock = new Socket("localhost", 8888);
			PrintWriter pwr = new PrintWriter(sock.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        	while(true) {
        		String msg = sc.nextLine();
        		pwr.println(msg);
        		pwr.flush();
        		System.out.println("\"Sent message\"");
        		String message = br.readLine();
        		if(message != null && message != "")
        			System.out.println("recieved: " + message);
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	sc.close();
        	try {
        		if(sock != null)
        			sock.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
}
