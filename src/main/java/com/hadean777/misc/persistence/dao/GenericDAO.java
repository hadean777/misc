package com.hadean777.misc.persistence.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

import org.springframework.dao.DataAccessException;

/**
 * Defines common operations with Data Access Objects
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-07 11:30:00 +0200 (Thu, 07 May 2015) $ at revision $Revision:   $ <br>
 *
 * @param <T> - class of entity
 * @param <ID> - class of primary key of entity
 */
public interface GenericDAO<T, ID extends Serializable> {
	
	/**
	 * Flush all pending saves, updates to the database.
	 * Only invoke this for selective eager flushing, 
	 * for example when JDBC code needs to see certain changes within the SAME transaction.
	 */
	void flush();
	
	/**
	 * Returns the persistence instance of entity by the given identifier, or null if not found
	 * @param p_key - indentifies of persistence entity
	 * @return persistence instance
	 * @throws DataAccessException - in case of DB errors
	 */
	T get(ID p_key);
	
	/**
	 * Returns the persistence entity by the given identifier, throwing exception if not found
	 * @param p_key - indentifies of persistence entity
	 * @return persistence instance
	 * @throws DataAccessException - in case of DB errors
	 */
	T load(ID p_key);

	/**
	 * Saves or updates the given persistence instance 
	 * @param p_entity - the persistence instance
	 * @throws DataAccessException - in case of DB errors
	 */
	void saveOrUpdate(T p_entity);
	
	/**
	 * Deletes the given persistence instance
	 * @param p_entity - the persistence instance
	 * @throws DataAccessException - in case of DB errors
	 */
	void delete(T p_entity);
	
	/**
	 * Copies the state of the given object onto the persistent object with the same identifier.
	 * @param p_entity - the object to merge with the corresponding persistence instance
	 * @return entity
	 * @throws DataAccessException - in case of DB errors
	 */
	T merge(T p_entity);

	/**
	 * Re-read the state of the given persistent instance
	 * @param p_entity - the persistent instance to re-read 
	 * @throws DataAccessException - in case of DB errors
	 */
	void refresh(T p_entity);
	
	/**
	 * Saves or updates all given persistence instances
	 * @param p_entities - the persistent instances to save or update 
	 * @return saved entities
	 * @throws DataAccessException - in case of DB errors
	 */
	Collection<T> bulkSaveOrUpdate(Collection<T> p_entities);
	
	/**
	 * Returns DB date and time
	 * @return {@link Timestamp}
	 */
	Timestamp getCurrentTime();
}
