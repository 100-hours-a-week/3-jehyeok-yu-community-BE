package com.kakaotechbootcamp.community.post.repository;

import com.kakaotechbootcamp.community.post.entity.Post;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {

    Slice<Post> findByLastReadId(Long lastReadId, int limit);

    Long incrementViewCount(long postId);
}
