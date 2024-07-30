package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception{
		http.authenticationProvider(authenticationProvider());
		http.authorizeHttpRequests((auth)->auth
				.requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin","Salesperson")
				.requestMatchers("/users/**","/settings/**","/countries/**","/states/**").hasAuthority("Admin")
				
				.requestMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
				
				.requestMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
				
				.requestMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")
				
				.requestMatchers("/products/edit/**","/products/save","/products/check_unique")
				                .hasAnyAuthority("Admin","Editor","Salesperson")
				
				.requestMatchers("/products","/products/","/products/detail/**","/products/page/**")
				                .hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
				                
				.requestMatchers("/products/**").hasAnyAuthority("Admin","Editor")
				.requestMatchers("/orders","/orders/","/orders/page/**","/orders/detail/**").hasAnyAuthority("Admin","Salesperson","Shipper")
				.requestMatchers("/products/detail/**","/customers/detail/**").hasAnyAuthority("Admin","Editor","Salesperson","Assistant")
				.requestMatchers("/customers/**","/orders/**","/get_shipping_cost","/reports/**").hasAnyAuthority("Admin","Salesperson")
				.requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
				.requestMatchers("/reviews/**","/questions/**").hasAnyAuthority("Admin","Assistant")
				.requestMatchers("/articles/**","/menus/**","/sections/**").hasAnyAuthority("Admin","Editor")
				
				.anyRequest().authenticated())
		    .formLogin((form)-> form
		    		.loginPage("/login")
		    		.usernameParameter("email")
		    		.permitAll()
		    		)
		    .logout(logout -> logout.permitAll())
		    .rememberMe(rem -> rem
		    		.key("DW4QrO3YwR")
		    		.tokenValiditySeconds(1 * 24 * 60 * 60)
		    		);
	  http.headers(header->header.frameOptions(frame->frame.sameOrigin()));	
		    
		return http.build();
	}
	
	//allow images
	@Bean
	WebSecurityCustomizer configureWebSecurity() throws Exception{
		return (web) -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
	}
}
