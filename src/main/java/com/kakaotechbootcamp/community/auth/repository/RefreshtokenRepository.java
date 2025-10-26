package com.kakaotechbootcamp.community.auth.repository;

import com.kakaotechbootcamp.community.auth.entity.Refreshtoken;
import com.kakaotechbootcamp.community.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshtokenRepository extends JpaRepository<Refreshtoken, Long> {

    Optional<Refreshtoken> findByUser(User user);

    Optional<Refreshtoken> findByUser_UserId(long userId);
}
