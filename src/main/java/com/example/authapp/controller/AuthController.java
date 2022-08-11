package com.example.authapp.controller;

import com.example.authapp.controller.service.UserService;
import com.example.authapp.exception.AlreadyExistsException;
import com.example.authapp.exception.PasswordInvalidException;
import com.example.authapp.models.User;
import com.example.authapp.models.helper.JwtResponse;
import com.example.authapp.models.helper.LoginRequest;
import com.example.authapp.models.helper.SignupRequest;
import com.example.authapp.security.jwt.JwtUtils;
import com.example.authapp.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  UserService userService;
  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    //Generate a random string that will constitute the fingerprint for this user
    byte[] randomFgp = new byte[50];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(randomFgp);
    String userFingerprint = DatatypeConverter.printHexBinary(randomFgp);
    Cookie cookie = new Cookie("__Secure-Fgp",userFingerprint);
    cookie.setDomain("localhost");
    cookie.setPath("/");
    cookie.setMaxAge(jwtUtils.getJwtExpirations()/1000);
    cookie.setHttpOnly(true);

    response.addCookie(cookie);

    String jwt = "";
    try {
      jwt = jwtUtils.generateJwtToken(authentication,userFingerprint);
    }catch (Exception exception){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
    }


    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());


    return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
    try {
      User user = new User(signupRequest.getUsername(),signupRequest.getEmail(),signupRequest.getPassword());
      User savedUser = userService.saveUser(user);
      return ResponseEntity.ok(savedUser);
    }catch (AlreadyExistsException alreadyExistsException){
      throw new ResponseStatusException(HttpStatus.CONFLICT,alreadyExistsException.getMessage());
    }catch (NoSuchElementException noSuchElementException){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,noSuchElementException.getMessage());
    }catch (PasswordInvalidException passwordInvalidException){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,passwordInvalidException.getMessage());
    }
  }

}
