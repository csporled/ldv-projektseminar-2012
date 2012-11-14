package de.syntaxparser.system;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.syntaxparser.Main;

/**
 * Defines all arguments given as program parameters used by this Program
 * 
 * @author nils
 */
public class OptionParser {
	//sets used for processing of arguments
	private Map<String, Option> nameToOption;
	private Map<String, Field> nameToField;
	private Set<String> requiredOptions;
	private Set<String> defaultedOptions;
	private List<File> files;
	
	public Set<String> seenOptions;
	
	//actual arguments	
	@Option(name = "-file", usage = "Datei/Ordner mit Dateien zur Analyse. Wenn nicht gesetzt, wird '-stdin' benötigt.")
	public String filePath;
	
	@Option(name = "-db", usage = "Daten werden in einer ObjectDB Datenbank mit angegebenem Namen gespeichert.")
	public String database;
	
	@Option(name = "-stdin", usage = "nötig, wenn vom Standard Input Stream (stdin), anstatt einer Datei geparst werden soll. Wenn nicht gesetzt, wird '-file' benötigt.")
	public boolean stdin;
	
	@Option(name = "-noerror", usage = "Wenn gesetzt, werden keine Fehlermeldungen zur Konsole ausgegeben.")
	public boolean noError;
	
	@Option(name = "-log", usage = "Wenn gesetzt, werden Logs mit diesen Tags zur Konsole ausgegeben (Tags getrennt durch Kommata)")
	public Set<String> log;
	
	/**
	 * Constructor: creates the sets used by the option processor.
	 */
	public OptionParser() {
		nameToOption = new HashMap<String, Option>();
		nameToField = new HashMap<String, Field>();
		requiredOptions = new HashSet<String>();
		defaultedOptions = new HashSet<String>();
		seenOptions = new HashSet<String>();
		
		for (Field field : OptionParser.class.getDeclaredFields()) {
			Option option = field.getAnnotation(Option.class);
			if (option == null)
				continue;
			nameToOption.put(option.name(), option);
			nameToField.put(option.name(), field);
			if (option.required())
				requiredOptions.add(option.name());
			if (option.defaulted())
				defaultedOptions.add(option.name());
		}
	}
	
	/**
	 * Parse options
	 * 
	 * @param args Array of options given to program at start
	 */
	@SuppressWarnings("rawtypes")
	public void parse(String[] args) {
		try {
			//Set argument depending on argument type
			for (int i=0; i<args.length; i++) {	
				if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("--help"))
					usage();
				
				Option option = nameToOption.get(args[i]);
				if (option == null) {
					log(args[i] + ": Unbekannter Parameter");
					continue;
				}
				
				seenOptions.add(args[i]);
				
				Field field = nameToField.get(args[i]);
				Class fieldType = field.getType();
				
				if (fieldType == boolean.class) {
					field.setBoolean(this, true);
					log(String.format("%-30s => true", option.name()));
					continue;
				}
				
				i++;
				String value = args[i];
				if (fieldType == String.class)
					field.set(this, args[i]);
				else if (fieldType == Set.class) {
					/*genericType = field.getGenericType();
					if (genericType == String.class) {*/
						String[] array = args[i].split(",");
						Set<String> set = new HashSet<String>(Arrays.asList(array));
						field.set(this, set);
						value = set.toString();
					//}
				}
				log(String.format("%-30s => %s", option.name(), value));
			}
			log("");
			
			//Check if either -file or -stdin is set
			if (!seenOptions.contains("-file") && !seenOptions.contains("-stdin")) {
				System.err.println("Zu wenig Parameter gesetzt! '-file' oder '-stdin' ist benötigt. Siehe '-h' für weitere Informationen über die Nutzung der Parameter.");
				Main.exit(-1, null);
			}
			
			//Handle required arguments
			requiredOptions.removeAll(seenOptions);
			if (!requiredOptions.isEmpty()) {
				for (String name : requiredOptions) {
					log(String.format("%-30s%s", name, "Parameter ben�tigt, aber nicht gesetzt. Nutzung: 'informationRetriever.jar -h'"), "error");
				}
				log("");
				Main.exit(-1, null);
			}
			
			//Handle defaulted arguments
			defaultedOptions.removeAll(seenOptions);
			if (!defaultedOptions.isEmpty()) {
				for (String name : defaultedOptions) {
					log(String.format("%-30s%s", name, "Parameter nicht gesetzt. Nutze vorgegebenen Wert: " + nameToField.get(name).get(this)));
				}
				log("");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			//log(e.toString(), "error");
			Main.exit(-1, null);
		}
		
		// set files for analysis
		setFiles();
	}

	/**
	 * Print a usage description of all options
	 */
	private void usage() {
		System.out.println("Argumentenliste mit Erkl�rungen:");
		for (Option option : nameToOption.values()) {
			System.out.println(String.format("%-30s%s", option.name(), option.usage()));
		}
		System.out.println(String.format("%-30s%s", "-h", "Zeigt diese Arumentenliste mit Erkl�rungen."));
		Main.exit(0, null);
	}
	
	/**
	 * Get file(s) and write it/them to List of files to process.
	 * 
	 * @param path path to file/folder
	 * @return list of files to process
	 */
	private void setFiles() {
		// if filePath == null -> read from stdin
		if (filePath == null)
			return;
		
		String path = filePath;

		List<File> files = new ArrayList<File>();
		File file = new File(path);
		log("");

		if (file.exists()) {
			if (file.isFile()) {
				log("Folgende Datei wird zur Bearbeitung vorgemerkt:");
				log(path);
				files.add(file);
			} else if (file.isDirectory()) {
				log("Folgende Dateien im Ordner werden zur Bearbeitung vorgemerkt:");
				File[] elements = file.listFiles();
				for (File element : elements) {
					if (element.isFile())
						log(path + element.getName());
					files.add(element);
				}
			}
		} else {
			System.err.println("'" + path + "': Die Datei oder der Ordner konnte nicht gefunden werden.");
			Main.exit(-1, null);
		}

		this.files = files;
	}
	
	/**
	 * Return the List of File objects
	 * 
	 * @return List of File objects
	 */
	public List<File> getFiles() {
		return files;
	}
	
	/**
	 * Test if at least one argument set
	 * 
	 * @return true, if at least one argument was set
	 */
	public boolean isEmpty() {
		return seenOptions.isEmpty();
	}
	
	/**
	 * Test if the log option is set
	 * 
	 * @return true, if at least one argument was set
	 */
	public boolean hasLog() {
		return seenOptions.contains("-log");
	}
	
	/**
	 * Logs given String.
	 * Sets log tag to 'options'.
	 * 
	 * @param logEntry - the String to log
	 */
	private static void log(String logEntry) {
		log(logEntry, "options");
	}
	
	/**
	 * Logs given String.
	 * Uses given log tag.
	 * 
	 * @param logEntry - the String to log
	 * @param tag - the log tag to use for logging
	 */
	public static void log(String logEntry, String tag) {
		Main.log(logEntry, tag);
	}
}
