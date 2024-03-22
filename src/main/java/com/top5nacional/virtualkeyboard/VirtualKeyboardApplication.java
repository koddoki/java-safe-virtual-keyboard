package com.top5nacional.virtualkeyboard;

import com.top5nacional.virtualkeyboard.model.Role;
import com.top5nacional.virtualkeyboard.model.User;
import com.top5nacional.virtualkeyboard.repository.RoleRepository;
import com.top5nacional.virtualkeyboard.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

// 0B2G2Np3K8Os3uz0
@SpringBootApplication
public class VirtualKeyboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualKeyboardApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isEmpty()) {
				Role adminRole = roleRepository.save(new Role("ADMIN"));
				Set<Role> roles = new HashSet<>();
				roles.add(adminRole);
				User admin = new User(1, "admin", passwordEncode.encode("136"), roles);
				userRepository.save(admin);
			}

			if(roleRepository.findByAuthority("SESSION").isEmpty()) {
				Role sessionRole = roleRepository.save(new Role("SESSION"));
				Set<Role> roles = new HashSet<>();
				roles.add(sessionRole);
				User sessionManager = new User(2, "session", passwordEncode.encode("session"), roles);
				userRepository.save(sessionManager);
			}

			if(roleRepository.findByAuthority("USER").isEmpty()) {
				Role userRole = roleRepository.save(new Role("USER"));
				Set<Role> roles = new HashSet<>();
				roles.add(userRole);
				User martin = new User(3, "Martin", passwordEncode.encode("71483"), roles);
				userRepository.save(martin);
			}
		};
	}
}
