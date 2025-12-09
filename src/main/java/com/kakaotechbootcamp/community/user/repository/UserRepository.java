package com.kakaotechbootcamp.community.user.repository;

import com.kakaotechbootcamp.community.user.entity.User;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByEmail(@NotNull String email);

    Optional<User> findByNickname(@NotNull String nickname);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);
}
