package com.niti.vision.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niti.vision.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    //Role findByName(String name);
	Optional<Role> findByName(String name);
}
