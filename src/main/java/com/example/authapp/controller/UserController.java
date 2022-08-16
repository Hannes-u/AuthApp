package com.example.authapp.controller;

import com.example.authapp.controller.service.UserService;
import com.example.authapp.exception.PasswordInvalidException;
import com.example.authapp.models.User;
import com.example.authapp.models.helper.ChangePasswordRequest;
import com.example.authapp.models.helper.ChangePasswordSuccessfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;
  @Autowired
  AuthenticationManager authenticationManager;


  @GetMapping("/all")
  public List<User> allUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/getMyInformation")
  public User getInformationOfUser() {
    //Über den Securitycontext der Aktuellen Anfrage kann der Nutzername herausgefunden werden
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username;
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }

    return userService.findByUsername(username);
  }

  @PutMapping("/changePassword")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){

    //Über den Securitycontext der Aktuellen Anfrage kann der Nutzername herausgefunden werden
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username;
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, changePasswordRequest.getOldPassword()));

    try {
      userService.changePassword(username,changePasswordRequest.getNewPassword());
      return ResponseEntity.ok(new ChangePasswordSuccessfulResponse("Password successfully changed!"));
    }catch (PasswordInvalidException passwordInvalidException){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(passwordInvalidException.getMessage());
    }

  }

}
