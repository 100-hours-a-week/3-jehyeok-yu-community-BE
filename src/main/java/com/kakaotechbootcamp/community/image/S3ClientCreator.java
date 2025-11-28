package com.kakaotechbootcamp.community.image;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@Slf4j
public class S3ClientCreator {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3ClientCreator() {
        this.s3Client = S3Client.builder()
            .region(Region.AP_NORTHEAST_2)  // 서울 리전
            .credentialsProvider(ProfileCredentialsProvider.create("default"))
            .build();
        this.s3Presigner = S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2) // 쓰는 리전으로
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }


    public String getPresignedUrl() {
        return createPresignedUrl("profile-origin", buildProfileImageKey(),
            Collections.EMPTY_MAP);
    }

    private String createPresignedUrl(String bucketName, String keyName,
        Map<String, String> metadata) {

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(keyName)
            .metadata(metadata)
            .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(objectRequest)
            .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String myURL = presignedRequest.url().toString();

        log.info("Presigned URL to upload a file to: [{}]", myURL);
        log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

        return presignedRequest.url().toExternalForm();
    }


    private String buildProfileImageKey() {
        String uuid = UUID.randomUUID().toString();
        return "public/image/profile/" + uuid;
    }
}
