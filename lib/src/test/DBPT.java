package test;

import java.util.List;

import javax.persistence.TypedQuery;

import classtypes.Person;
import classtypes.Regest;
import database.DBManager;

public class DBPT extends DBManager {

	public DBPT(String database) {
		super(database);
		// constructor. open specified database.
	}

	public void testpersist () {
		//create 3 people
		Person p1 = new Person () ;
		Person p2 = new Person ();
		Person p3 = new Person ();
		//add names to people
		p1.setFirstname("Matt");
		p1.setSurname("Striker");
		p2.setFirstname("James");
		p2.setSurname("Becker");
		p3.setTitle("Prof");
		p3.setFirstname("Lindsay");
		p3.setSurname("Mayson");
		p3.setMaidenname("Braun");
		p3.addOtherName("Stein");
		p3.addOtherName("Krause");
		//create regest
		Regest r = new Regest("993-11-23");
		//create text
		String text1 = "Long ago, when there was no wild life, we lived happily.";
		String text2 = "Someone once stole my briefcase.";
		String text3 = "Hence we left the dead and returned to our world.";
		//add people and text to regest
		r.addContent(text1);
		r.addContent(text2);
		r.addContent(text3);
		r.addPerson(p1);
		r.addPerson(p2);
		r.addPerson(p3);
		//save the regest to the database
		em.persist(r);
		
	}
	
	public void testretrieve () {
		//retrieve all objects in database
		TypedQuery<Regest> q = em.createQuery("SELECT r FROM Regest r", Regest.class);
		List<Regest> li = q.getResultList();
		//print result
		System.out.println(li.get(0).toString());		
	}
	
	public void run () {
		//open database. self explanatory
		openDB ();
		//call persist
		testpersist();
		//call retrieve
		testretrieve();
		//close database.
		closeDB();
	}
	public static void main(String[] args) {
		/* create database "testpersist.odb"
		 * open database, add object, retrieve object, print result.
		 */
		new DBPT("testpersist").run();
	}
}
