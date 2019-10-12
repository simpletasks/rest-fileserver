package com.javadevbod.fileserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadevbod.fileserver.data.jpa.User;
import com.javadevbod.fileserver.data.jpa.UserService;

@RequestMapping(path = "/")
@RestController
public class RegisterController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private FileSystemService fileSystemService;

    @PostMapping(path = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        System.out.println("new user :" + user.toString());
        
        if (userService.isNewUniqueEmail(user)) {
            userService.persist(user);
            fileSystemService.createUserDirectory(user.getUsername());
        } else {
            return new ResponseEntity<String>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }
}
