package com.kakaotechbootcamp.community.comment.controller;

import com.kakaotechbootcamp.community.comment.dto.request.CommentPostRequestDto;
import com.kakaotechbootcamp.community.comment.dto.request.CommentUpdateRequestDto;
import com.kakaotechbootcamp.community.comment.dto.response.CommentListResponseDto;
import com.kakaotechbootcamp.community.comment.service.CommentService;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<Void>> createComment(@RequestAttribute("userId") Long userId,
        @PathVariable Long postId,
        @RequestBody CommentPostRequestDto commentPostRequestDto) {
        commentService.createComment(userId, postId, commentPostRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentListResponseDto>> getComments(
        @RequestAttribute("userId") Long userId,
        @PathVariable Long postId
    ) {
        var response = commentService.getCommentList(postId, userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
        @RequestAttribute("userId") Long userId,
        @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> patchComment(
        @RequestAttribute("userId") Long userId,
        @PathVariable Long commentId,
        @RequestBody CommentUpdateRequestDto request
    ) {
        commentService.updateComment(commentId, userId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }
}
