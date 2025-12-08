package com.kakaotechbootcamp.community.image.controller;


import com.kakaotechbootcamp.community.image.dto.response.PresignedUrlDto;
import com.kakaotechbootcamp.community.image.service.ImageService;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/url")
    public ResponseEntity<ApiResponse<PresignedUrlDto>> getPresignedPatchUrl(
        @RequestParam(value = "mode", required = false) String mode
    ) {
        return ResponseEntity.ok(ApiResponse.ok(imageService.getPresignedPatchUrl(mode)));
    }
}
