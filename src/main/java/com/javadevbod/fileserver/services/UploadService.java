package com.javadevbod.fileserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.javadevbod.fileserver.controllers.entities.FileShareInvite;
import com.javadevbod.fileserver.data.jpa.entities.File;
import com.javadevbod.fileserver.data.jpa.entities.User;
import com.javadevbod.fileserver.data.jpa.services.FileService;
import com.javadevbod.fileserver.data.jpa.services.ShareFileService;
import com.javadevbod.fileserver.data.jpa.services.UserService;

@Service
public class UploadService {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ShareFileService shareFileService;

    @Autowired
    private FileSystemService fileSystemService;
    
    
    public ResponseEntity<String> fileShareInvite(FileShareInvite invite, Authentication authentication) {
        User user = userService.loadUserByUsername(authentication.getName());

        File fileOwnedByLoggedUser = fileService.findByUserAndFileId(user, invite.getFileId());

        if (fileOwnedByLoggedUser == null) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        User shareUser = userService.loadUserByUsername(invite.getEmail());
        
        if (shareFileService.findByUserAndFileFileId(shareUser, invite.getFileId()) != null) {
            return new ResponseEntity<String>(HttpStatus.ACCEPTED);
        }

        shareFileService.save(shareUser, fileOwnedByLoggedUser);

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }
    
    public ResponseEntity<String> uploadFile(MultipartFile file, Authentication authentication) {
        User user = userService.loadUserByUsername(authentication.getName());

        File fileEntity = fileService.persist(user, file.getOriginalFilename());

        if (isUniqueFileIdTaken(user, fileEntity, 0)) {
            fileService.delete(fileEntity);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        fileSystemService.persistFile(user, file, fileEntity.getFileId());
        return new ResponseEntity<String>(fileEntity.getFileId(), HttpStatus.OK);
    }

    private boolean isUniqueFileIdTaken(User user, File fileEntity, int loop) {
        if (loop >= 50) {
            return true;
        }
        if (fileSystemService.isUniqueFileIdAlreadyUsed(user, fileEntity.getFileId())) {
            fileEntity.setFileId(fileService.generateUniqueUUID());
            return isUniqueFileIdTaken(user, fileEntity, ++loop);
        }
        
        fileService.update(fileEntity);
        return false;
    }
    
}
