package com.example.authapp.controller;

import com.example.authapp.controller.service.UserAndRoleService;
import com.example.authapp.exception.PasswordInvalidException;
import com.example.authapp.models.User;
import com.example.authapp.models.helper.ChangePasswordRequest;
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
  UserAndRoleService userAndRoleService;
  @Autowired
  AuthenticationManager authenticationManager;


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

  @PutMapping("/changePassword")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
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
      userAndRoleService.changePassword(username,changePasswordRequest.getNewPassword());
      return ResponseEntity.ok("Password successfully changed!");
    }catch (PasswordInvalidException passwordInvalidException){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,passwordInvalidException.getMessage());
    }

  }

}
