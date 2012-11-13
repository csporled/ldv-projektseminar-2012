/**
 * Class for template/valency entries.
 */

package de.syntaxparser.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IRValency {
	// global variables
	private String lemma;
	private List<String> context;
	private List<IRFrame> frames;
	private List<Pattern> contextPatterns; 
	
	public IRValency(String lemma, List<String> context, List<IRFrame> frames) {
		this.lemma = lemma;
		this.context = context;
		this.frames = frames;
		
		contextPatterns = new ArrayList<Pattern>();
		for (String contextElement : context) {
			Pattern pattern = Pattern.compile(contextElement);
			contextPatterns.add(pattern);
		}
	}
	
	public String getLemma() {
		return lemma;
	}
	
	public List<String> getContext() {
		return context;
	}
	
	public List<IRFrame> getFrames() {
		return frames;
	}
	
	public List<Pattern> getContextPatterns() {
		return contextPatterns;
	}
	
	@Override
	public String toString() {
		return lemma + "(" + context + "): " + frames.toString();
	}
}
