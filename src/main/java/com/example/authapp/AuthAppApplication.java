package com.example.authapp;

import com.example.authapp.controller.service.UserAndRoleService;
import com.example.authapp.models.Role;
import com.example.authapp.models.User;
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

	//Anlegen von einem Admin User beim erstmaligen Start der Applikation.
	@Bean
	CommandLineRunner runner(UserAndRoleService userAndRoleService) {
		return args -> {
			try {
				userAndRoleService.findByUsername("admin");
			}catch (NoSuchElementException e){
				userAndRoleService.saveRole(new Role("Role_User"));
				userAndRoleService.saveRole(new Role("Role_Admin"));
				userAndRoleService.saveUser(new User("admin","admin@mail.de","Ad@1999@Password",new ArrayList<>()));
				userAndRoleService.addRoleToUser("Role_Admin","admin");
			}
		};
	}

}
