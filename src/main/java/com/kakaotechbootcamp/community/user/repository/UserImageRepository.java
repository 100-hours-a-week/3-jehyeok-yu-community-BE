package com.kakaotechbootcamp.community.user.repository;

import com.kakaotechbootcamp.community.user.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long>,
    UserImageRepositoryCustom {

}
