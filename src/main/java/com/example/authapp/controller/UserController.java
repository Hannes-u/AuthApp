package com.example.authapp.controller;

import com.example.authapp.controller.service.UserAndRoleService;
import com.example.authapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

  @PostMapping("/getAndMaybeCreateMyInformation")
  public Map<String, Object> getUserDetails(JwtAuthenticationToken authentication) {
    return authentication.getTokenAttributes();
  }

}
