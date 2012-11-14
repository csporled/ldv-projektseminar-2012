package de.syntaxtagger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads lines from stdin and processes them via berkeley parser.
 * Afterwards sends those processed lines to SyntaxParser.
 * 
 * @author nils
 *
 */
public class Main {
	// global variables
    public static List<String> taggedLines;
    
	public static void main (String[] args) {
		double start = System.currentTimeMillis();
		List<String> lines = new ArrayList<String>();
		String line;
		
		System.err.println("Started program\r\n");
		
		try {	    
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
			
			System.err.println("Reading input: (one sentence per line; empty line stops reading)");
			while ((line = reader.readLine()) != null && !line.isEmpty()) {
				lines.add(line);
			}
	    
			// run Tagger in new thread
			System.err.println("Invoking berkeleyParser");
			Thread tagger = new Thread(new Tagger(lines));
			tagger.start();
			
			// wait for tagger to finish
			System.err.println("Berkeley parser invoked. Waiting for response...");
			tagger.join();
			
			// check if taggedLines has been written by tagger thread
			System.err.println("berkeley parsed output:\n");
			if (taggedLines != null) {
				for (String outputLine : taggedLines)
					System.out.println(outputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.err.println("\nFinished in " + ((System.currentTimeMillis()-start)/1000) + " seconds");
	}
}
