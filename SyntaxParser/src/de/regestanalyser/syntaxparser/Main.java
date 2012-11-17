package de.regestanalyser.syntaxparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.regestanalyser.database.DBManager;
import de.regestanalyser.syntaxparser.elements.IREntry;
import de.regestanalyser.syntaxparser.system.Log;
import de.regestanalyser.syntaxparser.system.OptionParser;

/**
 * Takes Input from files, directories with files (option '-file') or stdin (option '-stdin') to process.
 * This Input needs to be processed by the berkeley parser (http://nlp.cs.berkeley.edu/).
 * 
 * @author nils
 * 
 */
public class Main {
	private static double start = System.currentTimeMillis();
	private static Log log;
	public static OptionParser options;
	public static List<String> lines = new ArrayList<String>();
	public static Map<String, List<String>> taggedLinesMap;
	
	// global variables
	private static String currentFileName;

	public static void main(String[] args) {
		options = new OptionParser();
		options.parse(args);
		log = new Log(options.logPath);

		// if files == null -> read from stdin
		List<File> files = options.getFiles();
		List<IREntry> irEntries = new ArrayList<IREntry>();

		try {
			IRProcessor irProcessor = new IRProcessor();
			
			// if option '-stdin' is set -> read from standard input stream
			if (options.stdin) {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			    String line;
			    
			    // loop while stdin is open and there's no empty line
			    System.err.println("Reading input from stdin: (one sentence per line; an empty line stops reading.)");
			    while ((line = in.readLine()) != null && line.length() != 0)
			    	lines.add(line);
			    
			    // pass 'lines' ArrayList to irProcessor
			    System.err.println("Parsing input...");
				irEntries.add(irProcessor.parse(lines));
				    
				// writes log to a log file
				System.err.println("Parsing finished. Writing result to log file.");
				logToFile("stdin");
			}
			
			// if option '-stdin' is not set, '-file' must be -> read from File objects within 'files' ArrayList
			else {
			    for (File currentFile : files) {
					currentFileName = currentFile.getName();
					
					// pass 'currentFile' file object to irProcessor
					System.err.println("Parsing: " + currentFileName);
					irEntries.add(irProcessor.parse(currentFile));
					
					// writes log to a log file
					System.err.println("Parsing of " + currentFileName + " finished. Writing result to log file.\n");
					logToFile(currentFileName);
				}
		    }
			
			// if option '-db' is set -> write to database
			if (options.database != null) {
				// create database with name set by '-db' option or open it if it already exists
				DBManager dbManager = new DBManager(options.database);
				
				// open database, write every IREntry object from 'irEntries' List to it and close it afterwards
				System.err.println("open/create database '" + options.database + "' and update data.");
				for (IREntry irEntry : irEntries)
					dbManager.getEntityManager().persist(irEntry);
				dbManager.closeDB();
			}
			
			exit(0, null);
		} catch (Exception e) {
			e.printStackTrace();
			// log(e.toString(), "error");
			exit(-1, currentFileName);
		}
	}

	/**
	 * Logs an Exception
	 * 
	 * @param e
	 *            the Exception to be logged
	 */
	public static void log(Exception e) {
		e.printStackTrace();
	}

	/**
	 * Logs given String. Uses given log tag.
	 * 
	 * @param logEntry
	 *            - the String to log
	 * @param tag
	 *            - the log tag to use for logging
	 */
	public static void log(String logEntry, String tag) {
		log.add(logEntry, tag);

		if (options.hasLog() && options.log.contains(tag) || tag.equals("error") && !options.noError)
			System.out.printf("%-78s\r\n", logEntry);
	}

	/**
	 * Write the log to a file. The file name varies with the currently
	 * processed file.
	 * 
	 * @param fileName
	 *            - name of currently processed file
	 */
	public static void logToFile(String fileName) {
		try {
			log.writeToFile(fileName);

		} catch (Exception e) {
			// e.printStackTrace();
			log(e);
		}
	}

	/**
	 * Exit program. Write the log to a file beforehand. The file name of the
	 * log varies with the currently processed file.
	 * 
	 * @param exitCode
	 *            - the integer value to exit the program with (0: normal exit,
	 *            other: abnormal exit e.g. due to error)
	 * @param fileName
	 *            - the name of the currently processed file
	 */
	public static void exit(int exitCode, String fileName) {
		try {
			log.writeToFile(fileName);
			System.err.println("\r\nProgramm beendet\r\nLaufzeit: " + ((System.currentTimeMillis()-start)/1000) + " Sekunden");
			System.exit(exitCode);
		} catch (Exception e) {
			// e.printStackTrace();
			log(e);
		}
	}
}
