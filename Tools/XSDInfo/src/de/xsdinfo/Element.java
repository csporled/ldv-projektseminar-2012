package de.xsdinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Element {
	private boolean element;
	private boolean simpleType;
	private boolean complexType;
	private boolean sequence;
	private boolean choice;
	private boolean all;
	private boolean restriction;
	private boolean extension;
	

	private String prefix;
	private String name;
	// muss String sein, da 'unbounded' möglich :(
	private String choiceMin;
	private String choiceMax;
	private String restrictionType;
	private String base;

	private Map<String, String> attributeMap;
	private List<Element> subElementList;
	private List<Element> attributeElementList;

	Element(String prefix, String name) {
		element = false;
		complexType = false;
		sequence = false;
		choice = false;

		choiceMin = "1";
		choiceMax = "1";

		setPrefix(prefix);
		setName(name);

		attributeMap = new HashMap<String, String>();
		subElementList = new ArrayList<Element>();
		attributeElementList = new ArrayList<Element>();
	}

	// setter, getter BOOLEANs
	public void setElement() {
		element = true;
	}

	public boolean isElement() {
		return element;
	}

	public void setSimpleType() {
		simpleType = true;
	}

	public boolean isSimpleType() {
		return simpleType;
	}

	public void setComplexType() {
		complexType = true;
	}

	public boolean isComplexType() {
		return complexType;
	}

	public void setSequence() {
		sequence = true;
	}

	public boolean isSequence() {
		return sequence;
	}

	public void setChoice() {
		choice = true;
	}

	public boolean isChoice() {
		return choice;
	}

	public void setAll() {
		all = true;
	}

	public boolean isAll() {
		return all;
	}

	public void setRestriction() {
		restriction = true;
	}

	public boolean isRestriction() {
		return restriction;
	}

	public void setExtension() {
		extension = true;
	}

	public boolean isExtension() {
		return extension;
	}
	
	/**
	 * Gibt den Typ des der Element-Instanz als String zurück.
	 * Mögliche Werte: 'element', 'simpleType', 'complexType'
	 * 
	 * @return Typ der Element-Instanz als String
	 */
	public String getType() {		
		if (element)
			return "element";
		
		if (complexType)
			return "complexType";
		
		if (simpleType)
			return"simpleType";
		
		return "unset";
	}

	// setter, getter STRINGs
	public void setPrefix(String prefix) {
		if (prefix != null)
			this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setName(String localName) {
		if (localName != null)
			this.name = localName;
	}

	public String getName() {
		return name;
	}

	public void setChoiceMin(String choiceMin) {
		this.choiceMin = choiceMin;
	}

	/**
	 * minOccurs des <choice>-TAG als Integer
	 * 
	 * @return minOccurs des <choice>-TAG (-1 = 'unbounded')
	 */
	public int getChoiceMin() {
		switch (choiceMin) {
		case "unbounded":
			return -1;

		default:
			return Integer.parseInt(choiceMin);
		}
	}

	public void setChoiceMax(String choiceMax) {
		this.choiceMax = choiceMax;
	}

	/**
	 * maxOccurs des <choice>-TAG als Integer
	 * 
	 * @return maxOccurs des <choice>-TAG (-1 = 'unbounded')
	 */
	public int getChoiceMax() {
		switch (choiceMax) {
		case "unbounded":
			return -1;

		default:
			return Integer.parseInt(choiceMax);
		}
	}

	public void setRestrictionType(String restrictionType) {
		if (restrictionType != null)
			this.restrictionType = restrictionType;
	}

	public String getRestrictionType() {
		return restrictionType;
	}

	public void setBase(String base) {
		if (base != null)
			this.base = base;
	}

	public String getBase() {
		return base;
	}

	/**
	 * Fügt der Attribut-HashMap ein neues hinzu. Jedes Name-Wert-Paar ist
	 * eindeutig innerhalb dieser Element-Instanz.
	 * 
	 * @param name
	 *            Name des Attributs
	 * @param value
	 *            Wert des Attributs
	 */
	public void addAttribute(String name, String value) {
		if (name != null && value != null)

			attributeMap.put(name, value);
	}
	
	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	/**
	 * Fügt der Liste von Unterelementen ein neues hinzu.
	 * 
	 * @param subElement
	 *            Das Element, das der Liste hinzugefügt werden soll
	 */
	public void addSubElement(Element subElement) {
		subElementList.add(subElement);
	}
	
	public List<Element> getSubElementList() {
		return subElementList;
	}
	
	/**
	 * Fügt der Liste von <attribute>-TAGs ein neues hinzu.
	 * 
	 * @param attributeElement
	 *            Das Element, das der Liste hinzugefügt werden soll
	 */
	public void addattributeElement(Element attributeElement) {
		attributeElementList.add(attributeElement);
	}
	
	public List<Element> getAttributeElementList() {
		return attributeElementList;
	}
	
	/**
	 * Gibt Wert des 'name'-Attributs zurück.
	 * 
	 * @return Wert des 'name'-Attributs
	 */
	public String getNameAttribute() {
		return getAttributeMap().get("name");
	}
}