package com.exam.security.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.exam.model.User;



public class UserDetailsImpl implements UserDetails {
	
	
	
	
	 private static final long serialVersionUID = 1L;
	    
	    // Core user data fields (all final for immutability)
	    private final Long id;          // Database ID of the user
	    private final String email;     // User's email (acts as username)
	    private final String password;  // Encrypted password
	    private final Collection<? extends GrantedAuthority> authorities;  // User's permissions/roles

	    /**
	     * Main constructor - Stores the essential user security information.
	     * 
	     * @param id          User's unique database identifier
	     * @param email       User's email address (used as login username)
	     * @param password    Encrypted password for authentication
	     * @param authorities Collection of granted permissions/roles
	     */
	    public UserDetailsImpl(Long id, String email, String password,
	                         Collection<? extends GrantedAuthority> authorities) {
	        this.id = id;
	        this.email = email;
	        this.password = password;
	        this.authorities = authorities;
	    }

	    /**this is convert the data into security form
	     * Factory method to convert a User entity to UserDetailsImpl.
	     * 
	     * @param user The User entity from your application
	     * @return UserDetailsImpl instance ready for Spring Security
	     */
	    public static UserDetailsImpl build(User user) {
	        List<GrantedAuthority> authorities = user.getUserRoles().stream()
	                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()))
	                .collect(Collectors.toList());

	        return new UserDetailsImpl(
	                user.getId(),          // User ID
	                user.getEmail(),    // Username (email or name)
	                user.getPassword(),    // Encrypted password
	                authorities            // Roles as authorities
	        );
	    }


	    // ========== UserDetails Interface Required Methods ==========

	    /**
	     * Returns the authorities (permissions/roles) granted to the user.
	     */
	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return authorities;
	    }

	    /**
	     * Returns the encrypted password used for authentication.
	     */
	    @Override
	    public String getPassword() {
	        return password;
	    }

	    /**
	     * Returns the username (email in this implementation) used to authenticate.
	     */
	    @Override
	    public String getUsername() {
	        return email;
	    }

	    // ========== Account Status Methods ==========
	    // (All return true by default - override with real logic if needed)

	    /** Indicates whether the user's account has expired */
	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    /** Indicates whether the user is locked or unlocked */
	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    /** Indicates whether the user's credentials (password) are expired */
	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    /** Indicates whether the user is enabled or disabled */
	    @Override
	    public boolean isEnabled() {
	        return true;
	    }

	    // ========== Additional Convenience Methods ==========

	    /**
	     * Returns the user's database ID (not part of UserDetails interface).
	     * Useful for application business logic.
	     */
	    public Long getId() {
	        return id;
	    }
	}
	
	



