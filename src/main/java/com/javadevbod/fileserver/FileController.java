package com.javadevbod.fileserver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.javadevbod.fileserver.data.jpa.File;
import com.javadevbod.fileserver.data.jpa.FileService;
import com.javadevbod.fileserver.data.jpa.ShareFileService;
import com.javadevbod.fileserver.data.jpa.SharedFile;
import com.javadevbod.fileserver.data.jpa.User;
import com.javadevbod.fileserver.data.jpa.UserService;

@RequestMapping(path = "api")
@RestController
public class FileController {

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ShareFileService shareFileService;

    @Autowired
    private FileSystemService fileSystemService;

    @GetMapping(path = "file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AvailableFiles> getFileList(Authentication authentication) {

        AvailableFiles availableFiles = userService.getAvailableFiles(userService.loadUserByUsername(authentication.getName()));
        return new ResponseEntity<AvailableFiles>(availableFiles, HttpStatus.OK);
    }

    @PostMapping(path = "file"/* , consumes = MediaType.MULTIPART_FORM_DATA_VALUE */)
    public ResponseEntity<String> uploadFile(@RequestParam("asdf") MultipartFile file, Authentication authentication) {

        User user = userService.loadUserByUsername(authentication.getName());

        File fileEntity = fileService.saveFile(user, file.getOriginalFilename());
        // TODO recursive method
        fileSystemService.isUniqueFileIdAlreadyUsed(user, fileEntity.getFileId());

        fileSystemService.persistFile(user, file, fileEntity.getFileId());
        System.out.println("........." + authentication.getName());
        String uploadDir = env.getProperty("com.demo.uploads.directory");

        System.out.println(".. upload dir: " + uploadDir);

        return new ResponseEntity<String>(fileEntity.getFileId(), HttpStatus.OK);
    }

    @PostMapping("share")
    public ResponseEntity<String> fileShareInvite(@RequestBody FileShareInvite invite, Authentication authentication, HttpServletRequest request) {

        User user = userService.loadUserByUsername(authentication.getName());

        File fileOwnedByLoggedUser = fileService.findByUserAndFileId(user, invite.getFileId());

        if (fileOwnedByLoggedUser == null) {
            return new ResponseEntity<String>(HttpStatus.ACCEPTED);
        }

        User shareUser = userService.loadUserByUsername(invite.getEmail());

        shareFileService.save(shareUser, fileOwnedByLoggedUser);

        return new ResponseEntity<String>(HttpStatus.CREATED);

    }

    @GetMapping("file/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id, Authentication authentication, HttpServletRequest request) {

        User user = userService.loadUserByUsername(authentication.getName());

        Resource resource = getFileIfAuthorizedByOwnership(id, user);

        if (resource == null) {
            return new ResponseEntity<Resource>(HttpStatus.NO_CONTENT);
        }

        String contentType = tryFindContentTypeOrDefault(request, resource);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

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

}
