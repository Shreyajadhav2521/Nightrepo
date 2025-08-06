package com.p05tourmgt.userservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.p05tourmgt.userservice.entities.User;
import com.p05tourmgt.userservice.repositories.UserRepository;

public class UserService {
	
	@Autowired
	UserRepository urepo;
	
	public List<User> getAll(){
		return urepo.findAll();
	}
	
	public User getLogin(String uname,String password)
	{
		User u;
		Optional<User> ol = urepo.getLogin(uname, password);
		try
		{
			u=ol.get();
		}
		catch(Exception e)
		{
			u=null;
		}
		return u;
	}

	
	public User getById(int uid)
	{
		return urepo.findById(uid).get();
	}


	public User getOne(int id) {
		Optional<User>soptional=urepo.findById(id);
		return soptional.get();
	}
	
	public User save(User u) {
		urepo.save(u);
		return u;
	}
	public User updateUser(int id, User updatedUser) {
        User existingUser = urepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUname(updatedUser.getUname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        
        // Add other fields as needed

        return urepo.save(existingUser);
    }
	
	public void deleteUser(int id) {
		urepo.deleteById(id);
    }
	
	public User findByEmail(String email) {
        return urepo.findByEmail(email);
    }
    
    public User findByUname(String uname) {
        return urepo.findByUname(uname);
    }
    
 // Create or update a user
    public User saveUser(User user) {
        return urepo.save(user);
    }


}
