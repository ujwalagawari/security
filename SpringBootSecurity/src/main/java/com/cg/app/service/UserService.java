package com.cg.app.service;

import java.util.Collection;

import com.cg.app.bean.UserBean;
import com.cg.app.model.User;

/**
 * @author Dinesh.Rajput
 *
 */
public interface UserService {
	
	User getUserById(long id);

    User getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserBean userBean);
}