/**
 * Class for phrases.
 * Used inside the IRSentence class.
 */

package de.syntaxparser.elements;

import javax.persistence.Embeddable;

import de.syntaxparser.MorphologyProcessor;

@Embeddable
public class IRPhrase {
	private int leftBoundary;
	private int rightBoundary;
	private String type;
	private String morphology;
	private String casus;
	
	public IRPhrase(String name, int leftBoundary, int rightBoundary) {
		
		String[] array = name.split("_");
		this.setType(array[0]);
		this.setMorphology(null);
		if (array.length == 2) {
			this.setMorphology(array[1]);
			this.setCasus(MorphologyProcessor.getCasusIR(morphology));
		}
		this.setLeftBoundary(leftBoundary);
		this.setRightBoundary(rightBoundary);
	}
	
	public String getCasus() {
		return casus;
	}
	
	public void setCasus(String casus) {
		if (casus != null)
			this.casus = casus;
	}

	public String getName() {
		return type + "_" + morphology;
	}
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setMorphology(String morphology) {
		this.morphology = morphology;
	}
	
	public String getMorphology() {
		return morphology;
	}

	public int getLeftBoundary() {
		return leftBoundary;
	}

	public void setLeftBoundary(int leftBoundary) {
		this.leftBoundary = leftBoundary;
	}

	public int getRightBoundary() {
		return rightBoundary;
	}

	public void setRightBoundary(int rightBoundary) {
		this.rightBoundary = rightBoundary;
	}
	
	/**
	 * Overrides toString() method.
	 * Get a String with information arranged more neatly
	 */
	@Override
	public String toString() {
		String returnString;
		
		returnString = type;
		if (morphology != null) {
			if (MorphologyProcessor.casusInMapIR(morphology) == 1)
				returnString += " (" + MorphologyProcessor.getCasusIR(morphology) + ")";
			else
				returnString += " (null [" + morphology + "])";
		}
		returnString +=  " [" + leftBoundary + ", " + rightBoundary + "]";
		
		return returnString;
	}
	
	public String getWordTokens(IRSentence sentence) {
		return sentence.getWordRangeString(leftBoundary, rightBoundary);
	}
}
