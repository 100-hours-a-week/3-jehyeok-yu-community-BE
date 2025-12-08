package com.kakaotechbootcamp.community.comment.entity;

import com.kakaotechbootcamp.community.post.entity.Post;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.utils.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comments SET deleted_at = now() WHERE comment_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, length = 200)
    private String content;

    private Comment(User author, Post post, String content) {
        this.author = author;
        this.post = post;
        this.content = content;
    }

    // 팩토리 메서드
    static public Comment create(User author, Post post, String content) {
        return new Comment(author, post, content);
    }

    public Long getAuthorId() {
        return this.author.getUserId();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
