package com.hadean777.misc.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;

import com.hadean777.misc.AppConstants;
import com.hadean777.misc.exception.DAOException;

/**
 * Aspect to log errors occurring during application work
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-05 18:30:00 +0200 (Tue, 05 May 2015) $ at revision $Revision:   $ <br>
 */
@Aspect
public class LoggingAdvice implements Ordered {
	@Autowired
	private MessageSource messageSource;
	
	private int order = 1;

	@Override
	public int getOrder() {
		return order;
	}

	/**
	 * Sets order according to which current advice is executed
	 * @param p_order
	 */
	public void setOrder(int p_order) {
		order = p_order;
	}
	
	@AfterThrowing(pointcut = "execution(* com.hadean777.misc.persistence.dao.hibernate.*.*(..))",
			throwing = "error")
	public void executeDAOLevelLogAdivce(JoinPoint jp, Throwable error){
		Logger logger = LoggerFactory.getLogger( jp.getTarget().getClass() );
		logger.error( messageSource.getMessage( AppConstants.MSG_KEY_ERROR_LOG, null, null), error );
	}
	
	@AfterThrowing(pointcut = "execution(* com.hadean777.misc.persistence.manager.impl.*.*(..)) "
			+ " || execution(* com.hadean777.misc.auth.*.*(..))" + " || execution(* com.hadean777.misc.webapp.controller.*.*(..))",
			throwing = "error")
	public void executeBusinessLevelLogAdivce(JoinPoint jp, Throwable error){
		if( !(error instanceof DAOException) ) {
			Logger logger = LoggerFactory.getLogger( jp.getTarget().getClass() );
			logger.error( messageSource.getMessage( AppConstants.MSG_KEY_ERROR_LOG, null, null), error );
		}
	}
	
}
