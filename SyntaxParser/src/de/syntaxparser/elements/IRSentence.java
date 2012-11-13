/**
 * Class for sentences containing words as IRWord objects.
 * Saves a List of words (IRWord objects) and phrases (IRPhrase objects).
 * Also saves a Map of frames constisting of a name and all its words as Integer indexes of the List of words.
 */

package de.syntaxparser.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IRSentence {
	// global variables
	private List<IRWord> words; //List of words in sentence
	private Map<String, List<Integer>> frameMap; //Map of frames (String: name of frame, List<Integer>: Indexes of words that the frame consists of
	private List<IRPhrase> phraseList; //List of phrases with it's boundary information
	private int finalVerbIndex;
	private String finalVerbLemma;
	
	
	public IRSentence() {
		words = new ArrayList<IRWord>();
		frameMap = new HashMap<String, List<Integer>>();
		phraseList = new ArrayList<IRPhrase>();
		
		finalVerbIndex = -1;
	}

	/**
	 * Adds a word to sentence.
	 * 
	 * @param irWord - the word to add
	 */
	public void add(IRWord irWord) {
		words.add(irWord);
	}
	
	/**
	 * Gets the amount of words in sentence.
	 * 
	 * @return - amount of words in sentence.
	 */
	public int size() {
		return words.size();
	}
	
	/**
	 * Gets the list index of the last word added to sentence.
	 * 
	 * @return - list index of last word added to sentence.
	 */
	public int lastIndex() {
		return words.size()-1;
	}
	
	/**
	 * Get the word at given position in List.
	 * 
	 * @param i - index of word to get
	 * @return - the word at given index
	 */
	public IRWord getIRWord(int i) {
		IRWord returnValue = null;
		if (i > -1 && i<words.size())
			returnValue = words.get(i);
		return returnValue;
	}
	
	/**
	 * Get the word at given position in List and return its String.
	 * 
	 * @param i - index of word to get
	 * @return - the word at given index
	 */
	public String getWord(int i) {
		String returnString = "";
		IRWord irWord = getIRWord(i);
		if (irWord != null)
			returnString = irWord.getToken();
		return returnString;
	}
	
	public List<IRWord> getWordRange(int start, int end) {
		List<IRWord> wordRange = new ArrayList<IRWord>();
		
		if (words.size() <= end)
			return null;
		
		for (int i=start; i<=end; i++) {
			wordRange.add(words.get(i));
		}
		
		return wordRange;
	}
	
	public String getWordRangeString(int start, int end) {
		String returnString = "";
		
		if (words.size() <= end)
			return null;
		
		for (int i=start; i<=end; i++) {
			returnString += this.words.get(i).getToken() + " ";
		}
		
		return returnString;
	}
	
	/**
	 * Get List of words.
	 * 
	 * @return - List of words
	 */
	public List<IRWord> getWords() {
		return words;
	}
	
	/**
	 * Overrides the toString() method.
	 * Get the sentence only with its words. (no other information)
	 */
	@Override
	public String toString() {
		String returnString = "";
		
		for (IRWord word : words) {
			returnString += word.getToken() + " ";
		}
		
		return returnString;
	}
	
	/**
	 * Like toString() but ads some morphological and syntactical information.
	 */
	public String toStringMorphoSyntax() {
		String returnString = "";
		String morphology;
		String pos;
		
		for (IRWord word : words) {
			morphology = word.getMorphology();
			pos = word.getPos();
			
			returnString += word.getToken() + " [" + pos;
			
			if (morphology != null)
				returnString += ", " + morphology;
			
			returnString += "] ";
		}
		
		return returnString;
	}

	/**
	 * Get the Map of frames.
	 * 
	 * @return - the Map of frames
	 */
	public Map<String, List<Integer>> getFrameMap() {
		return frameMap;
	}

	/**
	 * Adds a frame with the indexes of its containing words to the Map of frames.
	 * 
	 * @param name - the name of the frame
	 * @param indexList - the List of word indexes belonging to frame
	 */
	public void addFrameMap(String name, List<Integer> indexList) {
		this.frameMap.put(name, indexList);
	}
	
	/**
	 * Get a String of the frame with given name.
	 * Return empty String if the name is not found.
	 * 
	 * @param name - the name of the frame to get a String of
	 */
	public String getFrameString(String name) {
		String log = "";
		
		if (frameMap.containsKey(name)) {
			log += "Template <" + name + ">: ";
			for (Integer wordIndex : frameMap.get(name)) {
				log += words.get(wordIndex).getToken() + " ";
			}
		}
		
		return log;
	}
	
	/**
	 * Get the last word from List of words.
	 * 
	 * @return - last word from List of words.
	 */
	public IRWord getLastWord() {
		return words.get(words.size()-1);
	}
	
	/**
	 * Adds a phrase with given name to the List of phrases.
	 * The left and right boundaries are set as '-1' (therefore not set)
	 * 
	 * @param name - the name of the phrase
	 * @return 
	 */
	public String openPhrase(String name) {
		IRPhrase phrase = new IRPhrase(name, -1, -1);
		phraseList.add(phrase);
		return phrase.getMorphology();
	}
	
	/**
	 * Opens every unopened phrase in list of phrases.
	 * (Set every left boundary to current word index)
	 */
	public void setLeftPhraseBoundary() {
		for (IRPhrase phrase : phraseList) {
			if (phrase.getLeftBoundary() == -1)
				phrase.setLeftBoundary(lastIndex());
		}
	}
	
	/**
	 * Close the first unclosed phrase at the end of List of phrases.
	 * (Set right boundary to current word index)
	 */
	public void closePhrase() {
		for (int i=phraseList.size()-1; i>=0; i--) {
			if (phraseList.get(i).getRightBoundary() == -1) {
				phraseList.get(i).setRightBoundary(lastIndex());
				return;
			}
		}
	}
	
	/**
	 * Remove the last phrase from List of phrases.
	 * (Used when a new word was added to List of words and therefore the last element added to 
	 * List of phrases is actual the morphosyntactical information of the newly added word)
	 */
	public void removeLastPhrase() {
		phraseList.remove(phraseList.size()-1);
	}
	
	/**
	 * Remove the first two phrases from List of phrases.
	 * (Those phrases are a empty one ("") and another one, that only includes every other element ("TOP")
	 * and are therefore uninteresting)
	 */
	public void removeFirstPhrases() {
		phraseList.remove(0);
		phraseList.remove(0);
	}
	
	/**
	 * Get the List of phrases.
	 * 
	 * @return - the List of phrases
	 */
	public List<IRPhrase> getPhraseList() {
		return phraseList;
	}
	
	/**
	 * Get a String of the phrase with given index.
	 * The String consists of the phrase name and all of its containing words.
	 * 
	 * @param i - the index of the phrase
	 * @return - the String of the phrase
	 */
	public String getPhraseString(int i) {
		
		String returnString = null;
		
		if (i < phraseList.size()) {
			IRPhrase phrase = phraseList.get(i);
			returnString = phrase.toString() + " - ";
			
			for (int j=phrase.getLeftBoundary(); j<=phrase.getRightBoundary(); j++) {
				returnString += words.get(j).getToken() + "[" + words.get(j).getPos() + "] ";
			}
		}
		
		return returnString;
	}
	
	/**
	 * Set final verb/auxilary
	 * 
	 * @param index the index of the final verb/auxilary
	 * @param lemma the lemma of the final verb/auxilary
	 */
	public void setFinalVerbIndex(int index) {
		if (index > -1)
			finalVerbIndex = index;
	}
	
	/**
	 * Set lemma of final verb/auxilary
	 * 
	 * @param lemma the lemma of the final verb/auxilary
	 */
	public void setFinalVerbLemma(String lemma) {
		finalVerbLemma = lemma;
	}
	
	/**
	 * Return index of the final verb/auxilary in this sentence
	 * 
	 * @return index of the final verb/auxilary in this sentence
	 */
	public int getFinalVerbIndex() {
		return finalVerbIndex;
	}
	
	/**
	 * Return lemma of the final verb/auxilary in this sentence
	 * 
	 * @return lemma of the final verb/auxilary in this sentence
	 */
	public String getFinalVerbLemma() {
		return finalVerbLemma;
	}
}
