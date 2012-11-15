package de.syntaxparser.system;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This class provides easy database access.
 * Classes that make use of this class typically
 * invoke "openDB()", persist a new element (em.persist(Element e)) and close the database (closeDB()).
 * If this class is not extended, EntityManager must be declared public!
 * This file requires objectdb.jar.
 * It was written using objectdb version 2.4.3.
 * @author David Alfter
 * @version 0.0.1
 */
public class DBManager {

	private EntityManagerFactory emf;
	private EntityManager em;
	
	/**
	 * Constructor.
	 * Opens the specified database or creates it, if it does not exist.
	 * @param database the name of the database to access
	 */
	public DBManager (String database) {
		String db = (database.endsWith(".odb")?database:database+".odb");
		emf = Persistence.createEntityManagerFactory(db);
	}
	
	/**
	 * Opens the database for reading and writing access.
	 */
	public void openDB () {
		em = emf.createEntityManager();	
		em.getTransaction().begin();
	}
	
	/**
	 * Closes the database reading and writing access.
	 */
	public void closeDB () {
		em.getTransaction().commit();
		em.close();
		emf.close();
	}
	
	public EntityManager getEntityManager() {
		return em;
	}
}
