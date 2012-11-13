package de.syntaxparser.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * An execution class to run a command within a separate process.
 * 
 * 
 * @author nils
 *
 */
public class Executer {
	public BufferedReader input;
	public BufferedReader error;
	public BufferedWriter output;
	Process process;

	public boolean exec(String command) throws IOException {
		return this.exec(command, null, null);
	}

	public boolean exec(String command, String[] envp) throws IOException {
		return this.exec(command, envp, null);
	}

	public boolean exec(String command, String[] envp, File dir)
			throws IOException {
		process = Runtime.getRuntime().exec(command, envp, dir);

		input = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		error = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		output = new BufferedWriter(new OutputStreamWriter(
				process.getOutputStream()));

		return true;
	}

	public void write(String outputString) throws IOException {
		output.write(outputString);
		output.flush();
	}
	
	public void flush() throws IOException {
		output.flush();
	}
	
	public void closeOutput() throws IOException {
		output.close();
	}
	
	public void closeInput() throws IOException {
		input.close();
		error.close();
	}

	public int waitFor() throws InterruptedException {
		return process.waitFor();
	}
}