package de.hs_mannheim.vs;
import javax.naming.NamingException;

public class Main {

	public static void main(String[] args) {
		try {
			Adapter.start(args);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
