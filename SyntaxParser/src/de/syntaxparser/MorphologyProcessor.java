package de.syntaxparser;

import java.util.HashMap;

public class MorphologyProcessor {
	private static HashMap<String, String> casusMapIR;
	private static HashMap<String, String> casusMapValency;

	static {
		casusMapIR = new HashMap<String, String>();
		casusMapIR.put("SB", "Nom"); //Subject
		casusMapIR.put("OA", "Akk"); //Object
		casusMapIR.put("DA", "Dat");
		casusMapIR.put("HD", null);
		casusMapIR.put("MO", null);
		casusMapIR.put("NMC", null);
		casusMapIR.put("AC", null);
		casusMapIR.put("CJ", null);
		casusMapIR.put("NK", "name"); //named entity => casus not defineable
		casusMapIR.put("CD", null);
		casusMapIR.put("PD", null);
		casusMapIR.put("GL", null);
		casusMapIR.put("PNC", null);
		casusMapIR.put("APP", null);
		casusMapIR.put("GR", null);
		casusMapIR.put("RC", null);
		casusMapIR.put("MNR", null);
		casusMapIR.put("OC", null);
		casusMapIR.put("PG", null);
		
		casusMapValency = new HashMap<String, String>();
		casusMapValency.put("Sn", "Nom");
		casusMapValency.put("Sa", "Akk");
		casusMapValency.put("pSd", "Dat");
		casusMapValency.put("pSa", "Akk");
	}

	public static String getCasusIR(String morphology) {
		String returnString = null;
		
		if (casusMapIR.containsKey(morphology))
			returnString = casusMapIR.get(morphology);
		
		return returnString;
	}
	
	/**
	 * Check if given morphology string is contained in the Map of morphology -> casus (IR)
	 * 
	 * @param morphology morphology string to be contained in Map
	 * @return (int) 	'1' if Map contains morphology string as key
	 * 					'0' if Map doesn't contain morphology string as key
	 * 					'-1' if Map contains morphology string as key, but value is null
	 */
	public static int casusInMapIR(String morphology) {
		if (casusMapIR.containsKey(morphology)) {
			if (casusMapIR.get(morphology) == null)
				return -1;
			else
				return 1;
		}
		else
			return 0;
	}

	public static String getCasusValency(String morphology) {
		String returnString = null;
		
		if (casusMapValency.containsKey(morphology))
			returnString = casusMapValency.get(morphology);
		
		return returnString;
	}
	
	/**
	 * Check if given morphology string is contained in the Map of morphology -> casus (Valency)
	 * 
	 * @param morphology morphology string to be contained in Map
	 * @return (int) 	'1' if Map contains morphology string as key
	 * 					'0' if Map doesn't contain morphology string as key
	 * 					'-1' if Map contains morphology string as key, but value is null
	 */
	public static int casusInMapValency(String morphology) {
		if (casusMapIR.containsKey(morphology)) {
			if (casusMapValency.get(morphology) == null)
				return -1;
			else
				return 1;
		}
		else
			return 0;
	}
}
