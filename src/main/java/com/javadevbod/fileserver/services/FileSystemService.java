package com.javadevbod.fileserver.services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.javadevbod.fileserver.data.jpa.entities.User;

@Service
public class FileSystemService {

    @Autowired
    private Environment env;
    private String fileStoreDir;
    private Path root;

    @PostConstruct
    public void init() {
        fileStoreDir = env.getProperty("com.demo.uploads.directory");
        root = Paths.get(fileStoreDir);
    }

    public void createUserDirectory(String userEmail) {

        userEmail = userEmail.replace("@", "__");

        if (Files.notExists(root)) {
            try {
                root = Files.createDirectory(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path userDir = root.resolve(Paths.get(userEmail));

        if (Files.notExists(userDir)) {
            try {
                userDir = Files.createDirectory(userDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isUniqueFileIdAlreadyUsed(User user, String uniqueFileId) {

        String userUniqueDir = user.getUsername().replace("@", "__");

        Path userDir = root.resolve(Paths.get(userUniqueDir));
        Path filename = userDir.resolve(Paths.get(uniqueFileId));

        return Files.exists(filename);

    }

    public void persistFile(User user, MultipartFile file, String uniqueFileId) {

        String userUniqueDir = user.getUsername().replace("@", "__");
        Path userDir = root.resolve(Paths.get(userUniqueDir));
        Path filename = userDir.resolve(Paths.get(uniqueFileId));

        if (!isUniqueFileIdAlreadyUsed(user, uniqueFileId)) {
            try {
                filename = Files.createDirectory(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            write(file, filename);
        }
    }

    public void write(MultipartFile file, Path dir) {
        Path filepath = Paths.get(dir.toString(), file.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Resource loadFileAsResource(User owner, com.javadevbod.fileserver.data.jpa.entities.File file) {
        String userUniqueDir = owner.getUsername().replace("@", "__");

        try {
            Path filePath = root.resolve(Paths.get(userUniqueDir)).resolve(Paths.get(file.getFileId())).resolve(Paths.get(file.getFilename())).normalize();
            System.out.println(" .... fil path ??? " + filePath.toString());
            Resource resource = new UrlResource(filePath.toUri());
            return resource;
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
