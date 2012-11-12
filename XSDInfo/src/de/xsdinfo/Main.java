package de.xsdinfo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class Main {
	private static Set<String> startElementSet = new HashSet<String>();
	private static Set<Element> elementSet = new HashSet<Element>();

	/**
	 * <Mainmethode> Liest die XSD-Datei ein (hardcoded auf ./sbr-regesten.xsd)
	 * Zum Parsen wird die pointer-Methode von StAX genutzt
	 * 
	 * @param args
	 *            übergebene Optionen (keine implelentiert)
	 */
	public static void main(String[] args) {
		InputStream in;
		try {
			in = new FileInputStream("sbr-regesten.xsd");
			XMLStreamReader reader = XMLInputFactory.newInstance()
					.createXMLStreamReader(in);

			// prüfe, ob noch XML-TAGs folgen und starte Parser
			while (reader.hasNext()) {
				parse(reader);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// erzeuge Output
		new OutputParser().parseOutput(elementSet);
	}

	/**
	 * Parser für die XSD-Datei.
	 * 
	 * @param reader
	 *            der zu lesende XMLStreamReader. Dieser muss noch Events haben
	 *            (.hasNext())
	 * @throws Exception
	 */
	private static void parse(XMLStreamReader reader) throws Exception {
		// Stack für TAG-Elemente erstellen
		List<Element> tagStack = new ArrayList<Element>();

		while (reader.hasNext()) {
			// setze Pointer auf nächstes XML-TAG und starte Parse-Vorgang
			int event = reader.next();

			switch (event) {
			case XMLStreamReader.START_ELEMENT:
				String prefix = reader.getPrefix();
				String startName = reader.getLocalName();
				// Erstelle neue Element-Instanz für aktuelles TAG
				Element element = new Element(prefix, startName);

				switch (startName) {
				// ignoriere <schema>-TAG
				case "schema":
					return;
					
				// BOOLEAN Variablen innerhalb der Element-Instanz
				case "sequence":
					tagStack.get(0).setSequence();
					break;
				case "choice":
					tagStack.get(0).setChoice();

					// prüfe auf minOccurs- und maxOccurs-Attribute und setzte
					// diese für aktuelle (Haupt-) Element-Instanz
					for (int i = 0; i < reader.getAttributeCount(); i++) {
						switch (reader.getAttributeLocalName(i)) {
						case "minOccurs":
							tagStack.get(0).setChoiceMin(
									reader.getAttributeValue(i));
							break;
						case "maxOccurs":
							tagStack.get(0).setChoiceMax(
									reader.getAttributeValue(i));
							break;
						}
					}
					break;
				case "all":
					tagStack.get(0).setAll();
					break;
				}
				
				// Erstelle eine Liste von Attributen (Name-Wert-Paar)
				for (int i = 0; i < reader.getAttributeCount(); i++) {
					element.addAttribute(reader.getAttributeLocalName(i),
							reader.getAttributeValue(i));
				}

				// wenn unbekannt, füge Element-Name zur Liste der Start-TAGs
				// hinzu
				// und schreibe aktuelles Element auf TAG-Stack
				startElementSet.add(startName);
				tagStack.add(element);
				break;
			case XMLStreamReader.END_ELEMENT:
				// ignoriere (letztes) <schema>-TAG
				if (tagStack.size() == 0)
					return;
				
				// bearbeite End-TAG abhängig vom (lokalen) Namen
				String endName = reader.getLocalName();

				// wenn dies das neueste Element im TAG-Stack schließt
				if (endName.equals(tagStack.get(tagStack.size() - 1).getName())) {
					// neuestes Element des TAG-Stacks löschen -> removedElement
					Element removedElement = tagStack
							.remove(tagStack.size() - 1);

					// bearbeite removedElement je nach (lokalem) namen
					switch (endName) {
					case "element":
						// wenn Stack nicht leer ist, ist dieses <element> ein
						// subElement
						if (!tagStack.isEmpty()) {
							tagStack.get(0).addSubElement(removedElement);
						} else {
							// markiere Element-Objekt als <element>-TAG
							if (removedElement.getType().equals("unset"))
								removedElement.setElement();
						}	
						// füge Element-Objekt es dem elementSet hinzu
						elementSet.add(removedElement);						
						break;
						
					case "simpleType":
						// markiere Element-Objekt als <simpleType>-TAG und
						// füge es dem elementSet hinzu, wenn
						// es das letzte Element im Stack ist
						if (tagStack.isEmpty()) {
							removedElement.setSimpleType();
							elementSet.add(removedElement);
						} else {
							tagStack.get(0).setSimpleType();
						}
						break;
					
					case "complexType":
						// markiere Element-Objekt als <complexType>-TAG und
						// füge es dem elementSet hinzu, wenn
						// es das letzte Element im Stack ist
						if (tagStack.isEmpty()) {
							removedElement.setComplexType();
							elementSet.add(removedElement);
						} else {
							tagStack.get(0).setComplexType();
						}
						break;
					
					case "attribute":
						// Dies ist ein <attribute>-TAG (kein Name-Wert-Paar)
						// -> füge es der Liste von Attribut-Elementen hinzu
						tagStack.get(0).addattributeElement(removedElement);
						break;
						
					case "pattern":
						// <pattern> ist wie <enumeration> ein Typ eines <restriction>-TAGs
						// -> setze restrictionType des Haupt-Elements auf 'pattern'
						tagStack.get(0).setRestrictionType("pattern");
						tagStack.get(0).addSubElement(removedElement);
						break;
						
					case "enumeration":
						// setze restrictionType des Haupt-Elements auf 'pattern' (siehe case "pattern")
						tagStack.get(0).setRestrictionType("enumeration");
						tagStack.get(0).addSubElement(removedElement);
						break;
						
					case "restriction":
						tagStack.get(0).setRestriction();
						tagStack.get(0).setBase(removedElement.getAttributeMap().get("base"));
						break;
						
					case "extension":
						tagStack.get(0).setExtension();
						tagStack.get(0).setBase(removedElement.getAttributeMap().get("base"));
						break;
					}
				}

				// wenn TAG-Stack leer ist -> return
				if (tagStack.isEmpty()) {
					return;
				}

				// case XMLStreamReader.END_ELEMENT:
				break;
			}
		}
	}
}