package com.dridi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dridi.persistance.model.User;
import java.lang.String;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findOneByUsername(String username);
}
