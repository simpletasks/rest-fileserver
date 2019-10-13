package com.javadevbod.fileserver.data.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javadevbod.fileserver.data.jpa.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    int countByUsername(String email);
    
    User findByUsername(String email);
}
