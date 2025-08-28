package com.niti.vision.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Boolean active = false;
    private LocalDateTime deactivatedAt;
    private LocalDateTime created_dt;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
        this.active = (active != null ? active : false);
        if (!this.active) {
        	 this.deactivatedAt = null; // if reactivated, reset
            
        } else {
        	this.deactivatedAt = LocalDateTime.now(); // ✅ auto set timestamp when inactive
        }
    }
	public LocalDateTime getDeactivatedAt() { 
		return deactivatedAt; 
		}
    public void setDeactivatedAt(LocalDateTime deactivatedAt) { 
    	this.deactivatedAt = deactivatedAt;
    	}

	public LocalDateTime getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(LocalDateTime created_dt) {
		this.created_dt = created_dt;
	}
    
}
