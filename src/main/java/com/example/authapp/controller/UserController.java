package com.example.authapp.controller;

import com.example.authapp.controller.service.UserAndRoleService;
import com.example.authapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserAndRoleService userAndRoleService;


  @GetMapping("/all")
  public List<User> allUsers() {
    return userAndRoleService.getAllUsers();
  }

  @GetMapping("/getMyInformation")
  public User getInformationOfUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String username;
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }

    return userAndRoleService.findByUsername(username);
  }

}
