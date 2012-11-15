package module;
/**
 * A class for retrieving lemmas via Wortschatz Leipzig SOAP.
 * This class requires the full Baseform package, downloadable from http://wortschatz.uni-leipzig.de/axis/servlet/ServiceOverviewServlet.
 * This class can be used as command-line program and as instanced class.
 * @author David Alfter
 * @version 0.0.1
 */
public class Lemmatizer {
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
		de.uni_leipzig.wortschatz.webservice.client.baseform.BaseformClient bc = new de.uni_leipzig.wortschatz.webservice.client.baseform.BaseformClient();
		bc.setUsername("anonymous");
		bc.setPassword("anonymous");
		bc.setCorpus("de");
		bc.addParameter("Wort", word);		
		bc.execute();
		String[][] res = bc.getResult();	
		if (res.length == 0) {
			System.err.println("Could not find baseform of " + word);
			return null;
		} else {
		
		for (String[] st2 : res)
			for (String st3 : st2)
				if (st3.equals(t)) {
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
		Lemmatizer l = new Lemmatizer();
		if (args.length == 0)
			System.err.println("Usage:\njava Lemmatizer word [type]");
		else if (args.length == 1)
			System.out.println(l.getLemma(args[0]));
		else
			System.out.println(l.getLemma(args[0], args[1]));
	}
}
