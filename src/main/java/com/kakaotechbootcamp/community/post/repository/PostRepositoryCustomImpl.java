package com.kakaotechbootcamp.community.post.repository;

import com.kakaotechbootcamp.community.post.entity.Post;
import com.kakaotechbootcamp.community.post.entity.QPost;
import com.kakaotechbootcamp.community.user.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final QPost p = QPost.post;
    private final QUser u = QUser.user;
    private final JPAQueryFactory queryFactory;


    @Override
    public Slice<Post> findByLastReadId(Long lastReadId, int limit) {
        int pageSize = Math.min(Math.max(limit, 1), 10);

        List<Post> rows = queryFactory.selectFrom(p)
            .join(p.author, u).fetchJoin()
            .where(cursorCondition(lastReadId))
            .orderBy(p.postId.desc())
            .limit(pageSize + 1L)
            .fetch();

        boolean hasNext = rows.size() > pageSize;
        if (hasNext) {
            rows.removeLast();
        }

        return new SliceImpl<>(rows, PageRequest.of(0, pageSize), hasNext);
    }

    @Override
    public Long incrementViewCount(long postId) {
        return queryFactory.update(p)
            .set(p.viewCount, p.viewCount.add(1))
            .where(p.postId.eq(postId))
            .execute();
    }

    private BooleanExpression cursorCondition(Long cursor) {
        return (cursor == null || cursor <= 0) ? null : p.postId.lt(cursor);
    }
}
