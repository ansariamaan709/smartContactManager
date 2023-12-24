package com.smartContactManager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartContactManager.entities.Contact;
import com.smartContactManager.entities.User;

public interface ContactsRepository extends JpaRepository<Contact, Integer> {
	
	@Query("from Contact as c where c.user.id =:userId")
	public List<Contact> findContactsByUser(@Param("userId") int userId);
	
	
	@Query("from Contact as c where c.cId =:cId")
	public Contact findById(@Param("cId") int cId);
	
	
	public List<Contact> findByNameContainingAndUser(String name, User user);

}
