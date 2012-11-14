/**
 * Class for whole texts.
 * Saves data coming from the IRProcessor.
 * Data is saved in form of IRSentences within the respective tag space declared in the text file.
 */

package de.syntaxparser.elements;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class IREntry {
	// global variables
	private String filePath = null;
	private String fileDir = null;
	private List<IRSentence> text = null;
	
	public IRSentence getIRSentence(int index) {
		if (index < text.size())
			return text.get(index);
		
		return null;
	}
	
	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public List<IRSentence> getText() {
		return text;
	}
	
	public void setText(List<IRSentence> text) {
		this.text = text;
	}
}
