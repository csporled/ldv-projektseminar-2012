package module;

import de.uni_leipzig.wortschatz.webservice.client.baseform.BaseformClient;
/**
 * A class for retrieving lemmas via Wortschatz Leipzig SOAP.
 * This class requires the full Baseform package, downloadable from http://wortschatz.uni-leipzig.de/axis/servlet/ServiceOverviewServlet.
 * This class can be used as command-line program and as instanced class.
 * @author David Alfter
 * @version 0.0.1
 */
public class Lemmatizer {

	/**
	 * Variable for verbosity.
	 */
	private static boolean v;
	
	/**
	 * Lemmatizes a word, or returns null if no match of given type is found.
	 * @param word the word to lemmatize
	 * @param type type of word to retrieve (V for verb,...). Defaults to V if not specified.
	 * @return the lemma of word
	 * @throws Exception
	 */
	public String getLemma (String word, String... type) throws Exception {
		String t = "";
		if (type.length > 0)
			t = type[0];
		else
			t = "V";
		if (v)
			System.out.println("Starting retrieval of baseform for word " + word + " of type " + t);
		BaseformClient bc = new BaseformClient();
		bc.setUsername("anonymous");
		bc.setPassword("anonymous");
		bc.setCorpus("de");
		bc.addParameter("Wort", word);
		if (v)
			System.out.println("Executing webserver query...");
		bc.execute();
		String[][] res = bc.getResult();
		if (v && res.length > 0)
			System.out.println("Successfully retrieved " + res.length + " entries.");
		if (res.length == 0) {
			System.err.println("Could not find baseform of " + word);
			return null;
		} else {
		if (v)
			System.out.println("Checking for baseforms of type " + t);
		for (String[] st2 : res)
			for (String st3 : st2)
				if (st3.equals(t)) {
					if (v)
						System.out.println("Found baseform " + st2[0] + " of form " + word + " of type " + t);
					return st2[0];
				}
		}
		assert false; // execution should never reach this point
		return null;
	}
	/**
	 * Main method of program, used by command-line.
	 * @param args arguments to pass to method
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int k = 0;
		Lemmatizer l = new Lemmatizer();
		if (args.length == 0)
			System.err.println("Usage:\njava Lemmatizer [-v|--verbose] verb1 [verb2] [verb3] ...");
		else if (args[0].equals("-v")||args[0].equals("--verbose")) {
			k = 1;
			v = true;
		}
		for (int i = k; i < args.length; i++) {
			System.out.println(l.getLemma(args[i]));
		}
	}
}
