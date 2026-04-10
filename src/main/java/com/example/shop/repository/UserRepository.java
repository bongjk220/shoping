// src/main/java/com/example/shop/repository/UserRepository.java
package com.example.shop.repository;

import com.example.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}