package de.syntaxtagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tagger implements Runnable {
	// global variables
	private static Lock lock;
	private List<String> lines;
	private List<String> returnList;
	
	Tagger (List<String> lines) {
		this.lines = lines;
		lock = new ReentrantLock();
		returnList = new ArrayList<String>();
	}

	@Override
	public void run() {
		BufferedReader input, error;
		BufferedWriter output;
		Process process;
		String returnLine;
		
		String command = "java -jar berkeleyParser.jar -gr gerNegra.01.utf8";
		String berkeleyDir = "berkeleyParser/";
		
		try {
			// execute the barkeleyParser command
			process = Runtime.getRuntime().exec(command, null, new File(berkeleyDir));
			
			// grab Streams
			input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			
			// write the content of 'lines' to stdout
			for (String line : lines) {
				output.write(line);
			}
			
			// close stdout and wait for berkeley parser to finish
			output.flush();
			output.close();
			process.waitFor();
			
			// read stdin
			while ((returnLine = input.readLine()) != null) {
				returnList.add(returnLine);
			}
			
			// close stdin and stderr
			input.close();
			error.close();
			
			// write lines from stdin to variable in Main class (thread save)
			lock.lockInterruptibly();
			Main.taggedLines = returnList;
			lock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
