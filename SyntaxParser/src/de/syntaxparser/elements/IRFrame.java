package de.syntaxparser.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.syntaxparser.MorphologyProcessor;

public class IRFrame {
	private String name;
	private String morphology;
	private String casus;
	private List<String> prepositions;

	public IRFrame(String templateName, String templateMorphology, String[] templateSplitBySlash) {
		this.setName(templateName);
		this.setMorphology(templateMorphology);
		this.setCasus(MorphologyProcessor.getCasusValency(morphology));
		this.setPrepositions(templateSplitBySlash);
	}

	public IRFrame(String templateName, String templateMorphology) {
		this(templateName, templateMorphology, null);
	}
	
	public String getName() {
		return name;
	}
	
	private void setName (String name) {
		this.name = name;
	}
	
	public String getMorphology() {
		return morphology;
	}
	
	private void setMorphology (String morphology) {
		this.morphology = morphology;
	}
	
	public String getCasus() {
		return casus;
	}
	
	private void setCasus (String casus) {
		this.casus = casus;
	}
	
	public List<String> getPrepositions() {
		return prepositions;
	}
	
	private void setPrepositions(String[] prepositions) {
		if (prepositions != null)
			this.prepositions = new ArrayList<String>(Arrays.asList(prepositions));
	}
	
	public boolean hasPreposition() {
		if (prepositions != null)
			return true;
		else
			return false;
	}
	
	@Override
	public String toString() {
		String returnString = "[" + name + ", " + morphology + " (" + casus + ")";
		
		if (this.hasPreposition())
			returnString += ", " + prepositions;
		
		return returnString + "]";
	}
}
