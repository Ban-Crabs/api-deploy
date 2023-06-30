package com.bancrabs.villaticket.utils;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderUtil {
    
    @Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder(5, new SecureRandom());
	}
}
