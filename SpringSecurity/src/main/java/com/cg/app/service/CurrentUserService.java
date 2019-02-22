package com.cg.app.service;

import com.cg.app.bean.CurrentUser;

/**
 * @author Dinesh.Rajput
 *
 */
public interface CurrentUserService {
	
	 boolean canAccessUser(CurrentUser currentUser, Long userId);
}