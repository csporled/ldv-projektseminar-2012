package de.xsdinfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutputParser {
	private Set<String> attributeNameSet = new HashSet<String>();
	private String fileDirPath = "output/";
	private String fileExtension = ".md";
	
	// Variablen für debugging
	private List<Element> nullParseElementList = new ArrayList<Element>();
	
	/**
	 * Erstellt die Output-Dateien.
	 * 
	 * @param elementSet das Set von Elementen, aus dem der Output generiert wird
	 */
	public void parseOutput (Set<Element> elementSet) {
		List<String> lines;
		Set<String> tagSet = new HashSet<String>();
		
		try {
			// Erstelle Set aus TAG-Namen, die dokumentiert werden
			for (Element element : elementSet) {
				Map<String, String> attributeMap = element.getAttributeMap();
				for (String key : attributeMap.keySet()) {
					if ("name".equals(key))
						tagSet.add(attributeMap.get(key));
				}
			}
		
			// Bereite Output-Verzeichnis vor
			createOutputDir();
		
			// Erstelle Dateiinhalte
			for (String tag : tagSet) {
				lines = createFileContent(tag, elementSet);
			
				// Erstelle Datei mit erzeugtem Inhalt
				writeFile(tag, lines);
			}
			
			// Konsolen-Output
			System.out.println("nullParseElementList: " + nullParseElementList.size());
		}
		catch ( Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/********************************************
	 * 
	 * Methoden zur Erstellung der Datei-Inhalte
	 * 
	 ********************************************/
	
	/**
	 * Erstellt Datei-Inhalt für eine Datei.
	 * Als Layout-Syntax wird 'Markdown' benutzt.
	 * 
	 * @param tag das TAG, das dokumentiert wird
	 * @param elementSet Set aller Element-Objekte, aus denen Informationen geholt werden
	 * @return Dateiinhalt als String-Liste (zeilenweise)
	 */
	private List<String> createFileContent(String tag, Set<Element> elementSet) {
		List<String> lines = new ArrayList<String>();
		List<Element> subElementList = null;
		List<Element> subElementListExtern = null;
		List<Element> attributeElementList = null;
		Map<String, String> attributeMap = null;
		
		boolean isRestriction = false;
		boolean isExtension = false;
		boolean isSequence = false;
		boolean isChoice = false;
		boolean isAll = false;
		boolean isAbstract = false;
		boolean isMixed = false;
				
		int choiceMin = 1;
		int choiceMax = 1;

		String buffer = null;		
		String elementType = null;
		String restrictionType = null;
		String base = null;
		String name = null;
		String substitutionGroup = null;
		
		
		// Iteriere über elementSet und fülle nötige Variablen
		for (Element element : elementSet) {
			attributeMap = element.getAttributeMap();
			
			// bearbeite Attribut-Liste (Name-Wert-Paare)
			attributeMap = element.getAttributeMap();
			name = element.getNameAttribute();
			if (name != null && name.equals(tag)) {
				if (!element.getType().equals("unset")) {
					// Element in der Hauptimplementierung (Nicht-subElement-Objekt)
					//BOOLEANs
					isRestriction = element.isRestriction();
					isExtension = element.isExtension();
					isSequence = element.isSequence();
					isChoice = element.isChoice();
					isAll = element.isAll();
					if (attributeMap.containsKey("abstract"))
						isAbstract = Boolean.parseBoolean(attributeMap.get("abstract"));
					if (attributeMap.containsKey("mixed"))
						isAbstract = Boolean.parseBoolean(attributeMap.get("mixed"));

					// INTEGERs
					choiceMin = element.getChoiceMin();
					choiceMax = element.getChoiceMax();
							
					// STRINGs
					restrictionType = element.getRestrictionType();
					elementType = element.getType();
					base = element.getBase();
					if (attributeMap.containsKey("substitutionGroup"))
						substitutionGroup = attributeMap.get("substitutionGroup");
							
					// LISTs, MAPs
					subElementList = element.getSubElementList();
					attributeElementList = element.getAttributeElementList();
				}
			}
			
			
		}
		
		// Beginne mit Ausgabe
		//====================
		// Erstelle Überschrift
		lines.add("# " + tag);
		
		// Typ des Elements
		buffer = "";
		// abstract?
		if (isAbstract)
			buffer = "abstraktes ";
		// Typ - simpleType, complexType, Element oder ausschließlich lokales Element
		if (elementType == null)
			elementType = "ausschließlich lokales Element";
		lines.add(buffer + elementType+"  ");
		// Abgeleitet?
		if (isRestriction)
			lines.add("Abgeleitet von [" + base + "](" + base + ") (restriction)");
		else if (isExtension)
			lines.add("Abgeleitet von [" + base + "](" + base + ") (extension)");
		else if (substitutionGroup != null)
			lines.add("Abgeleitet von [" + substitutionGroup + "](" + substitutionGroup + ") (substitutionGroup)");
		// mixed?
		if (isMixed)
			lines.add("Das <" + tag + ">-TAG kann in Fließtext vorkommen (mixed=true).");
		lines.add("___");
		
		// lokale Elemente
		if (!isRestriction && subElementList != null && !subElementList.isEmpty()) {
			lines.add("## lokale Elemente:");
			buffer = "";
			for (int i=0; i<subElementList.size(); i++) {
				buffer += "["+subElementList.get(i).getAttributeMap().get("name")+"]("+subElementList.get(i).getAttributeMap().get("name")+")";
				if (i<subElementList.size()-1)
					buffer += ", ";
			}
			lines.add(buffer);
			lines.add("___");
		}
		
		// subElements
		if (base != null)
			subElementListExtern = getSubElements(base, elementSet);
		
		if (subElementList != null && !subElementList.isEmpty() || subElementListExtern != null && !subElementListExtern.isEmpty()) {
			lines.add("## Unterelemente");
		
			// extension
			if (isExtension) {			
				lines = parseSubElementList("übernommene Elemente", subElementListExtern, lines);
				lines = parseSubElementList("neue Elemente", subElementList, lines);
			}
		
			// restriction
			if (isRestriction) {
				//lines = parseSubElementList("neue Elemente", subElementList, lines);
			}
		
		}
		return lines;
	}
	
	/**
	 * Erstellt eine Markdown-Liste mit Unterelementen.
	 * Jedes Element-Objekt wird einzeln geparst und der Liste angehängt.
	 * 
	 * @param title Titel der Markdown-Liste
	 * @param subElementList Liste von Element-Objekten, die einzeln geparst werden
	 * @param lines Output-Liste, an die die Markdown-Liste angehängt wird
	 */
	private List<String> parseSubElementList(String title, List<Element> subElementList, List<String> lines) {
		String buffer = "";
		
		if (subElementList == null || subElementList.isEmpty()) {
			return lines;
		}

		lines.add("* " + title);
		for (Element element : subElementList) {
			buffer = parseElement(element);
			if (buffer == null)
				nullParseElementList.add(element);
			else
				lines.add("    * " + buffer);
		}
		
		return lines;
	}
	
	/**
	 * Generiert einen String aus einem Element-Objekt.
	 * 
	 * @param element Element-Objekt, aus dem ein String erstellt wird
	 * @return String, der aus einem Element-Objekt erstellt wird
	 */
	private String parseElement(Element element) {
		Map<String, String> attributeMap = element.getAttributeMap();
		String name = element.getNameAttribute();
		String type = null;
		String ref = null;
		String minOccurs = null;
		String maxOccurs = null;
		
		for (String key : attributeMap.keySet()) {
			switch (key) {
			case "type":
				type = attributeMap.get(key);
				break;
			case "ref":
				ref = attributeMap.get(key);
				break;
			case "minOccurs":
				minOccurs = attributeMap.get(key);
				break;
			case "maxOccurs":
				maxOccurs = attributeMap.get(key);
				break;
			}
		}
		
		if (ref != null)
			return String.format("Abgeleitetes TAG von [%1](%1)", ref);
		else if (type != null && minOccurs != null && maxOccurs != null && name != null)
			return String.format("\\<[%1](%1)\\>[%2](%2)\\<%1\\> [%3..%4]", name, type, minOccurs, maxOccurs);
		else
			return null;
	}
	
	/**
	 * Durchsucht das Set aller Element-Objekte nach einem bestimmten, mit 'name'= @param tag.
	 * Danach wird dessen subElementList zurückgegeben.
	 * 
	 * @param tag der (lokale) Name des Elemenents, dessen subElementList gesucht ist
	 * @param elementSet das Set aller Element-Objekte, das durchsucht wird
	 * @return Liste der subElemente von @param tag
	 */
	private List<Element> getSubElements(String tag, Set<Element> elementSet) {
		List<Element> returnList = null;
		
		for (Element element: elementSet) {
			
		}
		
		return returnList;
	}

	/********************************************
	 * 
	 * Methoden für die Datei-Erstellung
	 * 
	 ********************************************/

	/**
	 * Erstellt eine Datei im Output-Ordner und schreibt Inhalt.
	 * 
	 * @param tag Name des TAG, für den eine Datei erstellt wird (= Dateiname)
	 * @param lines Inhalt der Datei (zeilenweise)
	 * @throws Exception
	 */
	private void writeFile(String tag, List<String> lines) throws Exception {
		File file = new File(fileDirPath + tag + fileExtension);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		
		for (String line : lines) {
			writer.write(line + "\n");
		}
		
		writer.close();
	}
	
	/**
	 * Erstellt ein Verzeichnis für die Output-Dateien.
	 * Sollte schon ein gleichnamiges Verzeichnis existieren, wird es vorher gelöscht.
	 */
	private void createOutputDir() {
		File fileDir = new File(fileDirPath);
		if (fileDir.exists())
			deleteDirectory(fileDir);
		
		fileDir.mkdir();
	}

	/**
	 * Eine rekursive Funktion zum Löschen eines Verzeichnisses.
	 * 
	 * @param fileDir ein zu löschendes Verzeichnis als File-Objekt
	 */
	private void deleteDirectory(File fileDir) {
		if (fileDir.exists()) {
			for (File file : fileDir.listFiles()) {
				if (file.isFile())
					file.delete();
				else if (file.isDirectory())
					deleteDirectory(file);
			}
		}
	}
}
