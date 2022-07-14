package com.example.authapp.controller;

import com.example.authapp.controller.service.UserAndRoleService;
import com.example.authapp.exception.AlreadyExistsException;
import com.example.authapp.models.Role;
import com.example.authapp.models.User;

import com.example.authapp.models.helper.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  UserAndRoleService userAndRoleService;

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
    try {
      List<Role> roles = new ArrayList<>();
      signupRequest.getRoles().forEach(role -> roles.add(new Role(role)));
      User user = new User(signupRequest.getUsername(),signupRequest.getEmail(),signupRequest.getPassword(),roles);
      User savedUser = userAndRoleService.saveUser(user);
      return ResponseEntity.ok(savedUser);
    }catch (AlreadyExistsException alreadyExistsException){
      return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(alreadyExistsException.getMessage());
    }catch (NoSuchElementException noSuchElementException){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noSuchElementException.getMessage());
    }
  }
}
