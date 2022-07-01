package com.example.authapp.controllers;

import com.example.authapp.models.User;
import com.example.authapp.services.UserAndRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserAndRoleService userAndRoleService;

  @GetMapping("")
  @PreAuthorize("hasRole('ADMIN')")
  public List<User> getAllUsers() {
    return userAndRoleService.getAllUsers();
  }

  @PutMapping("/updateUserData")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public User changeDescOfUser(@RequestBody User user) {
    return userAndRoleService.changeDesc(user);
  }

  @PutMapping("/lol")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public String lol() {
    return "lol";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
