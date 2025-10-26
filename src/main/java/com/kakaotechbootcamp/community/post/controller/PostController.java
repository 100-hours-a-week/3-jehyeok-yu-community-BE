package com.kakaotechbootcamp.community.post.controller;

import com.kakaotechbootcamp.community.post.dto.request.PostCreateRequestDto;
import com.kakaotechbootcamp.community.post.dto.request.PostUpdateRequestDto;
import com.kakaotechbootcamp.community.post.dto.response.PostResponseDto;
import com.kakaotechbootcamp.community.post.dto.response.PostsResponseDto;
import com.kakaotechbootcamp.community.post.service.PostService;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import com.kakaotechbootcamp.community.utils.security.AuthPrincipal;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(
        @AuthenticationPrincipal AuthPrincipal principal,
        @RequestBody PostCreateRequestDto req) {
        postService.create(principal.getUserId(), req);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostsResponseDto>> getPosts(
        @RequestParam(value = "lastReadId", required = false) @Min(value = 1) Long lastReadId,
        @RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
        PostsResponseDto responsePostsDto = postService.getPosts(lastReadId, limit);
        return ResponseEntity.ok(ApiResponse.ok(responsePostsDto));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(
        @AuthenticationPrincipal AuthPrincipal principal,
        @PathVariable @Min(1) Long postId) {
        return ResponseEntity.ok(
            ApiResponse.ok(postService.getPost(postId, principal.getUserId())));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
        @AuthenticationPrincipal AuthPrincipal principal,
        @PathVariable Long postId,
        @RequestBody PostUpdateRequestDto req) {
        postService.update(postId, req, principal.getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.ok(null));
    }
}
