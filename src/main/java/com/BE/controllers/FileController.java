package com.BE.controllers;

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

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private BucketService bucketService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getName();

        try {
            String response = bucketService.put(file, username);
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
