package com.hadean777.misc.exception;

/**
 * Exception for DAO level
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-05 18:30:00 +0200 (Tue, 05 May 2015) $ at revision $Revision:   $ <br>
 */
public class DAOException extends RuntimeException {
	
	private static final long serialVersionUID = -1353122337259939326L;
	private boolean constraintViolationExceptionCatched = false;

	public DAOException() {
		super();
	}

	public DAOException( String message ) {
		super( message );
	}

    public DAOException( String p_arg0, Throwable p_arg1 ) {
        super( p_arg0, p_arg1 );
    }

    public DAOException( Throwable p_arg0 ) {
        super( p_arg0 );
    }
    
    public DAOException( boolean p_constraintViolationExceptionCatched ) {
        super();
        constraintViolationExceptionCatched = p_constraintViolationExceptionCatched;
    }

	/**
	 * @return the isConstraintViolationExceptionCatched
	 */
	public boolean isConstraintViolationExceptionCatched() {
		return constraintViolationExceptionCatched;
	}

	/**
	 * @param isConstraintViolationExceptionCatched the isConstraintViolationExceptionCatched to set
	 */
	public void setConstraintViolationExceptionCatched(
			boolean p_constraintViolationExceptionCatched) {
		constraintViolationExceptionCatched = p_constraintViolationExceptionCatched;
	}
}
