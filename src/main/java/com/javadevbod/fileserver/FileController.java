package com.javadevbod.fileserver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

import com.javadevbod.fileserver.data.jpa.UserService;

@RequestMapping(path = "api")
@RestController
public class FileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private DownloadService downloadService;

    @GetMapping(path = "file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AvailableFiles> getFileList(Authentication authentication) {

        AvailableFiles availableFiles = userService.getAvailableFiles(userService.loadUserByUsername(authentication.getName()));
        return new ResponseEntity<AvailableFiles>(availableFiles, HttpStatus.OK);
    }

    @PostMapping(path = "file"/* , consumes = MediaType.MULTIPART_FORM_DATA_VALUE */)
    public ResponseEntity<String> uploadFile(@RequestParam("asdf") MultipartFile file, Authentication authentication) {

        return uploadService.uploadFile(file, authentication);
    }

    @PostMapping("share")
    public ResponseEntity<String> fileShareInvite(@RequestBody FileShareInvite invite, Authentication authentication) {

        return uploadService.fileShareInvite(invite, authentication);

    }

    @GetMapping("file/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id, Authentication authentication, HttpServletRequest request) {

        return downloadService.downloadFile(id, authentication, request);
    }

}
