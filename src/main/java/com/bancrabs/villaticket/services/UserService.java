package com.bancrabs.villaticket.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.bancrabs.villaticket.models.dtos.LoginDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterUserDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveUserDTO;
import com.bancrabs.villaticket.models.entities.Token;
import com.bancrabs.villaticket.models.entities.User;

public interface UserService {
    Boolean register(RegisterUserDTO data) throws Exception;
    Boolean login(LoginDTO data) throws Exception;
    Boolean update(SaveUserDTO data, String id) throws Exception;
    Boolean deleteById(String id) throws Exception;
    User findById(UUID id);
    User findById(String id);
    Page<User> findAll(int page, int size);
    Boolean verifyIdentity(String id) throws Exception;

    Token registerToken(User user) throws Exception;
	Boolean isTokenValid(User user, String token);
	void cleanTokens(User user) throws Exception;
    User findUserAuthenticated();
}