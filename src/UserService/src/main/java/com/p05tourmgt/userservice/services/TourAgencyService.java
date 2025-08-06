package com.p05tourmgt.userservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.p05tourmgt.userservice.entities.Role;
import com.p05tourmgt.userservice.entities.TourAgency;
import com.p05tourmgt.userservice.entities.User;
import com.p05tourmgt.userservice.repositories.RoleRepository;
import com.p05tourmgt.userservice.repositories.TourAgencyRepository;
import com.p05tourmgt.userservice.repositories.UserRepository;

@Service
public class TourAgencyService {

    @Autowired
    private TourAgencyRepository tourAgencyRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    // 1. Get a TourAgency by its associated User
    public TourAgency getTourAgency(User user) {
        return tourAgencyRepository.findByUid(user);
    }
    
    // 2. Register a new TourAgency (Create operation)
    public TourAgency registerAgency(TourAgency agency) {
        // Ensure the 'tour_agency' role exists in your database and is assigned
        Role agencyRole = roleRepository.findByRname("tour_agency");
        if (agencyRole == null) {
            // Handle case where role doesn't exist
            agencyRole = new Role(0, "tour_agency");
            roleRepository.save(agencyRole);
        }

        User user = agency.getUid();
        user.setRid(agencyRole); // Set the role for the user

        User savedUser = userRepository.save(user); // Save the user first
        agency.setUid(savedUser); // Link the saved user to the agency
        return tourAgencyRepository.save(agency); // Save the agency
    }

    // 3. Login for an agency
    public TourAgency loginAgency(String username, String password) {
        User user = userRepository.findByUname(username);
        if (user != null && user.getPassword().equals(password) && user.getRid().getRname().equals("tour_agency")) {
            return tourAgencyRepository.findByUid(user);
        }
        return null;
    }
    
    // 4. Get all tour agencies (Read operation)
    public List<TourAgency> getAllAgencies() {
        return tourAgencyRepository.findAll();
    }

    // 5. Get an agency by its ID (Read operation)
    public Optional<TourAgency> getAgencyById(Integer id) {
        return tourAgencyRepository.findById(id);
    }
    
    // 6. Save or update a tour agency (Update operation)
    public TourAgency saveOrUpdateAgency(TourAgency agency) {
        return tourAgencyRepository.save(agency);
    }

    // 7. Delete an agency by its ID (Delete operation)
    public void deleteAgency(Integer id) {
        tourAgencyRepository.deleteById(id);
    }
}