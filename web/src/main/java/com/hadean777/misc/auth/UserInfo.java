package com.hadean777.misc.auth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Class represents authentication token with all required information about logged user
 * 
 * @author Created by hadean777 <br>
 * @author Last modified by $Author: hadean777 $ <br>
 * @author Last modified on $Date: 2015-05-05 18:30:00 +0200 (Tue, 05 May 2015) $ at revision $Revision:   $ <br>
 */
public class UserInfo extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 8738888000284716052L;

	/**
	 * Full constructor
	 * @param p_login - user's login; {@link String}
	 * @param p_password - user's password; {@link String}
	 * @param p_authorities - user's roles; {@link Collection} where each object is child class of {@link GrantedAuthority}
	 */
	public UserInfo(String p_login, String p_password,
			Collection<? extends GrantedAuthority> p_authorities) {
		super(p_login, p_password, p_authorities);
	}
}
