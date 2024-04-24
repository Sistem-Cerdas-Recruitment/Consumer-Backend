package com.BE.services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final Path rootLocation = Paths.get("upload-dir");

    public Path store(MultipartFile file, String name) throws FileUploadException{
        String tempFileName = new Date().getTime() + "-" + name + "-" + file.getOriginalFilename();
        try {
            if (file.isEmpty()) {
                throw new FileUploadException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(tempFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new FileUploadException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile;
        } catch (IOException e) {
            throw new FileUploadException("Failed to store file.", e);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        try{
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(fileName))
                    .normalize().toAbsolutePath();
            Files.delete(destinationFile);
        } catch (IOException e){
            throw new IOException("Failed to delete file.", e);
        
        }
    }
}
