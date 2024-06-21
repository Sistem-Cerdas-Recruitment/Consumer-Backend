package com.BE.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import software.amazon.awssdk.services.s3.model.S3Exception;

@ControllerAdvice
public class FileExceptionHandler {
    
    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<Object> handleS3Exception(S3Exception e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Failed to upload file: " + e.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<Object> handleSizeLimitExceededException(SizeLimitExceededException e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "File is too large");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFileNameException.class)
    public ResponseEntity<Object> handleInvalidFileNameException(InvalidFileNameException e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
