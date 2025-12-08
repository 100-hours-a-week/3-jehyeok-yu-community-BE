package com.kakaotechbootcamp.community.post.entity;


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
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "post_like")
@SQLDelete(sql = "UPDATE comments SET deleted_at = now() WHERE comment_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Getter
    private boolean isDeleted;

    protected PostLike() {
    }

    private PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
        this.isDeleted = true;
    }

    public static PostLike create(Post post, User user) {
        return new PostLike(post, user);
    }

    public void toggleLike() {
        this.isDeleted = !this.isDeleted;
    }
}
