package com.javadevbod.fileserver.data.jpa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javadevbod.fileserver.controllers.entities.AvailableFiles;
import com.javadevbod.fileserver.data.jpa.entities.User;
import com.javadevbod.fileserver.data.jpa.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private FileService fileService;

    @Autowired
    private ShareFileService shareFileService;

    @Autowired
    private UserRepository repository;

    public void persist(User user) {
        repository.save(user);
    }

    public boolean isNewUniqueEmail(User user) {
        if (repository.countByUsername(user.getUsername()) > 0) {
            return false;
        }

        return true;
    }

    public AvailableFiles getAvailableFiles(User user) {
        AvailableFiles container = new AvailableFiles();
        container.setOwned(fileService.findByUser(user));
        container.setShared(shareFileService.findFilesByUser(user));
        return container;
    }

    public User loadUserByUsername(String email) {
        return repository.findByUsername(email);
    }
}
