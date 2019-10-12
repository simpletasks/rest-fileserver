package com.javadevbod.fileserver.data.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareFileRepository extends JpaRepository<SharedFile, Long> {

    List<SharedFile> findAllByUser(User user);
    
    SharedFile findByUserAndFileFileId(User user, String fileId);

}
