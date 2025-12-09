package com.kakaotechbootcamp.community.comment.service;

import com.kakaotechbootcamp.community.auth.exception.UserNotFoundException;
import com.kakaotechbootcamp.community.comment.dto.request.CommentPostRequestDto;
import com.kakaotechbootcamp.community.comment.dto.request.CommentUpdateRequestDto;
import com.kakaotechbootcamp.community.comment.dto.response.CommentListResponseDto;
import com.kakaotechbootcamp.community.comment.dto.response.CommentResponseDto;
import com.kakaotechbootcamp.community.comment.entity.Comment;
import com.kakaotechbootcamp.community.comment.execption.CommentForbiddenException;
import com.kakaotechbootcamp.community.comment.execption.CommentNotFoundException;
import com.kakaotechbootcamp.community.comment.repository.CommentRepository;
import com.kakaotechbootcamp.community.post.entity.Post;
import com.kakaotechbootcamp.community.post.exception.PostNotFoundException;
import com.kakaotechbootcamp.community.post.repository.post.PostRepository;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createComment(Long userId, Long postId,
        CommentPostRequestDto commentPostRequestDto) {
        User author = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);

        var comment = Comment.create(author, post, commentPostRequestDto.getContent());
        commentRepository.save(comment);
    }

    public CommentListResponseDto getCommentList(Long postId, Long userId) {
        User author = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        List<Comment> commentList = commentRepository.findByPost_postId(postId);
        return new CommentListResponseDto(
            commentList.stream().map(c -> mapCommentToDto(c, author.getUserId()))
                .toList()
        );
    }

    private CommentResponseDto mapCommentToDto(Comment comment, Long userId) {
        return CommentResponseDto.builder()
            .commentId(comment.getCommentId())
            .owner(Objects.equals(comment.getAuthorId(), userId))
            .contents(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .authorNickname(comment.getAuthor().getNickname())
            .authorThumbnailPath(comment.getAuthor().getObjectKey())
            .build();
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);

        if (!user.equals(comment.getAuthor())) {
            throw new CommentForbiddenException();
        }
        log.info("comment {} , user {}", comment.getContent(), user.getNickname());
        commentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(Long commentId, Long userId, CommentUpdateRequestDto request) {
        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);

        if (!user.equals(comment.getAuthor())) {
            throw new CommentForbiddenException();
        }

        comment.updateContent(request.getContent());
    }
}
