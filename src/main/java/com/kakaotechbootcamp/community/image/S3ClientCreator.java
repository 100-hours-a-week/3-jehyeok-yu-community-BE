package com.kakaotechbootcamp.community.image;

import com.kakaotechbootcamp.community.image.dto.response.PresignedUrlDto;
import com.kakaotechbootcamp.community.user.dto.CachedUrl;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@Slf4j
public class S3ClientCreator {

    private final static String BUCKET_NAME = "profile-origin";
    private final static long CACHE_TTL_MS = 10 * 60 * 1000;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final ConcurrentHashMap<String, CachedUrl> presignStore;
    private final String[] AVAILABLE_IMAGE_TYPE = {"profile", "post"};

    public S3ClientCreator() {
        this.presignStore = new ConcurrentHashMap<>();
        this.s3Client = S3Client.builder()
            .region(Region.AP_NORTHEAST_2)  // 서울 리전
            .credentialsProvider(ProfileCredentialsProvider.create("default"))
            .build();
        this.s3Presigner = S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2) // 쓰는 리전으로
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

    public PresignedUrlDto getPutPresignedUrl(String mode, String objectKey) {
        String url = createPutPresignedUrl(BUCKET_NAME, objectKey, Collections.emptyMap());

        return new PresignedUrlDto(url, objectKey);
    }

    public PresignedUrlDto getPutPresignedUrl(String mode) {
        Optional<String> targetPath = Arrays.stream(AVAILABLE_IMAGE_TYPE)
            .filter(s -> s.equals(mode))
            .findFirst();
        log.info("[getPutPresignedUrl] path : {}, and target {}", targetPath.get(), mode);
        String path = targetPath.orElseThrow(PathNotFoundException::new);
        String objectKey = buildProfileImageKey(path);

        String url = createPutPresignedUrl(BUCKET_NAME, objectKey, Collections.emptyMap());

        return new PresignedUrlDto(url, objectKey);
    }

    private String createPutPresignedUrl(String bucketName, String keyName,
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


    private String buildProfileImageKey(String mode) {
        String uuid = UUID.randomUUID().toString();
        return "public/image/" + mode + "/" + uuid;
    }


    public String getPresignedGetUrl(String objectKey) {
        if (objectKey == null) {
            return null;
        }
        CachedUrl cached = presignStore.get(objectKey);

        if (cached != null && !cached.isExpired()) {
            return cached.getUrl();
        }

        String url = createPresignedGetUrl(BUCKET_NAME, objectKey);

        CachedUrl newCache = new CachedUrl(
            url,
            System.currentTimeMillis() + CACHE_TTL_MS
        );

        presignStore.put(objectKey, newCache);
        log.info("objectKey: [{}]", objectKey);
        return url;
    }

    private String createPresignedGetUrl(String bucketName, String keyName) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(keyName)
            .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .getObjectRequest(objectRequest)
            .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        log.info("Presigned URL: [{}]", presignedRequest.url().toString());
        log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

        return presignedRequest.url().toExternalForm();
    }

    public void evictCache(String objectKey) {
        if (objectKey == null) {
            return;
        }
        presignStore.remove(objectKey);
        log.info("evict presign cache, objectKey = {}", objectKey);
    }
}