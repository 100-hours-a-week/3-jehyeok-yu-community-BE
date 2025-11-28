package com.kakaotechbootcamp.community.user.service;

import com.kakaotechbootcamp.community.auth.exception.UserNotFoundException;
import com.kakaotechbootcamp.community.user.dto.request.SignUpRequestDto;
import com.kakaotechbootcamp.community.user.dto.response.SignUpResponseDto;
import com.kakaotechbootcamp.community.user.dto.response.UserDetailResponseDto;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.user.exception.DuplicateException;
import com.kakaotechbootcamp.community.user.exception.UserErrorCode;
import com.kakaotechbootcamp.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public SignUpResponseDto createUser(SignUpRequestDto dto) {
        checkDuplicateAtUserEmail(dto.getEmail());
        checkDuplicateAtUserNickname(dto.getNickname());
        User user;
        if (dto.getImage() == null) {
            user = User.create(dto.getEmail(), dto.getNickname(),
                encoder.encode(dto.getPassword()));
        } else {
            user = User.create(dto.getEmail(), dto.getNickname(),
                encoder.encode(dto.getPassword()), dto.getImage());
        }

        userRepository.save(user);

        System.out.println(dto.getImage());
        System.out.println(dto);
        return new SignUpResponseDto(user.getUserId());
    }

    @Transactional(readOnly = true)
    public void checkDuplicateAtUserEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException(UserErrorCode.DUPLICATE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public void checkDuplicateAtUserNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateException(UserErrorCode.DUPLICATE_ERROR);
        }
    }

    public UserDetailResponseDto getUserDetail(Long userId) {
        return userRepository.findById(userId)
            .map(UserDetailResponseDto::from)
            .orElseThrow(UserNotFoundException::new);
    }
}
