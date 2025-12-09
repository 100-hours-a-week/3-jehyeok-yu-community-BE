package com.kakaotechbootcamp.community.user.entity;

import com.kakaotechbootcamp.community.image.entity.Image;
import com.kakaotechbootcamp.community.utils.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

@Slf4j
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE user_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long userId;

    @Email
    @Column(unique = true, nullable = false, length = 255)
    @Getter
    private String email;

    @Length(min = 2, max = 10)
    @Column(unique = true, nullable = false, length = 10)
    @Getter
    private String nickname;

    @Column(nullable = false, length = 60)
    @Length(min = 60, max = 60, message = "저장과정에 서버 에러가 발생했습니다.")
    @Getter
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Getter
    private UserImage userImage;

    // 팩토리 메서드
    private User(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    static public User create(String email, String nickname, String password) {
        return new User(email, nickname, password);
    }

    public static User create(String email, String nickname, String password, Image profileImage,
        String thumbnailObjectKey) {
        User user = new User(email, nickname, password);
        if (profileImage != null) {
            user.addProfileImage(profileImage,
                thumbnailObjectKey == null ? profileImage.getObjectKey() : thumbnailObjectKey);
        }
        return user;
    }


    public void addProfileImage(Image image, String thumbnailObjectKey) {
        this.userImage = UserImage.create(this, image, thumbnailObjectKey);
    }

    public String getObjectKey() {
        log.info("getObject Key, {}", this.userImage);
        return this.userImage == null ? null : this.userImage.getObjectKey();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String encode) {
        this.password = encode;
    }
}
