package com.hadean777.misc.auth;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


/**
 * Application authentication manager
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-05 18:30:00 +0200 (Tue, 05 May 2015) $ at revision $Revision:   $ <br>
 */
public class AppAuthManager implements AuthenticationManager {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public Authentication authenticate(Authentication p_auth)
			throws AuthenticationException {
		UserInfo userInfo = null;
		
		if( p_auth != null ) {
			String login = (String) p_auth.getPrincipal();
			String pwrd = (String) p_auth.getCredentials();
			
			
			if( StringUtils.isNotBlank( login ) && StringUtils.isNotBlank( pwrd )
					&& StringUtils.equalsIgnoreCase( login, pwrd ) ) {
				userInfo = new UserInfo( login, pwrd, 
						Arrays.asList( new SimpleGrantedAuthority( "ROLE" ) ) );
			} else {
				throw new BadCredentialsException( messageSource.getMessage( "login.error.incorrect.login.info",
						null, null ) );
			}
		} else {
			throw new BadCredentialsException( messageSource.getMessage( "login.error.incorrect.login.info",
					null, null ) );
		}
		
		return userInfo;
	}

}
