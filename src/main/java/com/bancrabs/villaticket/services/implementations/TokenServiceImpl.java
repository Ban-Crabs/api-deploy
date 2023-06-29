package com.bancrabs.villaticket.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.entities.Token;
import com.bancrabs.villaticket.repositories.TokenRepository;
import com.bancrabs.villaticket.services.TokenService;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void cleanInactiveTokens() {
        List<Token> tokens = tokenRepository.findByActiveIsFalse();
        tokenRepository.deleteAll(tokens);
    }
    
}
