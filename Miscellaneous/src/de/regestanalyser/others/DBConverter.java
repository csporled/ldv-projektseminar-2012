package de.regestanalyser.others;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import de.regestanalyser.database.DBManager;

/**
 * Converts a database with content in former classtypes structure to the new
 * classtypes structure. CLI takes the path to the input database as first
 * option and optionally the path to the output database as second option
 * 
 * If no second option is given, the output database is named after the input
 * database + '_new'
 * 
 * @author nils
 * 
 */
public class DBConverter {
	// global variables
	private static DBManager inputDBM;
	private static DBManager outputDBM;
	private static String inputdbPath;
	private static String outputdbPath;

	public static void main(String[] args) {
		// local variables
		File inputdbFile;

		// parsing options
		if (args.length == 0) {
			System.out.println("No path to input database given.");
			System.exit(-1);
		} else {
			inputdbPath = args[0];
			if (args.length > 1)
				outputdbPath = args[1];
			else
				outputdbPath = inputdbPath.replace(".odb", "_new.odb");
		}

		// check path to input database
		inputdbFile = new File(inputdbPath);

		if (inputdbFile.exists()) {
			if (inputdbPath.endsWith(".odb") && inputdbFile.isFile()) {
				convert();
			} else {
				System.out.println("Input database is no .odb file.");
				System.exit(-1);
			}
		} else {
			System.out.println("Input database does not exist.");
			System.exit(-1);
		}
	}

	private static void convert() {
		// create DBManagers for input and output
		inputDBM = new DBManager(inputdbPath);
		outputDBM = new DBManager(outputdbPath);

		// get regests from database
		List<classtypes.Regest> regestListOld = inputDBM.getResultList(
				"SELECT r FROM Regest r", classtypes.Regest.class);

		// process every old regest
		for (classtypes.Regest regestOld : regestListOld) {
			// create new regest
			de.regestanalyser.classtypes.Regest regestNew = new de.regestanalyser.classtypes.Regest(
					regestOld.getId());
			regestNew.setClassification(regestOld.getClassification());

			// process People
			if (regestOld.getPeople() != null)
				for (classtypes.Person personOld : regestOld.getPeople()) {
					de.regestanalyser.classtypes.Person personNew = new de.regestanalyser.classtypes.Person();
					personNew.setFirstname(personOld.getFirstname());
					personNew.setSurname(personOld.getSurname());
					personNew.setMaidenname(personOld.getMaidenname());
					personNew.setTitle(personOld.getTitle());
					for (String otherName : personOld.getOtherNames())
						personNew.addOtherName(otherName);
				}

			// process content
			if (regestOld.getContent() != null)
				for (String content : regestOld.getContent())
					regestNew.addContent(content);

			// process other
			if (regestOld.getOther() != null)
				for (Serializable serial : regestOld.getOther())
					regestNew.addOther(serial);

			// persist new regest
			outputDBM.persist(regestNew);
		}

		// every regest should be converted => closing DBManagers
		inputDBM.closeDB();
		outputDBM.closeDB();
	}
}
