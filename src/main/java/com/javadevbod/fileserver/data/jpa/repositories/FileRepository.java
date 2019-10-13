package com.javadevbod.fileserver.data.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javadevbod.fileserver.data.jpa.entities.File;
import com.javadevbod.fileserver.data.jpa.entities.User;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{

    List<File> findByUser(User user);
    
    List<File> findByUserAndFilename(User user, String filename);
    
    File findByUserAndFileId(User user, String fileId);
    
    int countByFileId(String uuid);
    
}
