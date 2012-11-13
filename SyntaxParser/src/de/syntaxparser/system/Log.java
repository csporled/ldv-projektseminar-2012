/**
 * This class is for logging and creating log files.
 */

package de.syntaxparser.system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Log {
	// global variables
	private Map<String, List<String>> logMap;
	private boolean logDirCreated = false;
	private String logDirPath = "logs/";
	
	public Log() {
		System.err.println();
		logMap = new HashMap<String, List<String>>();
		
		File logDir = new File(logDirPath);
		delete(logDir);
		if (logDir.mkdir())
			logDirCreated = true;
		else
			System.err.println("Ordner f�r Log-Dateien konnte nicht erstellt werden. (" + logDir.getPath() + ")");
	}
	
	/**
	 * Add a String to the log.
	 * 
	 * @param logEntry - the String to add
	 * @param tag - the tag that the String is added to
	 */
	public void add(String logEntry, String tag) {
		if (logMap.containsKey(tag)) {
			logMap.get(tag).add(logEntry);
		}
		else {
			List<String> logList = new ArrayList<String>();
			logList.add(logEntry);
			logMap.put(tag, logList);
		}
	}
	
	/**
	 * Write the whole log to a new file in the log folder.
	 * The file name consists of the currently processed file with "_log" as suffix.
	 * If The log folder doesn't exit, create it.
	 * If there is a file with the same name as the log folder, delete it and then create the log folder.
	 * 
	 * @param fileName - the name of the currently processed file
	 * @throws Exception
	 */
	public void writeToFile(String fileName) throws Exception {
		if (fileName == null)
			return;
		
		if (logDirCreated) {
			fileName = fileName.replace(".txt", "_log.txt");
			OutputStream os = new FileOutputStream(logDirPath + fileName);
			Writer writer = new OutputStreamWriter(os,"UTF-8");
			BufferedWriter out = new BufferedWriter(writer);
		
			for (Map.Entry<String, List<String>> entry : logMap.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					out.write("<" + entry.getKey() + ">\n");
					for (String line : entry.getValue()) {
						out.write("\t" + line + "\n");
					}
					out.write("</" + entry.getKey() + ">\n\n");
				}
			}
		
			out.close();
			clearLog();
		} else {
			System.err.println("Log-Datei konnte nicht erstellt werden. (" + logDirPath + fileName + ")");
		}
	}
	
	/**
	 * Deletes given file/directory
	 * 
	 * @param path the directory to delete
	 * @return true if deletion successful
	 */
	public boolean delete(File path) {
		if(path.exists()) {
			if (path.isDirectory()) {
				for(File file : path.listFiles()) {
					delete(file);
				}
			}
			else
				path.delete();
		}
		return( path.delete() );
	}
	
	/**
	 * clears the log, leaving only the valency entries.
	 */
	public void clearLog() {
		logMap.put("error", new ArrayList<String>());
		logMap.put("file", new ArrayList<String>());
		logMap.put("ir", new ArrayList<String>());
		logMap.put("analysis", new ArrayList<String>());
	}
}
