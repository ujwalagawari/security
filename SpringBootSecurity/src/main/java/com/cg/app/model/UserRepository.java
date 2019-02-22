package com.cg.app.model;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Dinesh.Rajput
 *
 */
public interface UserRepository extends CrudRepository<User, Long>{
	
	User findOneByEmail(String email);
}