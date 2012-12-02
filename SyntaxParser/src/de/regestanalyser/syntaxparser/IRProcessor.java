/**
 * Processes berkeley parsed text files.
 * Reads the file line by line and gets sentence information (words, phrases, etc.)
 * Create a IREntry object per file and returns it to main class (Analyser)
 */

package de.regestanalyser.syntaxparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.regestanalyser.syntaxparser.elements.IREntry;
import de.regestanalyser.syntaxparser.elements.IRSentence;
import de.regestanalyser.syntaxparser.elements.IRWord;

public class IRProcessor {
	// global variables
	private List<IRSentence> sentenceList= new ArrayList<IRSentence>();
	private IRSentence sentence = null;
	private IRWord word = null;
	private IREntry irEntry = null;
	private int lineCount = 0;
	
	Set<String> wordMorphologySet = new HashSet<String>();
	Set<String> wordMorphologySetNull = new HashSet<String>();
	Set<String> wordMorphologySetNew = new HashSet<String>();
	Set<String> phraseMorphologySet = new HashSet<String>();
	Set<String> phraseMorphologySetNull = new HashSet<String>();
	Set<String> phraseMorphologySetNew = new HashSet<String>();

	/**
	 * Parse a List of Strings. 
	 * 
	 * @param lines List of Strings to parse
	 * @return IREntry object containing the parsed information
	 */
	public IREntry parse(List<String> lines) {
		for (String line : lines)
			sentenceList.add(processLine(line));
		
		irEntry = new IREntry();
		irEntry.setFilePath("unknown");
		irEntry.setFileDir("unknown");
		irEntry.setText(sentenceList);
		
		closeFile();

		return irEntry;
	}
	
