package com.shopme.common.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="shipping_rates")
public class ShippingRate extends IdBasedEntity {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    private float rate;
    private int days;
    
    @Column(name = "cod_supported")
    private boolean codSupported;
    
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    
    @Column(nullable = false, length = 45)
    private String state;

	public ShippingRate() {
		super();
	}

	public ShippingRate(Integer id) {
		super();
		this.id = id;
	}

//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isCodSupported() {
		return codSupported;
	}

	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ShippingRates [id=" + id + ", rate=" + rate + ", days=" + days + ", codSupported=" + codSupported
				+ ", country=" + country.getName() + ", state=" + state + "]";
	}

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
		ShippingRate other = (ShippingRate) obj;
		return Objects.equals(id, other.id);
	}
    
    
    
}
