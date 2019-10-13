package com.javadevbod.fileserver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.javadevbod.fileserver.data.jpa.File;
import com.javadevbod.fileserver.data.jpa.FileService;
import com.javadevbod.fileserver.data.jpa.ShareFileService;
import com.javadevbod.fileserver.data.jpa.SharedFile;
import com.javadevbod.fileserver.data.jpa.User;
import com.javadevbod.fileserver.data.jpa.UserService;

@Service
public class DownloadService {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ShareFileService shareFileService;

    @Autowired
    private FileSystemService fileSystemService;

    private Resource getFileIfAuthorizedByOwnership(String id, User user) {
        Resource resource = null;
        File fileToDownload = fileService.findByUserAndFileId(user, id);
        if (fileToDownload != null) {
            resource = fileSystemService.loadFileAsResource(user, fileToDownload);
        } else {
            SharedFile sharedFileToDownload = shareFileService.findByUserAndFileFileId(user, id);
            if (sharedFileToDownload != null) {
                resource = fileSystemService.loadFileAsResource(sharedFileToDownload.getUser(), sharedFileToDownload.getFile());
            }
        }
        return resource;
    }

    private String tryFindContentTypeOrDefault(HttpServletRequest request, Resource resource) {
        String defaultContentType = "text/plain";

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = defaultContentType;
        }
        return contentType;
    }

    public ResponseEntity<Resource> downloadFile(String fileId, Authentication authentication, HttpServletRequest request) {
        User user = userService.loadUserByUsername(authentication.getName());

        Resource resource = getFileIfAuthorizedByOwnership(fileId, user);

        if (resource == null) {
            return new ResponseEntity<Resource>(HttpStatus.NO_CONTENT);
        }

        String contentType = tryFindContentTypeOrDefault(request, resource);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }
}
