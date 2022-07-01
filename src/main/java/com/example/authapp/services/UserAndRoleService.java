package com.example.authapp.services;

import com.example.authapp.exceptions.AlreadyExistsException;
import com.example.authapp.models.Role;
import com.example.authapp.models.User;
import com.example.authapp.repository.RoleRepository;
import com.example.authapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        for (Role role: user.getRoles()){
            roleRepo.findByName(role.getName()).orElseThrow(() -> new NoSuchElementException("Role "+role.getName()+" does not exist."));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User changeDesc(User user){
        User user1 = findUserById(user.getId());
        user1.setDescription(user.getDescription());
        return userRepo.save(user1);
    }
    public User findByUsername(String username){
        return userRepo.findByUsername(username).orElseThrow(() -> new NoSuchElementException("There is no user in database with username: "+username+"!"));
    }

    public User findUserById(Long id){
        return userRepo.findById(id).orElseThrow(() -> new NoSuchElementException("There is no user in database with id: "+id+"!"));
    }

    public Role findRoleByName(String name){
        return roleRepo.findByName(name).orElseThrow(() -> new NoSuchElementException("There is no role in database with name: "+name+"!"));
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public void addRoleToUser(String role, String username){
        User user = findByUsername(username);
        Role role1 = findRoleByName(role);
        user.getRoles().add(role1);
        userRepo.save(user);
    }

    public Role saveRole(Role role){
        return roleRepo.save(role);
    }

    public List<Role> findAllRoles(){
        return roleRepo.findAll();
    }
}
