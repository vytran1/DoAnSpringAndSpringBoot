package com.shopme.common.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name= "roles")
public class Role extends IdBasedEntity {
//	@jakarta.persistence.Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer Id;
	
	@Column(length = 40, nullable = false, unique = true)
    private String name;
	
	@Column(length = 150, nullable = false)
    private String description;
	
	public Role() {
		
	}
	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Role(Integer id) {
		super();
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
//	public Integer getId() {
//		return Id;
//	}
//	public void setId(Integer id) {
//		Id = id;
//	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return this.name;
	}
    
}
