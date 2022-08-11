package com.example.authapp.controller;

import com.example.authapp.controller.service.UserService;
import com.example.authapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;


  @GetMapping("/all")
  public List<User> allUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/getAndMaybeCreateMyInformation")
  public User getUserDetails(JwtAuthenticationToken authentication) {
    String username = authentication.getTokenAttributes().get("sub").toString();

    try{
      return userService.findByUsername(username);
    }catch (NoSuchElementException noSuchElementException){
      userService.saveUser(new User(username,username));
      return userService.findByUsername(username);
    }

  }

}
