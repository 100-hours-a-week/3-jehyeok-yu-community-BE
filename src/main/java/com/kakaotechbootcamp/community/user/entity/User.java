package com.kakaotechbootcamp.community.user.entity;

import com.kakaotechbootcamp.community.utils.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long userId;

    @Email
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Length(min = 2, max = 10)
    @Column(unique = true, nullable = false, length = 10)
    @Getter
    private String nickname;

    @Column(nullable = false, length = 60)
    @Length(min = 60, max = 60, message = "저장과정에 서버 에러가 발생했습니다.")
    @Getter
    private String password;

    // 팩토리 메서드
    private User(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    static public User create(String email, String nickname, String password) {
        return new User(email, nickname, password);
    }
}
