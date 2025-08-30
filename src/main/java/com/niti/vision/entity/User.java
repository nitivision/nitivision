package com.niti.vision.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean active = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @Column(nullable = true)
    private String otp;
    @Column(nullable = false)
    private Long otpGeneratedAt;
    @Column(nullable = false)
    private LocalDateTime deactivatedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
 // ---------- Constructors ----------
    public User() {}

    private User(UserBuilder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.active = builder.active;
        this.roles = builder.roles;
    }
    
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
            this.deactivatedAt = LocalDateTime.now(); // ✅ auto set timestamp when inactive
        } else {
            this.deactivatedAt = null; // if reactivated, reset
        }
    }
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public Long getOtpGeneratedAt() {
		return otpGeneratedAt;
	}
	public void setOtpGeneratedAt(Long otpGeneratedAt) {
		this.otpGeneratedAt = otpGeneratedAt;
	}
	public LocalDateTime getDeactivatedAt() { 
		return deactivatedAt; 
		}
    public void setDeactivatedAt(LocalDateTime deactivatedAt) { 
    	this.deactivatedAt = deactivatedAt;
    	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	// ---------- Builder ----------
    public static class UserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private boolean active;
        private Set<Role> roles = new HashSet<>();

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }
        public UserBuilder active(boolean active) {
            this.active = active;
            return this;
        }
        public UserBuilder roles(Set<Role> set) {
            this.roles = set;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }
}
