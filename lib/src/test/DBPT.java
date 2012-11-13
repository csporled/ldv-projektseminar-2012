package test;

import java.util.List;

import javax.persistence.TypedQuery;

import classtypes.Person;
import classtypes.Regest;
import database.DBManager;

public class DBPT extends DBManager {

	public DBPT(String database) {
		super(database);
		// TODO Auto-generated constructor stub
	}

	public void testpersist () {
		Person p1 = new Person () ;
		Person p2 = new Person ();
		Person p3 = new Person ();
		p1.setForename("Matt");
		p1.setSurname("Striker");
		p2.setForename("James");
		p2.setSurname("Becker");
		p3.setTitle("Prof");
		p3.setForename("Lindsay");
		p3.setSurname("Mayson");
		p3.setMaidenname("Braun");
		p3.addOtherName("Stein");
		p3.addOtherName("Krause");
		Regest r = new Regest("993-11-23");
		String text1 = "Long ago, when there was no wild life, we lived happily.";
		String text2 = "Someone once stole my briefcase.";
		String text3 = "Hence we left the dead and returned to our world.";
		r.addContent(text1);
		r.addContent(text2);
		r.addContent(text3);
		r.addPerson(p1);
		r.addPerson(p2);
		r.addPerson(p3);
		
		em.persist(r);
		
	}
	
	public void testretrieve () {
		
		TypedQuery<Regest> q = em.createQuery("SELECT r FROM Regest r", Regest.class);
		List<Regest> li = q.getResultList();
		System.out.println(li.get(0).toString());
		
		
	}
	
	public void run () {
		openDB ();
		testpersist();
		testretrieve();
		closeDB();
	}
	public static void main(String[] args) {
		new DBPT("testpersist").run();
	}
}
