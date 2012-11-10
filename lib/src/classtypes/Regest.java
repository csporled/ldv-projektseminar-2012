package classtypes;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * A simple class to represent a regest.
 * A regest has an id, contains people (a list of Person) and other textual content.
 * @see Person
 * @author David Alfter
 * @version 0.0.1
 */
@Entity
public class Regest {

	@Id @GeneratedValue
	private int internid;
	/**
	 * Id value of this regest
	 */
	private String id;
	/**
	 * ArrayList of Person to store people
	 */
	private ArrayList<Person> people;
	/**
	 * ArrayList of String to store additional information.
	 */
	private ArrayList<String> content;
	/**
	 * Constructor. The only mandatory field is id.
	 * @param id the id of the regest
	 */
	public Regest (String id) {
		people = new ArrayList<Person>();
		content = new ArrayList<String>();
		this.id = id;
	}
	/**
	 * Returns the id of this regest.
	 * @return the id
	 */
	public String getId () {
		return id;
	}
	/**
	 * Sets the id of this regest.
	 * @param id the id of this regest
	 */
	public void setId (String id) {
		this.id = id;
	}
	/**
	 * Adds a Person to this regest.
	 * Can be used as often as needed.
	 * @param person the person to add
	 */
	public void addPerson (Person person) {
		people.add(person);
	}
	/**
	 * Adds textual information to this regest.
	 * @param text the text to add to content
	 */
	public void addContent (String text) {
		content.add(text);
	}
	/**
	 * Checks for the presence of people in this regest.
	 * @return whether or not this regest contains people
	 */
	public boolean hasPeople () {
		return people.size()==0?false:true;
	}
	/**
	 * Checks for textual content in this regest.
	 * @return whether or not this regest contains textual information
	 */
	public boolean hasContent () {
		return content.size()==0?false:true;
	}
	/**
	 * Returns the people contained in this regest as an ArrayList<People>.
	 * Preferably first check whether or not this regest contains people before invocation of this method.
	 * @return an ArrayList of Person
	 * @see hasPeople()
	 */
	public ArrayList<Person> getPeople () {
		return people;
	}
	/**
	 * Returns the text contained in this regest as an ArrayList<String>.
	 * Preferable first check whether or not this regest contains text before invocation of this method.
	 * @return an ArrayList of String
	 * @see hasContent()
	 */
	public ArrayList<String> getContent () {
		return content;
	}
}