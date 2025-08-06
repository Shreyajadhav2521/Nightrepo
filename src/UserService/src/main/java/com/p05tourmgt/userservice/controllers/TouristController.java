package com.p05tourmgt.userservice.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.p05tourmgt.userservice.entities.Tourist;
import com.p05tourmgt.userservice.entities.User;
import com.p05tourmgt.userservice.services.TouristService;
import com.p05tourmgt.userservice.services.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tourist")
public class TouristController {
    
    @Autowired
    private TouristService tservice;

    @Autowired
    private UserService uservice;

    // Retrieves a tourist by their associated User ID. - GET
    // http://localhost:8080/tourist/getByUserId?uid={uid}
    @GetMapping("/getByUserId")
    public ResponseEntity<Tourist> getTouristByUserId(@RequestParam("uid") int uid) {
        User u = uservice.getById(uid);
        if (u != null) {
            Tourist tourist = tservice.getTourist(u);
            if (tourist != null) {
                return ResponseEntity.ok(tourist);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Registers a new tourist account.- POST
    //http://localhost:8080/tourist/register
    @PostMapping("/register")
    public Tourist registerTourist(@RequestBody Tourist tourist) {
        return tservice.registerTourist(tourist);
    }

    // Login for a tourist - POST
    //http://localhost:8080/tourist/login
    @PostMapping("/login")
    public ResponseEntity<?> loginTourist(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Tourist tourist = tservice.loginTourist(username, password);
        if (tourist != null) {
            return ResponseEntity.ok(tourist);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password, or not a tourist");
        }
    }

    //Retrieves a list of all tourists.- GET
    //http://localhost:8080/tourist
    @GetMapping
    public List<Tourist> getAllTourists() {
        return tservice.getAllTourists();
    }

    //Retrieves a specific tourist by their unique ID.- GET
    // http://localhost:8080/tourist/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Tourist> getTouristById(@PathVariable int id) {
        Optional<Tourist> tourist = tservice.getTouristById(id);
        return tourist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Updates an existing tourist by their ID.- PUT
    // http://localhost:8080/tourist/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Tourist> updateTourist(@PathVariable int id, @RequestBody Tourist updatedTourist) {
        return tservice.getTouristById(id)
            .map(existingTourist -> {
                existingTourist.setFname(updatedTourist.getFname());
                existingTourist.setLname(updatedTourist.getLname());
                existingTourist.setAddress(updatedTourist.getAddress());
                existingTourist.setDob(updatedTourist.getDob());
                // If you intend to update user details via tourist update, handle it here
                // For example: existingTourist.getUid().setEmail(updatedTourist.getUid().getEmail());
                return ResponseEntity.ok(tservice.saveOrUpdateTourist(existingTourist));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Deletes a tourist by their ID.- DELETE
    // http://localhost:8080/tourist/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourist(@PathVariable int id) {
        tservice.deleteTourist(id);
        return ResponseEntity.noContent().build();
    }
}