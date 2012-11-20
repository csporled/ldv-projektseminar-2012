package de.regestanalyser.database;

import java.util.List;

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
		em = emf.createEntityManager();	
	}
	
	/**
	 * Get the EntityManager for manual query
	 * 
	 * @return EntityManager object of DBManager instance
	 */
	public EntityManager getEntityManager () {
		return em;
	}
	
	public EntityManager createEntityManager () {
		return emf.createEntityManager();
	}
	
	/**
	 * Closes the database reading and writing access.
	 */
	public void closeDB () {
		em.close();
		emf.close();
	}
	
	/**
	 * Writes Entity to database.
	 * 
	 * @param object the Entity, that shall be written to dadabase
	 */
	public void persist(Object object) {
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
	}
	
	/**
	 * Makes given untyped query towards the database and returns the result.
	 * 
	 * @param query the query as String
	 * @return an Object as result of the query
	 */
	public Object getSingleResult(String query) {
		Object result;
		
		result = em.createQuery(query).getSingleResult();
		
		return result;
	}
	
	/**
	 * Makes given untyped query towards the database and returns the result.
	 * 
	 * @param query the query as String
	 * @return a List of Objects as result of the query
	 */
	@SuppressWarnings("rawtypes")
	public List getResultList(String query) {
		List result;
		
		result = em.createQuery(query).getResultList();
		
		return result;
	}
	
	/**
	 * Makes given typed query towards the database and returns the result.
	 * 
	 * @param query the query as String
	 * @param type the type of the query
	 * @return an typed Object as result of the query
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSingleResult(String query, Class<T> type) {
		T result;
		
		result = (T) em.createQuery(query, type.getClass()).getSingleResult();
		
		return result;
	}
	
	/**
	 * Makes given untyped query towards the database and returns the result.
	 * 
	 * @param query the query as String
	 * @param type the type of the query
	 * @return a List of typed Objects as result of the query
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(String query, Class<T> type) {
		List<T> result;
		
		result = (List<T>) em.createQuery(query, type.getClass()).getResultList();
		
		return result;
	}
}
