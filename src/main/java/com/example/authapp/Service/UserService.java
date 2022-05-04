package com.example.authapp.Service;

import com.example.authapp.Entity.Role;
import com.example.authapp.Entity.User;
import com.example.authapp.Repo.RoleRepo;
import com.example.authapp.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public User saveUser(User user){
        return userRepo.save(user);
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public Role saveRole(Role role){
        return roleRepo.save(role);
    }

    public List<Role> findAllRoles(){
        return roleRepo.findAll();
    }

    public void addRoleToUser(String username,String role){
        User byUsername = userRepo.findByUsername(username);
        Role byName = roleRepo.findByName(role);
        byUsername.getRoles().add(byName);
    }


}
