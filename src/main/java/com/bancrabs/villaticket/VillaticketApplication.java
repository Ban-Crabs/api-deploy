package com.bancrabs.villaticket;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bancrabs.villaticket.models.dtos.save.RegisterUserDTO;
import com.bancrabs.villaticket.models.dtos.save.SavePrivilegeDTO;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.services.UserPrivilegeService;
import com.bancrabs.villaticket.services.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class VillaticketApplication {

	@Autowired
	private UserService userService;

	@Autowired
	private UserPrivilegeService userPrivilegeService;

	public static void main(String[] args) {
		SpringApplication.run(VillaticketApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder(5, new SecureRandom());
	}

	@PostConstruct
	public void init(){
		try{
			User user = userService.findById("sysadmin");
			if(user == null){
				userService.register(new RegisterUserDTO("sysadmin", "00389819@uca.edu.sv"));
				user = userService.findById("sysadmin");
				user.setPassword(passwordEncoder().encode("VanillaTicket"));
				user.setActive(true);
				userService.update(user);
				userPrivilegeService.save(new SavePrivilegeDTO("admin", user.getId()));
				userPrivilegeService.save(new SavePrivilegeDTO("employee", user.getId()));
				userPrivilegeService.save(new SavePrivilegeDTO("user", user.getId()));
				userPrivilegeService.save(new SavePrivilegeDTO("analyst", user.getId()));
			}
			System.out.println("Initialized");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
