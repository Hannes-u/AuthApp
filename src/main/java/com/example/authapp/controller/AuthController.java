package com.example.authapp.controller;

import com.example.authapp.controller.service.UserAndRoleService;
import com.example.authapp.exception.AlreadyExistsException;
import com.example.authapp.exception.PasswordInvalidException;
import com.example.authapp.models.Role;
import com.example.authapp.models.User;
import com.example.authapp.models.helper.ChangePasswordRequest;
import com.example.authapp.models.helper.JwtResponse;
import com.example.authapp.models.helper.LoginRequest;
import com.example.authapp.models.helper.SignupRequest;
import com.example.authapp.security.jwt.JwtUtils;
import com.example.authapp.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());


    return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
  }

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
    }catch (PasswordInvalidException passwordInvalidException){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(passwordInvalidException.getMessage());
    }
  }

}
