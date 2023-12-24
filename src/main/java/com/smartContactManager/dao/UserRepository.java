package com.smartContactManager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartContactManager.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.email=:email")	
	public User getUsserByName(@Param("email") String email);

	@Query("SELECT u.id FROM User u")
    List<Long> findAllIds();
	
	@Query("SELECT u.email FROM User u")
    List<String> findAllEmails();
	
	User findById(Long id);
}
