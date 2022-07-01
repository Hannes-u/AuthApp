package com.example.authapp;

import com.example.authapp.models.Role;
import com.example.authapp.models.User;
import com.example.authapp.services.UserAndRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@SpringBootApplication
public class AuthAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthAppApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserAndRoleService userAndRoleService) {
		return args -> {
			try {
				userAndRoleService.findByUsername("admin");
			}catch (NoSuchElementException e){
				userAndRoleService.saveRole(new Role("USER"));
				userAndRoleService.saveRole(new Role("ADMIN"));
				userAndRoleService.saveUser(new User("admin","admin@mail.de","Hes the admin","1234",new ArrayList<>()));
				userAndRoleService.addRoleToUser("Role_Admin","admin");
			}
		};
	}

}
