package com.example.authapp.models.helper;

import java.util.List;

public class SignupRequest {

  private String username;


  private String email;

  private List<String> roles;

  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<String> getRoles() {
    return this.roles;
  }

  public void setRoles(List<String> role) {
    this.roles = role;
  }
}