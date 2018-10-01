package com.dridi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dridi.persistance.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
User findoneByUsername(String username );
}
