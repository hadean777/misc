package com.hadean777.misc.exception;


/**
 * Exception for business logic
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-05 18:30:00 +0200 (Tue, 05 May 2015) $ at revision $Revision:   $ <br>
 */
public class BusinessLogicException extends RuntimeException{
	
	private static final long serialVersionUID = -4562561678737269210L;

	/**
     * Default constructor
     */
    public BusinessLogicException() {
        super();
    }

    /**
     * Constructor
     * @param p_message - {@link String}
     */
    public BusinessLogicException(String p_message) {
        super(p_message);
    }

    /**
     * Constructor
     * @param p_message - {@link String}
     * @param p_cause - {@link Throwable}
     */
    public BusinessLogicException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }

    /**
     * Constructor
     * @param p_cause - {@link Throwable}
     */
    public BusinessLogicException(Throwable p_cause) {
        super(p_cause);
    }

}
