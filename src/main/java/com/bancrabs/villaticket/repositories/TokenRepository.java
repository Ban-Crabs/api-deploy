package com.bancrabs.villaticket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Token;
import com.bancrabs.villaticket.models.entities.User;

public interface TokenRepository extends JpaRepository<Token, UUID>{ 
	
	List<Token> findByUserAndActive(User user, Boolean active);

}