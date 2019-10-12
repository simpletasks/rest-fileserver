package com.javadevbod.fileserver.data.jpa;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private FileRepository repository;

    public List<File> findByUser(User user) {
        return repository.findByUser(user);
    }
    
    public File findByUserAndFileId(User user, String fileId) {
        return repository.findByUserAndFileId(user, fileId);
    }

    public File saveFile(User user, String filename) {

        List<File> existingFiles = repository.findByUserAndFilename(user, filename);
        Collections.sort(existingFiles, new FileNrComparator().reversed());

        String generatedId = generateUniqueUUID();
        File newFile = new File();
        newFile.setUser(user);
        newFile.setFilename(filename);
        if (existingFiles.isEmpty()) {
            newFile.setFileVersionNr(1);
        } else {
            newFile.setFileVersionNr(existingFiles.get(0).getFileVersionNr() + 1);
        }
        newFile.setFileId(generatedId);

        repository.save(newFile);

        return newFile;

    }

    private String generateUniqueUUID() {
        String generatedId = UUID.randomUUID().toString();
        if (repository.countByFileId(generatedId) > 0) {
            return generateUniqueUUID();
        }
        return generatedId;
    }
}
