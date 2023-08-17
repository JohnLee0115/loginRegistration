package com.john.loginregistration.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.john.loginregistration.models.LoginUser;
import com.john.loginregistration.models.User;
import com.john.loginregistration.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User registerUser(User user) {
        // TO-DO: Additional validations!
    	String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    	user.setPassword(hashed);
        return userRepository.save(user);
    }
    
    public User getUser(String email) {
    	Optional <User> potentialUser = userRepository.findByEmail(email);
    	return potentialUser.isPresent() ? potentialUser.get() : null;
    	
    }
    
    public User getUser(Long id) {
    	Optional <User> potentialUser = userRepository.findById(id);
    	return potentialUser.isPresent() ? potentialUser.get() : null;
    	
    }
    
    public User login(LoginUser loginUser, BindingResult result) {
        
    	if(result.hasErrors()) {
    		return null;
    	}
    	User existingUser = this.getUser(loginUser.getEmail());
    	if(existingUser == null) {
    		result.rejectValue("email", "Unique", "Invalid credentials");
    		return null;
    	}
    	if(!BCrypt.checkpw(loginUser.getPassword(), existingUser.getPassword())) {
    		result.rejectValue("email", "Unique", "Invalid credentials");
    		return null;
    	}
        return existingUser;
    }
}
