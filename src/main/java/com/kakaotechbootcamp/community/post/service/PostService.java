package com.kakaotechbootcamp.community.post.service;

import com.kakaotechbootcamp.community.post.dto.request.PostCreateRequestDto;
import com.kakaotechbootcamp.community.post.dto.request.PostUpdateRequestDto;
import com.kakaotechbootcamp.community.post.dto.response.AuthorThumbNailDto;
import com.kakaotechbootcamp.community.post.dto.response.PostResponseDto;
import com.kakaotechbootcamp.community.post.dto.response.PostThumbNailResponseDto;
import com.kakaotechbootcamp.community.post.dto.response.PostsResponseDto;
import com.kakaotechbootcamp.community.post.entity.Post;
import com.kakaotechbootcamp.community.post.exception.PostNotFoundException;
import com.kakaotechbootcamp.community.post.repository.PostRepository;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.user.repository.UserRepository;
import com.kakaotechbootcamp.community.utils.exception.customexception.ForbiddenException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    public final PostRepository postRepository;
    public final UserRepository userRepository;

    public void create(long userId, PostCreateRequestDto req) {
        User user = userRepository.getReferenceById(userId);
        Post post = Post.create(user, req.getTitle(), req.getContent());
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostsResponseDto getPosts(Long lastReadId, int limit) {
        Slice<Post> posts = postRepository.findByLastReadId(lastReadId, limit);
        long nextCursor = posts.hasNext()
            ? posts.getContent().get(posts.getNumberOfElements() - 1).getPostId()
            : -1;
        return makeSliceToPostDto(posts, nextCursor);
    }

    private PostsResponseDto makeSliceToPostDto(Slice<Post> posts, long nextCursor) {
        PostsResponseDto postsResponseDto = new PostsResponseDto(
            nextCursor, posts.hasNext(),
            new ArrayList<>());
        List<PostThumbNailResponseDto> postList = postsResponseDto.getPosts();
        posts.stream().forEach(e -> {
            User author = e.getAuthor();
            postList.add(
                new PostThumbNailResponseDto(e.getTitle(), e.getPostId(), 0, 0, e.getViewCount(),
                    e.getCreatedAt(),
                    new AuthorThumbNailDto(author.getNickname(), "default", author.getUserId())));
        });
        return postsResponseDto;
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId, Long userId) {
        return postRepository.findById(postId)
            .map((p) -> toPostResponseDto(p, userId))
            .orElseThrow(PostNotFoundException::new);
    }

    private PostResponseDto toPostResponseDto(Post post, Long userId) {
        return PostResponseDto.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .content(post.getContent())
            .authorId(post.getAuthor().getUserId())
            .nickname(post.getAuthor().getNickname())
            .viewCount(post.getViewCount())
            .createdAt(post.getCreatedAt())
            .commentCount(0)
            .likeCount(0)
            .owner(userId == post.getAuthor().getUserId())
            .build();
    }

    public void update(long postId, PostUpdateRequestDto req, long userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);
        User author = post.getAuthor();
        if (!author.getUserId().equals(userId)) {
            throw new ForbiddenException("게시물 접근");
        }
        post.updateContent(req.getContent());
        post.updateTitle(req.getTitle());
    }
}
