package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
	
	@Autowired
	private CustomerOAuth2UserService oauthService;
	
	@Autowired
	private OAuth2LoginSuccessHandler  ouAuth2LoginSuccessHandler;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception{
		
		http.authenticationProvider(authenticationProvider());
		http.authorizeHttpRequests((auth)->auth
				    .requestMatchers("/account_details").authenticated()
				    .requestMatchers("/update_account_details").authenticated()
				    .requestMatchers("/cart").authenticated()
				    .requestMatchers("/address_book/**").authenticated()
				    .requestMatchers("/checkout","/place_order","/process_paypal_order").authenticated()
				    .requestMatchers("/orders/**").authenticated()
				    .requestMatchers("/reviews/**").authenticated()
				    .requestMatchers("/write_review/**").authenticated()
				    .requestMatchers("/post_review").authenticated()
				    .requestMatchers("/customer/questions/**","/post_question/**","/ask_question/**").authenticated()
				    .anyRequest().permitAll())
		    .formLogin((form)->form
		    		.loginPage("/login")
		    		.usernameParameter("email")
		    		.successHandler(databaseLoginSuccessHandler)
		    		.permitAll()
		    )
		    .oauth2Login((form)->form
		            .loginPage("/login")
		            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oauthService))
		            .successHandler(ouAuth2LoginSuccessHandler)
		    		)
		    .logout(logout -> logout.permitAll())
		    .rememberMe(rem -> rem
		    		.key("sl8ohWO4fWhufxHPBo7A")
		    		.tokenValiditySeconds(1*24*60*60)
		    		);
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		return http.build();
		 
	}
	
	
	
	
	//allow images
	@Bean
	WebSecurityCustomizer configureWebSecurity() throws Exception{
		return (web) -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
	}
	
	
}
