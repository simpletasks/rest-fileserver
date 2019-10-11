package com.javadevbod.fileserver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "api")
@RestController
public class FileController {

    @GetMapping(path = "files")
    public ResponseEntity<String> getFileList() {

        return new ResponseEntity<String>("ID-file", HttpStatus.OK);
    }
}
