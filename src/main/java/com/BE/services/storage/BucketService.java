package com.BE.services.storage;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class BucketService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Presigner presigner;

    private String bucketName = "sistem-cerdas-bucket";

    public String createPresignedGetUrl(String keyName) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // The URL will expire in 10 minutes.
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toExternalForm();

    }

    public String getUrl(String keyName) {
        GetUrlRequest urlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
        return s3Client.utilities().getUrl(urlRequest).toString();
    }

    public byte[] get(String keyName) {

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        return s3Client.getObjectAsBytes(objectRequest).asByteArray();
    }

    public String put(MultipartFile file, String name) {
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("x-amz-meta-myVal", "test");
            String fileName = new Date().getTime() + "-" + name + "-" + file.getOriginalFilename();
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .metadata(metadata)
                    .build();

            byte[] bytes = file.getBytes();
            s3Client.putObject(putOb, RequestBody.fromBytes(bytes));

            return fileName;

        } catch (IOException e) {
            throw S3Exception.builder().cause(e).build();
        }
    }

    public void delete(String keyName) {
        try {
            s3Client.deleteObject(b -> b.bucket(bucketName).key(keyName));    
        } catch (Exception e) {
            throw S3Exception.builder().cause(e).build();
        }
    }
}
