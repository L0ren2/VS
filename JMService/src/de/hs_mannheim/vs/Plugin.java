package de.hs_mannheim.vs;

import java.util.*;
import java.io.*;

public class Plugin implements PluginInterface {
	ArrayList<String> names = new ArrayList<String>();
	File nameFile = new File(System.getProperty("user.dir") + "/src/names.txt");

	Plugin() {
		Scanner sc = null;
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
		boolean transformed = false;
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
}
