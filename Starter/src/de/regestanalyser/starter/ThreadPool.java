package de.regestanalyser.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Creates an ExecutorService instance to manage multi threading.
 * 
 * @author nils
 * 
 */
public class ThreadPool {
	private ExecutorService executor;
	private List<Future<List<String>>> taskList;

	/**
	 * Constructor
	 */
	public ThreadPool() {
		executor = Executors.newCachedThreadPool();
		taskList = new ArrayList<Future<List<String>>>();
	}

	/**
	 * Creates an instance of SingleThread (with stdout=null) and executes it
	 * with this instance's ExecutorService. Invokes the overloaded submit
	 * method taking a command string and a list of Strings as stdout.
	 * 
	 * @param command
	 *            the command string to be executed by the instance of
	 *            SingleThread
	 */
	public Future<List<String>> submit(String command) {
		return this.submit(command, null);
	}

	/**
	 * Creates an instance of SingleThread and executes it with this instance's
	 * ExecutorService. Invokes the overloaded submit method taking a instance
	 * of Callable.
	 * 
	 * @param command
	 *            the command string to be executed by the instance of
	 *            SingleThread
	 * @param stdout
	 *            the list of strings to be used as this Thread's stdout
	 */
	public Future<List<String>> submit(String command, List<String> stdout) {
		ExecutingCallable task = new ExecutingCallable(command, stdout);
		return this.submit(task);
	}

	/**
	 * Executes an instance of Callable. This method invokes the submit() method
	 * of this instance's ExecutorService. Furthermore the instance of Callable
	 * is added to a task list ordered chronologically.
	 * 
	 * @param task
	 *            the instance of Callable to execute
	 */
	public Future<List<String>> submit(Callable<List<String>> task) {
		Future<List<String>> f = executor.submit(task);
		taskList.add(f);

		return f;
	}

	/**
	 * This method takes the current list of tasks and produces a new
	 * OutputThread from it. The OutputThread processes the list of tasks and
	 * writes their output to the output pane.
	 * 
	 * Afterwards the list of tasks is cleared.
	 */
	public void finishTasks() {
		if (!taskList.isEmpty()) {
			new OutputThread(taskList).start();
			taskList.clear();
		}
	}
}
