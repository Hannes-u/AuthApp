package com.example.authapp.controller.service;

import com.example.authapp.exception.AlreadyExistsException;
import com.example.authapp.exception.PasswordInvalidException;
import com.example.authapp.models.User;
import com.example.authapp.repository.UserRepository;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    //Methode um neue Nutzer in der Datenbank abzulegen
    public User saveUser(User user){
        if (userRepo.findByUsername(user.getUsername()).isPresent()){
            throw new AlreadyExistsException("User with Username "+user.getUsername()+" already exists.");
        }
        if (userRepo.findByEmail(user.getEmail()).isPresent()){
            throw new AlreadyExistsException("User with Email "+user.getEmail()+" already exists.");
        }
        isPasswordValid(user.getUsername(), user.getPassword());
        // Hier wird das Passwort gehashed
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username).orElseThrow(() -> new NoSuchElementException("There is no user in database with username: "+username+"!"));
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public void changePassword(String username, String newPassword){
        isPasswordValid(username,newPassword);
        User user = findByUsername(username);
        // Hier wird das Passwort gehashed
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    // Methode um Passwörter zu validieren
    private void isPasswordValid(String username,String password){
        PasswordValidator passwordValidator =
                new PasswordValidator(
                        new LengthRule(10,125),
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new CharacterRule(EnglishCharacterData.Special,1),
                        new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, true),
                        new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, true),
                        new UsernameRule(MatchBehavior.Contains)
                );
        RuleResult result = passwordValidator.validate(new PasswordData(username,password));
        if (!result.isValid()) {
            List<String> messages = passwordValidator.getMessages(result);
            String messageTemplate = String.join("\n", messages);
            throw new PasswordInvalidException(messageTemplate);
        }
    }
}
