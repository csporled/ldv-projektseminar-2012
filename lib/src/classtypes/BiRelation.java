package classtypes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A simple dual relation representation.
 * Two people (Person 1 and Person 2) are in relation of type T.
 * @author David Alfter
 * @version 0.0.1
 */
@Entity
public class BiRelation {

	/**
	 * Id used by the database. DO NOT ACCESS OR MODIFY!
	 */
	@Id @GeneratedValue
	private int internid;
	/**
	 * People in this relation
	 */
	private Person p1, p2;
	/**
	 * Specification of this relation
	 */
	private String type;
	/**
	 * Constructor.
	 * Constructs a relation of type T (String) 
	 * between Person 1 and Person 2.
	 * @param p the first person in this relation
	 * @param q the second person in this relation
	 * @param s the type of relation
	 */
	public BiRelation (Person p, Person q, String s) {
		p1 = p;
		p2 = q;
		type = s;
	}
	/**
	 * Returns the first Person of this relation.
	 * @return Person 1
	 */
	public Person getPerson1 () {
		return p1;
	}
	/**
	 * Returns the second Person of this relation.
	 * @return Person 2
	 */
	public Person getPerson2 () {
		return p2;
	}
	/**
	 * Returns the type of this relation.
	 * @return the type of this relation.
	 */
	public String getRelationType () {
		return type;
	}
}
