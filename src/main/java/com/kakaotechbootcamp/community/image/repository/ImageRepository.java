package com.kakaotechbootcamp.community.image.repository;

import com.kakaotechbootcamp.community.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {

}
