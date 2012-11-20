package classtypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
/**
 * Represents a simple Person.
 * Person has names, none of which are mandatory.
 * This file requires objectdb.jar.
 * It was written using objectdb version 2.4.3.
 * @author David Alfter
 * @version 0.0.1
 */
@Embeddable 
public class Person implements Serializable {


	/**
	 * Default serial version id.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Names of Person
	 */
	private String title, forename, surname, maidenname;
	/**
	 * ArrayList of additional or other names
	 */
	private List<String> otherNames;
	/**
	 * Constructor.
	 */
	public Person () {
		otherNames = new ArrayList<String>();
	}
	/**
	 * Checks for the presence of a title.
	 * @return whether or not Title is set
	 */
	public boolean hasTitle () {
		return title==null?false:true;
	}
	/**
	 * Returns the title.
	 * Preferably first check whether or not title is set before invocation of this method.
	 * @return the title
	 * @see hasTitle()
	 */
	public String getTitle () {
		return title;
	}
	/**
	 *  Sets the title of a Person.
	 * @param title the title of the Person
	 */
	public void setTitle (String title) {
		this.title = title;
	}
	/**
	 * Checks for the presence of a first name.
	 * @return whether or not first name is set
	 */
	public boolean hasFirstname () {
		return forename == null ? false : true;
	}
	/**
	 * Returns the first name.
	 * Preferably first check whether or not first name is set before invocation of this method.
	 * @return the first name
	 * @see hasFirstname()
	 */
	public String getFirstname () {
		return forename;
	}
	/**
	 * Sets the first name of a Person.
	 * @param firstname the first name of the Person
	 */
	public void setFirstname (String firstname) {
		this.forename = firstname;
	}
	/**
	 * Checks for the presence of a surname.
	 * @return whether or not Surname is set
	 */
	public boolean hasSurname () {
		return surname == null ? false : true;
	}
	/**
	 * Returns the surname.
	 * Preferably first check whether or not surname is set before invocation of this method.
	 * @return the surname
	 * @see hasSurname()
	 */
	public String getSurname () {
		return surname;
	}
	/**
	 * Sets the surname of a Person.
	 * @param surname the surname of the Person
	 */
	public void setSurname (String surname) {
		this.surname = surname;
	}
	/**
	 * Checks for the presence of a maiden name.
	 * @return whether or not maiden name is set
	 */
	public boolean hasMaidenname () {
		return maidenname == null ? false : true;
	}
	/**
	 * Returns the maiden name.
	 * Preferably first check whether or not maiden name is set before invocation of this method.
	 * @return the maiden name
	 * @see hasMaidenname()
	 */
	public String getMaidenname () {
		return maidenname;
	}
	/**
	 * Sets the maiden name.
	 * @param maidenname the maiden name
	 */
	public void setMaidenname (String maidenname) {
		this.maidenname = maidenname;
	}
	/**
	 * Checks whether other names are set.
	 * @return whether other names are set
	 */
	public boolean hasOtherNames () {
		return otherNames.size() == 0 ? false : true;
	}
	/**
	 * Adds other names.
	 * Can be used as often as necessary.
	 * @param otherName other name to add
	 */
	public void addOtherName (String otherName) {
		otherNames.add(otherName);
	}
	/**
	 * Returns an ArrayList<String> containing other names.
	 * Preferably first check whether other names have been set before invocation of this method.
	 * @return an ArrayList of String containing other names 
	 * @see hasOtherNames()
	 */
	public List<String> getOtherNames () {
		return otherNames;
	}
	/**
	 * Returns the full name of a person, consisting of the concatenation of the fields 
	 * Title, First name, Surname, Maiden name and Other Names.
	 * If a field X is not set (hasX() returns false), this field will not be included.
	 * This method should always return a non-empty string.
	 * @return the full name of a Person.
	 */
	public String getFullname () {
		StringBuilder sb = new StringBuilder((hasTitle()?getTitle():"") + " " + (hasFirstname()?getFirstname():"") + " " + (hasSurname()?getSurname():"") + " " + (hasMaidenname()?"nee "+getMaidenname():""));
		if (hasOtherNames()) {
			sb.append(" (");
			for (int i = 0; i < otherNames.size(); i++) {
				if (i == 0)
					sb.append(otherNames.get(i));
				else
					sb.append(" " + otherNames.get(i));
			}
			sb.append(")");
		}
		assert sb.length() > 0;
		return sb.toString();
	}
	/**
	 * Standard toString method.
	 */
	public String toString () {
		return "Person: " + getFullname();
	}
}
