package de.regestanalyser.starter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This class waits for a Future instance, produced by some Callable, to finish.
 * Afterwards it notifies all processes waiting at the future instance.
 * 
 * @author nils
 *
 */
public class WaitingThread extends Thread {
	private Future<List<String>> future;
	
	/**
	 * Constructor taking a Future instance to wait for finishing
	 */
	public WaitingThread(Future<List<String>> future) {
		this.future = future;
	}
	
	/**
	 * This Method waits for a Future instance to finish.
	 * Afterwards it notifies all processes waiting at the future instance.
	 */
	@Override
	public void run() {
		try {
			// wait for Callable to finish
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			Starter.error(e);
		}
		// notify thread
		synchronized (future) {
			future.notifyAll();
		}
	}
}
