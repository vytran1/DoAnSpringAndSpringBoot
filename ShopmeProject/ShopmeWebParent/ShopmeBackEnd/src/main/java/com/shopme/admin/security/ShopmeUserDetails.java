package com.shopme.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

public class ShopmeUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	
	

	public ShopmeUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = this.user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList();
		for(Role x : roles) {
			authorities.add(new SimpleGrantedAuthority(x.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.getEnabled();
	}
    
	public String getFullname() {
		return this.user.getFirstName() + " " + this.user.getLastName();
	}
	
	public void setFirstName(String firstName) {
		this.user.setFirstName(firstName);
	}
	public void setLastName(String lastName) {
		this.user.setLastName(lastName);
	}
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
	
	public User getUser() {
		return this.user;
	}
}
