/**
 * Class for single words.
 */

package de.syntaxparser.elements;

import de.syntaxparser.MorphologyProcessor;


public class IRWord {
	// global variables
	private String token = null;
	private String lemma = null;
	private String pos = null;
	private String casus = null;
	private String morphology = null;
	private int knot;
	
	public IRWord(String word, String syntaphology) {
		
		String[] array = syntaphology.split("_");
		
		this.setToken(word);
		this.setPos(array[0]);
		if (array.length > 1) {
			this.setMorphology(array[1]);
			this.setCasus(MorphologyProcessor.getCasusIR(morphology));
		}
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String word) {
		this.token = word;
	}
	
	public String getLemma() {
		return lemma;
	}
	
	public String getPos() {
		return pos;
	}
	
	public void setPos(String pos) {
		this.pos = pos;
	}
	
	public String getMorphology() {
		return morphology;
	}
	
	public void setMorphology(String morphology) {
		this.morphology = morphology;
	}
	
	public String getCasus() {
		return casus;
	}
	
	public void setCasus(String casus) {
		if (casus != null)
			this.casus = casus;
	}
	
	/**
	 * Compare two IRWord objects and return true or false whether the words are equal.
	 * 
	 * @param other - the other IRWord object to compare words with
	 * @return - true if words within IRWord objects are equal; false if not
	 */
	public boolean isSameWord(IRWord other) {
		return this.getToken().equals(other.getToken());
	}

	public int getKnot() {
		return knot;
	}

	public void setKnot(int knot) {
		this.knot = knot;
	}
}
