package com.bancrabs.villaticket.configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.io.PrintWriter;

import com.bancrabs.villaticket.models.dtos.response.QRResponseDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterUserDTO;
import com.bancrabs.villaticket.models.entities.OauthUser;
import com.bancrabs.villaticket.models.entities.QR;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.services.QRService;
import com.bancrabs.villaticket.services.UserService;
import com.bancrabs.villaticket.services.implementations.OAuthUserService;
import com.bancrabs.villaticket.utils.JWTTokenFilter;
import com.bancrabs.villaticket.utils.JWTTools;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration {
    @Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTTools jwtTools;

	@Autowired
	private QRService qrService;

	@Autowired
	private OAuthUserService oauthUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private JWTTokenFilter filter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// Http login and cors disabled
		http.httpBasic(withDefaults()).csrf(csrf -> csrf.disable()).cors(cors->cors.disable()).cors(cors -> cors.disable());
		http.cors(withDefaults());

		// Route filter
		http.authorizeHttpRequests(auth -> auth
                .requestMatchers("api/user/register", "api/user/login", "api/user/traditionalRegister", "/api/user/loginSuccess", "/api/user/activate", "/oauth/**").permitAll()
                .anyRequest().authenticated()).oauth2Login(oauth -> 
				oauth.tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
						.and().defaultSuccessUrl("/api/user/loginSuccess")
						.userInfoEndpoint().userService(oauthUserService).and()
						.successHandler(new AuthenticationSuccessHandler(){

							@Override
							public void onAuthenticationSuccess(HttpServletRequest request,
									HttpServletResponse response,
									org.springframework.security.core.Authentication authentication)
									throws IOException, ServletException {
										OauthUser user = (OauthUser) authentication.getPrincipal();
										User check = userService.findById(user.getEmail());
										if(check == null){
											try {
												userService.register(new RegisterUserDTO(user.getName(), user.getEmail()));
												check = userService.findById(user.getEmail());
												QR qr = qrService.save((passwordEncoder.encode(check.getId().toString() + Long.toString(System.currentTimeMillis()))));
                								QRResponseDTO qrResponseDTO = new QRResponseDTO(qr.getCode());
												PrintWriter	out = response.getWriter();
												response.setContentType("application/json");
												response.setCharacterEncoding("UTF-8");
												out.print(qrResponseDTO);
												out.flush();
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										else{
											try {
												if(!check.getActive()){
													QR qr = qrService.save((passwordEncoder.encode(check.getId().toString() + Long.toString(System.currentTimeMillis()))));
													QRResponseDTO qrResponseDTO = new QRResponseDTO(qr.getCode());
													PrintWriter	out = response.getWriter();
													response.setContentType("application/json");
													response.setCharacterEncoding("UTF-8");
													out.print(qrResponseDTO);
													out.flush();
												}
												else{
													String token = jwtTools.generateToken(check);
													PrintWriter	out = response.getWriter();
													response.setContentType("application/json");
													response.setCharacterEncoding("UTF-8");
													out.print(token);
													out.flush();
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										response.setStatus(200);
										response.sendRedirect("/");
							}
						})
					);

		

		// UnAunthorized handler
		http.exceptionHandling(handling -> handling.authenticationEntryPoint((req, res, ex) -> {
			res.sendError(
					HttpServletResponse.SC_UNAUTHORIZED,
					"Auth fail!");
		}));

		// JWT filter
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

		managerBuilder
				.userDetailsService(identifier -> {
					User user = userService.findById(identifier);

					if (user == null)
						throw new UsernameNotFoundException("User: " + identifier + ", not found!");

					return user;
				})
				.passwordEncoder(passwordEncoder);

		return managerBuilder.build();
	}

	@Bean
	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
		return new DefaultAuthorizationCodeTokenResponseClient();
	}
}
