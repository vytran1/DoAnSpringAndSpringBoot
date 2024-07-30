package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    
	@Test
	public void testendocdePassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "12345678";
		String encodePassword = passwordEncoder.encode(rawPassword);
		System.out.println(encodePassword);
		System.out.println(passwordEncoder.matches(rawPassword, encodePassword));
		boolean isValid = passwordEncoder.matches(rawPassword, encodePassword);
		assertThat(isValid).isTrue();
	}
}
