package com.prototipo.tcc.services.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import com.prototipo.tcc.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
}
