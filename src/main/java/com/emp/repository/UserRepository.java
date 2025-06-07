package com.emp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emp.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Object findByUsername(String username);

	boolean existsByUsername(String username);

}
