package com.example.authapp.controller.service;

import com.example.authapp.exception.AlreadyExistsException;
import com.example.authapp.exception.PasswordInvalidException;
import com.example.authapp.models.Role;
import com.example.authapp.models.User;
import com.example.authapp.repository.RoleRepository;
import com.example.authapp.repository.UserRepository;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserAndRoleService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public UserAndRoleService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user){
        if (userRepo.findByUsername(user.getUsername()).isPresent()){
            throw new AlreadyExistsException("User with Username "+user.getUsername()+" already exists.");
        }
        isPasswordValid(user.getPassword());
        List<Role> rolesFromDatabase = new ArrayList<>();
        Role roleFromDatabase = findRoleByName("Role_User");
        rolesFromDatabase.add(roleFromDatabase);
        user.setRoles(rolesFromDatabase);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public void addRoleToUser(String role, String username){
        User user = findByUsername(username);
        Role role1 = findRoleByName(role);
        user.getRoles().add(role1);
        userRepo.save(user);
    }

    public Role findRoleByName(String role){
        return roleRepo.findByName(role).orElseThrow(() -> new NoSuchElementException("Role "+role+" does not exist."));
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username).orElseThrow(() -> new NoSuchElementException("There is no user in database with username: "+username+"!"));
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public Role saveRole(Role role){
        return roleRepo.save(role);
    }


    public void changePassword(String username, String newPassword){
        isPasswordValid(newPassword);
        User user = findByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    private void isPasswordValid(String password){
        PasswordValidator passwordValidator =
                new PasswordValidator(
                        new LengthRule(10,125),
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new CharacterRule(EnglishCharacterData.Special,1),
                        new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, true),
                        new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, true),
                        new IllegalSequenceRule(GermanSequenceData.Alphabetical, 4, true)
                );
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (!result.isValid()) {
            List<String> messages = passwordValidator.getMessages(result);
            String messageTemplate = String.join("\n", messages);
            throw new PasswordInvalidException(messageTemplate);
        }
    }
}