	/**
	 * Loads and processes provided file. Creates an IREntry element to record
	 * every information found in text file.
	 * 
	 * @param currentFile the berkeley parsed text file
	 * @return the IREntry element containing every information found in text file
	 * @throws Exception
	 */
	public IREntry parse(File currentFile) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(currentFile), "UTF-8"));
		String line = null;

		log("Öffne Datei für Analyse: " + currentFile);

		while ((line = readLine(in)) != null) {
			sentence = processLine(line);
			sentenceList.add(sentence);
		}
		
		irEntry = new IREntry();
		irEntry.setFilePath(currentFile.getPath());
		irEntry.setFileDir(currentFile.getParent());
		irEntry.setText(sentenceList);
		
		closeFile();

		return irEntry;
	}

	/**
	 * Processes given line of text consisting of a berkeley parsed brackets
	 * structure
	 * 
	 * @param line
	 *            - the berkeley parsed line from text file
	 * @return - the IRSentence object with every sentence relevant information
	 */
	private IRSentence processLine(String line) {
		lineCount++;
		sentence = new IRSentence();

		log(line);

		recursiveProcessor(line.split(" "), 0, 0);
		sentence.removeFirstPhrases(); // removes the first two phrases because
										// they're irrelevant ("", "TOP")
		
		for (int i = 0; i < sentence.getPhraseList().size(); i++)
			// outputs the phrases found in sentence
			log(sentence.getPhraseString(i));
		log("Verb: " + sentence.getWord(sentence.getFinalVerbIndex()));
		log("");

		return sentence;
	}

	/**
	 * Cycles through berkeley parsed line (brackets structured sentence)
	 * recursively and find phrases + words (with morphological, structural
	 * information)
	 * 
	 * @param array
	 *            - the array to process. It includes the elements for
	 *            structural analysis
	 * @param level
	 *            - the level of structural deepness within the phrase structure
	 *            (representing brackets structure)
	 * @param i
	 *            - the current index of the array to process
	 */
	private void recursiveProcessor(String[] array, int level, int i) {
		int bracketCounter = 0;

		if (i == array.length - 1) { // if last element
			while (array[i].endsWith(")")) { // deletes brackets on the right
												// and counts the amount
				array[i] = array[i].substring(0, array[i].length() - 1);
				bracketCounter++;
			}
			word = new IRWord(array[i], array[i - 1]);
			sentence.add(word);
			rememberWordMorphology(word.getMorphology());
			//check if word is the final verb
			String pos = word.getPos();
			//System.out.println(word.getWord() + " - " + word.getPos() + " - " + word.getMorphology());
			if (pos.equals("VVFIN") || pos.equals("VAFIN") || pos.equals("VMFIN"))
				sentence.setFinalVerbIndex(sentence.lastIndex());
			for (int j = 0; j < bracketCounter; j++)
				// closes as many phrases as brackets were found
				sentence.closePhrase();
			sentence.removeLastPhrase(); // removes last 'phrase' because it's
											// the word's morphological
											// information
		}
		// if the element has brackets on both sides, then those brackets
		// shouldn't be erased
		else if (array[i].startsWith("(") && array[i].endsWith(")")) {
			// deletes  1 pair of brackets on both sides and puts them in again afterwards
			array[i] = array[i].substring(1, array[i].length() - 1);
			// after 'rescuing' the pair of brackets: delete every closing
			// bracket, count amount and decrease level of structural deepness
			while (array[i].endsWith(")")) {
				array[i] = array[i].substring(0, array[i].length() - 1);
				bracketCounter++;
				level--;
			}
			word = new IRWord("(" + array[i] + ")", array[i - 1]); // put in
																	// pair of
																	// brackets
																	// again
			sentence.add(word);
			rememberWordMorphology(word.getMorphology());
			for (int j = 0; j < bracketCounter; j++)
				// s.o.
				sentence.closePhrase();
			sentence.setLeftPhraseBoundary(); // if there are any phrases
												// without left boundary
												// (therefore 'unopened'), open
												// them
			sentence.removeLastPhrase(); // s.o.
			recursiveProcessor(array, level, i + 1);
		} else if (array[i].startsWith("(")) { // if opening bracket
			array[i] = array[i].substring(1);
			rememberPhraseMorphology(sentence.openPhrase(array[i])); // every opening bracket in line
											// opens a phrase, unneeded phrases
											// are being deleted afterwards
			recursiveProcessor(array, level + 1, i + 1);
		} else if (array[i].endsWith(")")) { // if closing bracket
			while (array[i].endsWith(")")) {
				array[i] = array[i].substring(0, array[i].length() - 1);
				bracketCounter++;
				level--;
			}
			word = new IRWord(array[i], array[i - 1]);
			sentence.add(word);
			rememberWordMorphology(word.getMorphology());
			//check if word is the final verb
			String pos = word.getPos();
			//System.out.println(word.getWord() + " - " + word.getPos() + " - " + word.getMorphology());
			if (pos.equals("VVFIN") || pos.equals("VAFIN") || pos.equals("VMFIN"))
				sentence.setFinalVerbIndex(sentence.lastIndex());
			for (int j = 0; j < bracketCounter; j++)
				sentence.closePhrase();
			sentence.setLeftPhraseBoundary(); // s.o.
			sentence.removeLastPhrase(); // s.o.
			recursiveProcessor(array, level, i + 1);
		}
	}

	private void rememberWordMorphology(String morphology) {
		if (morphology != null) {
			int casusInMap = MorphologyProcessor.casusInMapIR(morphology);
			
			wordMorphologySet.add(morphology);
			
			if (casusInMap == -1)
				wordMorphologySetNull.add(morphology);
			else if (casusInMap == 0)
				wordMorphologySetNew.add(morphology);
		}
	}
	
	private void rememberPhraseMorphology(String morphology) {
		if (morphology != null) {
			int casusInMap = MorphologyProcessor.casusInMapIR(morphology);
		
			phraseMorphologySet.add(morphology);
			
			if (casusInMap == -1)
				phraseMorphologySetNull.add(morphology);
			else if (casusInMap == 0)
				phraseMorphologySetNew.add(morphology);
		}
	}

	/**
	 * Logs an Exception
	 * 
	 * @param e the Exception to be logged
	 */
	public static void log(Exception e) {
		Main.log(e);
	}
	
	/**
	 * Logs given String. Inserts empty lines beforehand depending on 2nd
	 * parameter. Sets log tag to 'ir'.
	 * 
	 * @param logEntry
	 *            - the String to log
	 * @param emptyLines
	 *            - the amount of empty lines to insert
	 */
	private void log(String logEntry, int emptyLines) {
		for (int i = 0; i < emptyLines; i++)
			log("");
		log(logEntry, "ir");
	}

	/**
	 * Logs given String. Sets log tag to 'ir'.
	 * 
	 * @param logEntry
	 *            - the String to log
	 */
	private void log(String logEntry) {
		log(logEntry, "ir");
	}

	/**
	 * Logs given String. Uses given log tag.
	 * 
	 * @param logEntry
	 *            - the String to log
	 * @param tag
	 *            - the log tag to use for logging
	 */
	private void log(String logEntry, String tag) {
		Main.log(logEntry, tag);
	}

	/**
	 * Reads new line of text from textfile provided by BufferedReader. Counts
	 * amount of lines read (s. 'lineCount').
	 * 
	 * @param in
	 *            - the BufferedReader to read from
	 * @return - the line of text
	 * @throws IOException
	 */
	private String readLine(BufferedReader in) throws IOException {
		String line = in.readLine();

		return line;
	}

	/**
	 * Output some more inpormation regarding the parsing of this data.
	 */
	private void closeFile() {
		log("Dateianalyse abgeschlossen. (" + lineCount + " Zeilen eingelesen)",
				2);
		
		//log every seen morphology type
		log("Wort-Morphologie: " + wordMorphologySet, 1); //word morphology
		if (wordMorphologySetNull.size() > 0)
			log("Wort-Morphologie (unbekannter Kasus): " + wordMorphologySetNull);
		if (wordMorphologySetNew.size() > 0)
			log("Wort-Morphologie (neu in Liste): " + wordMorphologySetNew);
		log("Phrasen-Morphologie: " + phraseMorphologySet); //phrase morphology
		if (phraseMorphologySetNull.size() > 0)
			log("Phrasen-Morphologie (unbekannter Kasus): " + phraseMorphologySetNull);
		if (phraseMorphologySetNew.size() > 0)
			log("Phrasen-Morphologie (neu in Liste): " + phraseMorphologySetNew);
		wordMorphologySet.addAll(phraseMorphologySet);
		log("gesamte Liste der Morphologien: " + wordMorphologySet);
	}
}
