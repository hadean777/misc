package com.hadean777.misc.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Initialization servlet
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-05 18:30:00 +0200 (Tue, 05 May 2015) $ at revision $Revision:   $ <br>
 */
public class InitializationServlet extends HttpServlet {


	private static final long serialVersionUID = -4988285932183785244L;

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext( getServletContext() );

	}

}
