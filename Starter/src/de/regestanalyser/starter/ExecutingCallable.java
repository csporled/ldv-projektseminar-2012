package de.regestanalyser.starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Instances of ExecutingCallable can be used to run a command as a Callable within a Thread ExecutorService.
 * This class implements Callable and hence overrides it's call() method.
 * The command gets executed in a new process and returns it's stdout as list of strings (line-wise).
 * 
 * @author nils
 *
 */
public class ExecutingCallable implements Callable <List<String>> {
	// global variables
	private List<String> stdout;
	private List<String> stdin;
	private String workingDir = Starter.getBinPath();
	private String command;

	/**
	 * Constructor taking a command to execute and a list of strings as stdout.
	 * 
	 * @param command the command to execute
	 * @param stdout the list of strings to be used as stdout
	 */
	ExecutingCallable (String command, List<String> stdout) {
		this.stdout = stdout;
		this.command = command;
		stdin = new ArrayList<String>();
	}
	
	/**
	 * Constructor taking a command to execute and a string as stdout.
	 * 
	 * @param command the command to execute
	 * @param stdout the string to be used as stdout
	 */
	ExecutingCallable (String command, String stdout) {
		this.stdout = new ArrayList<String>();
		this.stdout.add(stdout);
		this.command = command;
		stdin = new ArrayList<String>();
	}

	/**
	 * the overridden call() method of Callable.
	 * executes a command within a new process and returns it's stdout as list of strings (line-wise).
	 */
	@Override
	public List<String> call() {
		BufferedReader input, error;
		BufferedWriter output;
		Process process;
		String returnLine;

			try {
				// execute the barkeleyParser command
				process = Runtime.getRuntime().exec(command, null,
						new File(workingDir));

				// grab Streams
				input = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				error = new BufferedReader(new InputStreamReader(
						process.getErrorStream()));
				output = new BufferedWriter(new OutputStreamWriter(
						process.getOutputStream()));

				// write the content of 'lines' to stdout
				if (stdout != null)
					for (String line : stdout) {
						output.write(line);
					}

				// close stdout and wait for berkeley parser to finish
				output.flush();
				output.close();
				process.waitFor();

				// read stdin
				while ((returnLine = input.readLine()) != null) {
					stdin.add(returnLine);
				}

				// close stdin and stderr
				input.close();
				error.close();
			} catch (IOException e) {
				Starter.error(e);
			} catch (InterruptedException e) {
				Starter.error(e);
			}
		return stdin;
	}
}
