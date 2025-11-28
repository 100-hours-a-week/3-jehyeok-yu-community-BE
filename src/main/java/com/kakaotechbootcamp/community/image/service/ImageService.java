package com.kakaotechbootcamp.community.image.service;

import com.kakaotechbootcamp.community.image.S3ClientCreator;
import com.kakaotechbootcamp.community.image.dto.response.PresignedUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3ClientCreator s3ClientCreator;

    public PresignedUrlDto getPresignedPatchUrl(String mode) {
        return s3ClientCreator.getPresignedUrl();
    }
}
