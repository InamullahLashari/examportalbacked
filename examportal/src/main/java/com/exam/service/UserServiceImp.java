package com.exam.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repsoitory.RoleRepository;
import com.exam.repsoitory.UserRepository;

@Service
public class UserServiceImp implements UserService {

    // ============================
    // Dependencies Injection
    // ============================

    @SuppressWarnings("unused") // Suppresses unused warning if not used yet
    @Autowired
    private RoleRepository roleRepository;

    @SuppressWarnings("unused")
    @Autowired
    private UserRepository userRepository;

    // ============================
    // Create User Method
    // ============================

    @Override
    public User createUser(User user, Set<UserRole> userRoles) {

            // Save roles and associate them with the user
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }

            // Associate roles with user
            user.getUserRoles().addAll(userRoles);

            // Save the user
           User local = this.userRepository.save(user);
        

        return local;
    }
/////////////////////////////get user by Username//////////////////////////////
	@Override
	public User getUser(String username) {
		
		return userRepository.findByUsername(username);
	}
/////////////////////////////get user by Id//////////////////////////////
	@Override
	public Optional<User> getById(long id) {
		
		return userRepository.findById(id);
	}
/////////////////////////////Detelet user by Id//////////////////////////////
	@Override
	public boolean deleteUser(long id) {
		
		if(userRepository.existsById(id)) {
			
			userRepository.deleteById(id);
			return true;
		}
		else {
		return false;
	}
	

	}
	
/////////////////////////////update user by Id//////////////////////////////
	@Override
	public User updateUserDetail(long id, User newUser) {
		User userData = userRepository.findById(id).orElse(null) ;
		if(userData!=null) {
			
			return userRepository.save(newUser);
		}
		
		else {
			throw new RuntimeException("user not found with id"+ id);
		}
	}
	
	
/////////////////////////////return true or false for verification//////////////////////////////
	@Override
	public boolean existsByEmail(String email) {
		
		return userRepository.existsByEmail(email);
	}
	@Override
	public boolean existsByPhone(String phone) {
		return userRepository.existsByPhone(phone);
		
	}	

}
