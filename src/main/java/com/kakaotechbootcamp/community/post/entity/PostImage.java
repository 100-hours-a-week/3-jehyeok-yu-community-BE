package com.kakaotechbootcamp.community.post.entity;

import com.kakaotechbootcamp.community.image.entity.Image;
import com.kakaotechbootcamp.community.utils.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", nullable = false, unique = true)
    private Image image;

    private PostImage(Post post, Image image) {
        this.post = post;
        this.image = image;
    }

    public static PostImage create(Post post, Image image) {
        return new PostImage(post, image);
    }

    public String getObjectKey() {
        return image.getObjectKey();
    }

    public void changeOriginalName(String originalName) {
        this.image.changeOriginalName(originalName);
    }
}
