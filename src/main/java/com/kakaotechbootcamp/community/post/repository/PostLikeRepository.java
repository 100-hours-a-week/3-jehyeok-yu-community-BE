package com.kakaotechbootcamp.community.post.repository;

import com.kakaotechbootcamp.community.post.entity.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPost_PostIdAndUser_UserId(Long postId, Long userId);


    Optional<PostLike> findByPost_PostIdAndUser_UserIdAndIsDeletedFalse(Long postId, Long userId);

    @Query("""
            select count(pl)
            from PostLike pl
            where pl.post.postId = :postId
              and pl.isDeleted = false
        """)
    long countByPost_PostIdAndIsDeletedFalse(@Param("postId") Long postId);
}
