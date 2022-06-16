package com.example.authapp.Entity;

import lombok.*;
import org.hibernate.annotations.ManyToAny;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long iden;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
}
