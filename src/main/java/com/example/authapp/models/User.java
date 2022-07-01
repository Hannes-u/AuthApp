package com.example.authapp.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String email;

  private String description;

  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> roles = new ArrayList<>();

  public User(String username, String email, String description, String password, List<Role> roles) {
    this.username = username;
    this.email = email;
    this.description = description;
    this.password = password;
    this.roles = roles;
  }
}
