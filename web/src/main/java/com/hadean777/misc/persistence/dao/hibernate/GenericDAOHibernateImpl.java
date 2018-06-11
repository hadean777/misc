package com.hadean777.misc.persistence.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hadean777.misc.persistence.dao.GenericDAO;

/**
 * Implements common operations with Data Access Objects
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-07 11:30:00 +0200 (Thu, 07 May 2015) $ at revision $Revision:   $ <br>
 * 
 * @param <T> - class of entity
 * @param <ID> - class of primary key of entity
 */
@SuppressWarnings("unchecked")
@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
public abstract class GenericDAOHibernateImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	@Autowired
	private SessionFactory sessionFactory;
	protected Class<T> modelClass;

	/**
	 * Default constructor
	 */
	public GenericDAOHibernateImpl() {
		modelClass = (Class<T>)( (ParameterizedType) getClass().getGenericSuperclass() ).getActualTypeArguments()[0];
	}
	
	/**
	 * Returns current session
	 * @return {@link Session}
	 */
	protected Session getSession() {
		return sessionFactory != null ? sessionFactory.getCurrentSession() : null;
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public T get(ID p_key) {
		return (T) getSession().get( getModelClass(), p_key );
	}

	@Override
	public T load(ID p_key) {
		return (T) getSession().load( getModelClass(), p_key );
	}

	@Override
	public void saveOrUpdate(T p_entity) {
		getSession().saveOrUpdate( p_entity );
	}

	@Override
	public void delete(T p_entity) {
		getSession().delete( p_entity );
	}

	@Override
	public T merge(T p_entity) {
		return (T) getSession().merge( p_entity );
	}

	@Override
	public void refresh(T p_entity) {
		getSession().refresh( p_entity );
	}

	@Override
	public Collection<T> bulkSaveOrUpdate(Collection<T> p_entities) {
		List<T> savedEntities = new ArrayList<T>();
		
		if( CollectionUtils.isNotEmpty( p_entities ) ) {
			Integer batchSize = 20;
			if( sessionFactory != null && sessionFactory.getClass().equals( LocalSessionFactoryBean.class ) ) {
				batchSize = Integer.valueOf( ( (LocalSessionFactoryBean) sessionFactory).
						getHibernateProperties().getProperty( "hibernate.jdbc.batch_size", "20" ) ); 
			}
			
			int count = 0;
			for(T entity : p_entities) {
				entity = merge( entity );
				saveOrUpdate( entity );
				savedEntities.add( entity );
				if( ++count % batchSize == 0 ) {
					getSession().flush();
					getSession().clear();
				}
			}
		}
		getSession().flush();
		getSession().clear();
		
		return savedEntities;	
	}

	@Override
	public Timestamp getCurrentTime() {
		SQLQuery sqlQuery = getSession().createSQLQuery( "select current_timestamp as curr_date" );
		sqlQuery.addScalar( "curr_date", TimestampType.INSTANCE );
		return (Timestamp) sqlQuery.uniqueResult();
	}
	
	protected Class<T> getModelClass() {
		return modelClass;
	}
}
