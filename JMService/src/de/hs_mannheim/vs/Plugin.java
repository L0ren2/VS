package de.hs_mannheim.vs;

import java.util.*;
import java.io.*;

public class Plugin implements PluginInterface {
	ArrayList<String> names = new ArrayList<String>();
	File nameFile = new File(System.getProperty("user.dir") + "/src/names.txt");
	boolean transformed;

	Plugin() {
		Scanner sc = null;
		transformed = false;
		try {
			sc = new Scanner(nameFile);
			while (sc.hasNextLine()) {
				names.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}
	
	@Override
	public String transformText(String text) {
		String match = matchForName(text);
		if (!match.equals("")) {
			transformed = true;
			text = text.replace(match, "[*** Name ***]");
			return transformText(text);
		}
		if (transformed) {
			text = "Vertraulich: enthält personenbezogene Daten\n" + text;
		}
		return text;
	}
	
	/**
	 * Searches a given String for names and returns the first match
	 * @param text
	 * @return empty String if no matches found
	 */
	private String matchForName(String text) {
		String[] words = text.split("[\s'.,]");
		for (String word : words) {
			for (String name : names) {
				if (name.equals(word)) {
					return name;
				}
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
		Plugin plugin = new Plugin();
		String text = "Alexander König & Yusuf Özdemirkan, wanderten den Fluss entlang, und trafen Lorenz test." ;
		String text1 = "Soll nicht verändert werden";
		System.out.println("Text1: " +  plugin.transformText(text1));
		System.out.println("Text2: " +  plugin.transformText(text));

	}
}
