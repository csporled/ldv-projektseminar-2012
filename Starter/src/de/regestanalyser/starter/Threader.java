package de.regestanalyser.starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Threader extends Thread {
	// global variables
	private List<String> stdout;
	private List<String> stdin;
	private String workingDir = Starter.appDir + File.separator + "tools";
	private String command;

	Threader (String command, List<String> stdout) {
		this.stdout = stdout;
		this.command = command;
		stdin = new ArrayList<String>();
	}

	Threader (String command) {
		this(command, null);
	}

	@Override
	public void run() {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getStdin() {
		return stdin;
	}
}
