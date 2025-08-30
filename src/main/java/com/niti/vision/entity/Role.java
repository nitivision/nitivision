package com.niti.vision.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

 // ---------- Constructors ----------
    public Role() {}

    private Role(RoleBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.users = builder.users;
    }
    
    // ✅ Convenience constructor
    public Role(String name) {
        this.name = name;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
    
	// ---------- Builder ----------
    public static class RoleBuilder {
        private Long id;
        private String name;
        private Set<User> users = new HashSet<>();

        public RoleBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public RoleBuilder name(String name) {
            this.name = name;
            return this;
        }
        public RoleBuilder users(Set<User> users) {
            this.users = users;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }

    public static RoleBuilder builder() {
        return new RoleBuilder();
    }
}
