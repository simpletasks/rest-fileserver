package com.javadevbod.fileserver.data.jpa.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javadevbod.fileserver.data.jpa.entities.File;
import com.javadevbod.fileserver.data.jpa.entities.SharedFile;
import com.javadevbod.fileserver.data.jpa.entities.User;
import com.javadevbod.fileserver.data.jpa.repositories.ShareFileRepository;

@Service
public class ShareFileService {

    @Autowired
    private ShareFileRepository repository;
    
    public void save(User user, File file) {
        SharedFile sharedFile = new SharedFile();
        sharedFile.setFile(file);
        sharedFile.setUser(user);
        repository.save(sharedFile);
    }
    
    public List<File> findFilesByUser(User user){
        List<SharedFile> shares = repository.findAllByUser(user);
        
        List<File> sharedFiles = shares.stream().map(e -> e.getFile()).collect(Collectors.toCollection(ArrayList::new));
        return sharedFiles;
    }
    
    public SharedFile findByUserAndFileFileId(User user, String fileId) {
        return repository.findByUserAndFileFileId(user, fileId);
    }
}
