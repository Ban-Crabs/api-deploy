package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.LoginDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterUserDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveUserDTO;
import com.bancrabs.villaticket.models.entities.QR;
import com.bancrabs.villaticket.models.entities.Token;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.repositories.TokenRepository;
import com.bancrabs.villaticket.repositories.UserRepository;
import com.bancrabs.villaticket.services.QRService;
import com.bancrabs.villaticket.services.UserService;
import com.bancrabs.villaticket.utils.JWTTools;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private QRService qrService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String register(RegisterUserDTO data) throws Exception {
        try{
            User check = userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail());
            if(check == null){
                check = userRepository.save(new User(data.getUsername(), data.getEmail(), null));
                System.out.println(check);
                QR qr = qrService.save((passwordEncoder.encode(check.getId().toString() + Long.toString(System.currentTimeMillis()))));
                System.out.println("Registered code: "+qr.getCode());
                return qr.getCode();
            }
            else{
                throw new Exception("User already exists");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean deleteById(String id) throws Exception{
        try{
            User toDelete = userRepository.findByUsernameOrEmail(id, id);
            if(toDelete == null){
                throw new Exception("User not found");
            }
            userRepository.deleteById(toDelete.getId());
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findById(String id) {
        return userRepository.findByUsernameOrEmail(id, id);
    }

    @Override
    public Page<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllByOrderByUsernameAsc(pageable);
    }

    @Override
    public Boolean login(LoginDTO data) throws Exception {
        try{
            User check = userRepository.findByUsernameOrEmail(data.getId(), data.getId());
            if(check == null){
                throw new Exception("User not found");
            }
            else{
                if(check.getPassword() == null || !check.getActive()) throw new Exception("Unauthorized");
                if(passwordEncoder.matches(data.getPassword(), check.getPassword())){
                    return true;
                }
                else{
                    throw new Exception("Wrong password");
                }
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Boolean update(SaveUserDTO data, String id, String oldPassword) throws Exception {
        try{
            User toUpdate = userRepository.findByUsernameOrEmail(id, id);
            if(toUpdate == null){
                throw new Exception("User not found");
            }
            if(toUpdate.getPassword() != null && !passwordEncoder.matches(oldPassword, toUpdate.getPassword())){ 
                throw new Exception("Wrong password");
            }
            else{
                if(!verifyIdentity(id)) throw new Exception("Unauthorized");
                toUpdate.setUsername(data.getUsername());
                toUpdate.setEmail(data.getEmail());
                toUpdate.setPassword(passwordEncoder.encode(data.getPassword()));
                userRepository.save(toUpdate);
                return true;
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
	@Transactional(rollbackOn = Exception.class)
	public Token registerToken(User user) throws Exception {
		cleanTokens(user);
		
		String tokenString = jwtTools.generateToken(user);
		Token token = new Token(tokenString, user);
		
		tokenRepository.save(token);
		
		return token;
	}

    @Override
	public Boolean isTokenValid(User user, String token) {
		try {
			cleanTokens(user);
			List<Token> tokens = tokenRepository.findByUserAndActive(user, true);
			
			tokens.stream()
				.filter(tk -> tk.getContent().equals(token))
				.findAny()
				.orElseThrow(() -> new Exception());
			
			return true;
		} catch (Exception e) {
			return false;
		}		
	}

    @Override
	@Transactional(rollbackOn = Exception.class)
	public void cleanTokens(User user) throws Exception {
		List<Token> tokens = tokenRepository.findByUserAndActive(user, true);
		
		tokens.forEach(token -> {
			if(!jwtTools.verifyToken(token.getContent())) {
				token.setActive(false);
				tokenRepository.save(token);
			}
		});
		
	}

    @Override
    public User findUserAuthenticated() {
        String username = SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getName();
		
		return userRepository.findByUsernameOrEmail(username, username);
    }

    @Override
    public Boolean verifyIdentity(String id) throws Exception {
        try {
            User user = userRepository.findByUsernameOrEmail(id, id);
            if(user == null) throw new Exception("User not found");
            User check = findUserAuthenticated();
            if(!check.equals(user)) return false;
            return true;
        } catch (Exception e) {
            throw e;   
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean logoutActive() throws Exception {
        try {
            User toLogout = findUserAuthenticated();
            if(toLogout == null) throw new Exception("User not found");
            List<Token> tokens = tokenRepository.findByUserAndActive(toLogout, true);
            tokens.forEach(token -> {
                token.setActive(false);
                tokenRepository.save(token);
        });
        return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Boolean activate(String code, String username) throws Exception {
        try{
            QR check = qrService.findByCode(code);
            if(check == null) throw new Exception("QR not found");
            if(System.currentTimeMillis() - check.getCreationTime().getTime() > 86400000 || System.currentTimeMillis() - check.getCreationTime().getTime() <= 0 ) throw new Exception("QR expired");
            User user = findById(username);
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean update(User user) throws Exception {
        try{
            userRepository.save(user);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public String register(SaveUserDTO data) throws Exception {
        try{
            User check = userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail());
            if(check == null){
                check = userRepository.save(new User(data.getUsername(), data.getEmail(), passwordEncoder.encode(data.getPassword())));
                System.out.println(check);
                QR qr = qrService.save((passwordEncoder.encode(check.getId().toString() + Long.toString(System.currentTimeMillis()))));
                return qr.getCode();
            }
            else{
                throw new Exception("User already exists");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public String generateActivationCode(User user) throws Exception {
        try{
            QR qr = qrService.save((passwordEncoder.encode(user.getId().toString() + Long.toString(System.currentTimeMillis()))));
            return qr.getCode();
        }
        catch(Exception e){
            throw e;
        }
    }

    
}
