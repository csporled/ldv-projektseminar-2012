package de.regestanalyser.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This class produces output from instances of Future returned by some Callables.
 * There are two different Contructors:
 * 
 * 1) The first takes one Future instance
 * 2) The second one takes a list of Future instances
 * 
 * Afterwards it processes each future instance in order.
 * If the Callable corresponding to the future instance is already finished, the list of strings contained in the future instance is written to the output pane.
 * Else, if the Callable is still running, the method waits and another Thread is started to notify it when the Callable finished its work.
 * 
 * @author nils
 *
 */
public class OutputThread extends Thread{
	private List<Future<List<String>>> futureList;

	/**
	 * Constructor taking one Future instance.
	 * 
	 * @param f the Future instance to process
	 */
	public OutputThread(Future<List<String>> f) {
		futureList = new ArrayList<Future<List<String>>>();
		futureList.add(f);
	}
	
	/**
	 * Constructor taking a list of Future instances.
	 * 
	 * @param list the list of Future instances to process
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public OutputThread(List<Future<List<String>>> list) {
		futureList = new ArrayList<Future<List<String>>>();
		for (Future f : list)
			futureList.add(f);
	}
	
	/**
	 * This method checks if the Callable instance corresponding to the Future instance is finished.
	 * If not, it waits for it to finish.
	 * If it is already finished, the output taken from the future instance is written to the output pane
	 */
	@Override
	public void run() {
		double start = System.currentTimeMillis();
		Starter.setWaitinLayerVisibility(true);
		
		// process list of Future instances and output them in order
		for (Future<List<String>> future : futureList) {
			// if Callable is not finished wait for it
			if (!future.isDone()) {
				try {
					new WaitingThread(future).start();
					synchronized (future) {
						future.wait();
					}
				} catch (InterruptedException e) {
					Starter.error(e);
				}
			}
			
			// output list of Strings from future ^^
			try {
				for (String line : future.get())
					Starter.addOutput(line);
			} catch (InterruptedException | ExecutionException e) {
				Starter.error(e);
			}
		}
		
		Starter.setWaitinLayerVisibility(false);
		
		Starter.addOutput("The threads finished in "+((System.currentTimeMillis()-start)/1000)+" seconds.");
	}
}
