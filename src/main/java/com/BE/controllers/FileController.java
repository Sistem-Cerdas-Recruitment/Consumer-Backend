package com.BE.controllers;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.BE.entities.User;
import com.BE.services.storage.BucketService;
import com.BE.services.storage.FileService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private BucketService bucketService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getName();

        try {
            Path tempPath = fileService.store(file, username);
            String response = bucketService.putS3Object(tempPath.getFileName().toString(), tempPath.toString());
            try {
                fileService.deleteFile(tempPath.getFileName().toString());
            } catch (Exception e) {
                log.error("Failed to delete file: " + e);
            }
            return response;
        } catch (Exception e) {
            return "Failed to upload file: " + e;
        }

    }

    @PostMapping("/getFileUrl")
    public String getFileUrl(@RequestBody String fileName) {
        return bucketService.createPresignedGetUrl(fileName);
    }

}
