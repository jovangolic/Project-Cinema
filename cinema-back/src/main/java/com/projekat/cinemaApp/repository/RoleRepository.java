package com.projekat.cinemaApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projekat.cinemaApp.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(String role);

	boolean existsByName(Role role);

}
