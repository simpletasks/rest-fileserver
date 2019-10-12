package com.javadevbod.fileserver.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    int countByUsername(String email);
    
    User findByUsername(String email);
}
