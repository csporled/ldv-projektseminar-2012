package de.syntaxparser.system;

/**
 * Class for finding OS information
 * 
 * @author nils
 *
 */
public class OSUtils {
	private static String os = null;
	
	public OSUtils () {
		os = System.getProperty("os.name").toLowerCase();
	}
 
	public static boolean isWindows() {
 
		return (os.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (os.indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );
 
	}
 
	public static boolean isSolaris() {
 
		return (os.indexOf("sunos") >= 0);
 
	}
}
