package com.kakaotechbootcamp.community.post.entity;

import com.kakaotechbootcamp.community.comment.entity.Comment;
import com.kakaotechbootcamp.community.image.entity.Image;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.utils.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Slf4j
@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE posts SET deleted_at = now() WHERE post_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;


    @Column(nullable = false, length = 26)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "UNSIGNED INT")
    private long viewCount;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PostImage postImage;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments;

    private Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    // 팩토리메서드
    static public Post create(User author, String title, String content) {
        return new Post(author, title, content);
    }

    static public Post create(User author, String title, String content, Image image) {
        Post createdPost = create(author, title, content);
        if (image != null) {
            createdPost.addPostImage(image);
        }
        return createdPost;
    }

    public void addPostImage(Image postImage) {
        this.postImage = PostImage.create(this, postImage);
    }


    // 엔티티 메서드
    public void updateContent(String content) {
        this.content = content;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void incrementViewCount() {
        this.viewCount += 1;
    }

    public String getObjectKey() {
        return this.postImage == null ? null : this.postImage.getObjectKey();
    }
}
