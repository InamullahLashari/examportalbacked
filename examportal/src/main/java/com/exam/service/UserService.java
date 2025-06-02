package com.exam.service;

import java.util.Optional;
import java.util.Set;

import com.exam.model.User;
import com.exam.model.UserRole;

public interface UserService {
	
	public User createUser(User user,Set<UserRole> userRoles);
	
	public User getUser(String username);
	
	public Optional<User> getById(long id);
	
	
	public boolean deleteUser(long id);
	
	public User updateUserDetail(long id ,User newUser);
	
	public boolean existsByEmail(String email);

	public boolean existsByPhone(String phone);

}
