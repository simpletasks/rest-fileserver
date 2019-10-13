package com.javadevbod.fileserver.data.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javadevbod.fileserver.data.jpa.entities.SharedFile;
import com.javadevbod.fileserver.data.jpa.entities.User;

public interface ShareFileRepository extends JpaRepository<SharedFile, Long> {

    List<SharedFile> findAllByUser(User user);
    
    SharedFile findByUserAndFileFileId(User user, String fileId);

}
