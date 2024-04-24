package com.BE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class BucketConfig {
    
    private final String awsAccessKey = "AKIAQ3EGRLH2KNN4WR4Y";
    private final String awsSecretKey = "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";

    // @Bean
    // public AmazonS3 s3() {
    //     AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    //     return AmazonS3ClientBuilder
    //             .standard()
    //             .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
    //             .withRegion(Regions.AP_SOUTHEAST_1)
    //             .build();
    // }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
            .region(Region.AP_SOUTHEAST_1)
            .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
            .region(Region.AP_SOUTHEAST_1)
            .build();
    }
}
