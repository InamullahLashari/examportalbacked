package com.exam.repsoitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exam.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	
	public User findByUsername(String username);
	public boolean existsById(long id);
	
	
	//this only give you email
	public boolean existsByEmail(String email);
	//this is only give you phone
	public boolean existsByPhone(String phone);
	Optional<User> findByEmail(String email);
	
	
	

}
