package com.kakaotechbootcamp.community.post.service;

import com.kakaotechbootcamp.community.image.S3ClientCreator;
import com.kakaotechbootcamp.community.image.entity.Image;
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
    public final S3ClientCreator s3ClientCreator;

    public void create(long userId, PostCreateRequestDto dto) {
        User user = userRepository.getReferenceById(userId);

        Image postImage = null;
        if (dto.getImage() != null) {
            postImage = Image.create(
                dto.getImage().getOriginalName(),
                dto.getImage().getObjectKey()
            );
        }

        Post post = Post.create(user, dto.getTitle(), dto.getContent(), postImage);
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
                    new AuthorThumbNailDto(author.getNickname(),
                        s3ClientCreator.getPresignedGetUrl(author.getObjectKey()),
                        author.getUserId())));
        });
        return postsResponseDto;
    }

    @Transactional
    public PostResponseDto getPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);
        postRepository.incrementViewCount(post.getPostId());
        return toPostResponseDto(post, userId);
    }

    private PostResponseDto toPostResponseDto(Post post, Long userId) {
        return PostResponseDto.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .content(post.getContent())
            .postImagePath(
                s3ClientCreator.getPresignedGetUrl(post.getObjectKey()))
            .authorId(post.getAuthor().getUserId())
            .authorThumbnailPath(
                s3ClientCreator.getPresignedGetUrl(post.getAuthor().getObjectKey()))
            .nickname(post.getAuthor().getNickname())
            .viewCount(post.getViewCount())
            .createdAt(post.getCreatedAt())
            .commentCount(0)
            .likeCount(0)
            .owner(userId.equals(post.getAuthor().getUserId()))
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
