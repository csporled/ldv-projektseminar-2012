package de.regestanalyser.syntaxtagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.regestanalyser.database.DBManager;

/**
 * Reads input from stdin or from database and sends it to berkeley parser.
 * Options db and r need to be set when used with a database.
 * 
 * class path must contain objectDB and Apache CLI packages!
 * 
 * @author nils
 */
public class Main {
	// global variables
    public static List<String> taggedLines;
	private static double start = System.currentTimeMillis();
    
	public static void main (String[] args) {
		CommandLine options = parseOptions(args);
		
		List<String> lines = new ArrayList<String>();
		String line;
		
		System.err.println("Started program\r\n");
		
		try {	    
			// read from database?
			if (options.hasOption("db")) {
				// database exists?
				String database = options.getOptionValue("db");
				database = (database.endsWith(".odb")?database:database+".odb");
				File databaseFile = new File(database);
				if (!databaseFile.exists() || !databaseFile.isFile()) {
					System.err.println("Provided database name could not be found. Please check your options.");
					exit(-1);
				}
				
				// is regest also set?
				if (options.hasOption("r")) {
					// create database manager
					DBManager dbm = new DBManager(database);
					
					// query database
					@SuppressWarnings("unchecked")
					List<String> text = (List<String>) dbm.getSingleResult("SELECT r.content FROM Regest r WHERE id="+options.getOptionValue("r"));
					
					// nothing retrieved?
					if (text == null || text.isEmpty()) {
						System.err.println("Could not retrieve input from database. Please check your options.");
						exit(-1);
					}
					
					// invoke parser
					parse(text);
				} else {
					System.err.println("Option r has not been set. Read help for more information (option h)");
					exit(-1);
				}
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
				
				System.err.println("Reading input: (one sentence per line; empty line stops reading)");
				while ((line = reader.readLine()) != null && !line.isEmpty()) {
					lines.add(line);
				}
		    
				// something has been read from stdin?
				if (!lines.isEmpty()) {
					// invoke parser
					parse(lines);
				} else {
					System.err.println("No input has been read. Program is closing.");
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		exit(0);
	}
	
	private static void parse(List<String> lines) throws Exception {
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
	}
	
	private static CommandLine parseOptions(String[] args) {
		HelpFormatter help = new HelpFormatter();
		Options options = new Options();
		CommandLine commandLine = null;
		CommandLineParser parser = new BasicParser();
		
		options.addOption("db", true, "Read input from database. With this, option r(egest) must also be set.");
		options.addOption("r", true, "Read input from given regest. Needed, if option db has been set.");
		options.addOption("h", false, "Print this message.");
		
		try {
			commandLine = parser.parse(options, args);
			
			// want some help?
			if (commandLine.hasOption("h")) {
				help.printHelp("java -jar syntaxTagger.jar [options]", options);
				exit(0);
			}
		} catch (ParseException e) {
			help.printHelp("java -jar syntaxTagger.jar [options]", options);
			System.err.println("An error occured: " + e.getMessage());
			exit(-1);
		}
		
		return commandLine;
	}
	
	private static void exit (int code) {
		System.err.println("\nFinished in " + ((System.currentTimeMillis()-start)/1000) + " seconds");
		System.exit(code);
	}
}
