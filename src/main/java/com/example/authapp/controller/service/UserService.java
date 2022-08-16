package com.example.authapp.controller.service;

import com.example.authapp.exception.AlreadyExistsException;
import com.example.authapp.models.User;
import com.example.authapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    //Methode um neue Nutzer in der Datenbank abzulegen
    public User saveUser(User user){
        if (userRepo.findByUsername(user.getUsername()).isPresent()){
            throw new AlreadyExistsException("User with Username "+user.getUsername()+" already exists.");
        }
        if (userRepo.existsByEmail(user.getEmail())){
            throw new AlreadyExistsException("User with Email "+user.getEmail()+" already exists.");
        }
        return userRepo.save(user);
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }
    public User findByUsername(String username){
        return userRepo.findByUsername(username).orElseThrow(() -> new NoSuchElementException("There is no user in database with username: "+username+"!"));
    }
}
