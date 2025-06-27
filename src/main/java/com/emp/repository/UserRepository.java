package com.emp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.emp.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
