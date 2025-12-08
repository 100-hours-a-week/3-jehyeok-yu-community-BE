package com.kakaotechbootcamp.community.user.service;

import com.kakaotechbootcamp.community.auth.exception.UserNotFoundException;
import com.kakaotechbootcamp.community.image.S3ClientCreator;
import com.kakaotechbootcamp.community.image.dto.response.PresignedUrlDto;
import com.kakaotechbootcamp.community.image.entity.Image;
import com.kakaotechbootcamp.community.user.dto.request.SignUpRequestDto;
import com.kakaotechbootcamp.community.user.dto.request.UserUpdatePasswordRequestDto;
import com.kakaotechbootcamp.community.user.dto.request.UserUpdateRequestDto;
import com.kakaotechbootcamp.community.user.dto.response.SignUpResponseDto;
import com.kakaotechbootcamp.community.user.dto.response.UserDetailResponseDto;
import com.kakaotechbootcamp.community.user.dto.response.UserProfileDetailResponseDto;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.user.entity.UserImage;
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
    private final S3ClientCreator s3ClientCreator;

    public SignUpResponseDto createUser(SignUpRequestDto dto) {
        checkDuplicateAtUserEmail(dto.getEmail());
        checkDuplicateAtUserNickname(dto.getNickname());

        Image profileImage = null;
        String thumbnailObjectKey = null;

        if (dto.getImage() != null) {
            profileImage = Image.create(
                dto.getImage().getOriginalName(),
                dto.getImage().getObjectKey()
            );
            thumbnailObjectKey = dto.getImage().getThumbnailObjectKey();
        }

        User user = User.create(
            dto.getEmail(),
            dto.getNickname(),
            encoder.encode(dto.getPassword()),
            profileImage,
            thumbnailObjectKey
        );

        userRepository.save(user);
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

    @Transactional(readOnly = true)
    public UserDetailResponseDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String objectKey = user.getObjectKey();
        String presignedGetUrl;
        if (objectKey == null) {
            String defaultKey = "public/image/profile/default-profile.png";
            presignedGetUrl = s3ClientCreator.getPresignedGetUrl(defaultKey);
        } else {
            presignedGetUrl = s3ClientCreator.getPresignedGetUrl(objectKey);
        }
        return new UserDetailResponseDto(user.getUserId(), user.getEmail(), user.getNickname(),
            presignedGetUrl);
    }

    @Transactional(readOnly = true)
    public UserProfileDetailResponseDto getProfileDetail(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String objectKey = user.getObjectKey();
        String presignedGetUrl = objectKey;
        if (objectKey != null) {
            presignedGetUrl = s3ClientCreator.getPresignedGetUrl(objectKey);
        }
        return new UserProfileDetailResponseDto(user.getUserId(), user.getEmail(),
            user.getNickname(),
            presignedGetUrl, objectKey);
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateRequestDto req) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!req.getNickname().equals(user.getNickname())) {
            checkDuplicateAtUserNickname(req.getNickname());
        }

        user.changeNickname(req.getNickname());
        if (req.getImage() == null) {
            return;
        }
        UserImage userImage = user.getUserImage();
        if (userImage == null) {
            Image image = Image.create(
                req.getImage().getOriginalName(),
                req.getImage().getObjectKey()
            );
            user.addProfileImage(image, image.getObjectKey());
        } else {
            userImage.changeOriginalName(req.getImage().getOriginalName());
        }

        s3ClientCreator.evictCache(user.getObjectKey());
    }

    public PresignedUrlDto getPatchUrl(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        return user.getObjectKey() == null ? s3ClientCreator.getPutPresignedUrl("profile")
            : s3ClientCreator.getPutPresignedUrl("profile", user.getObjectKey());
    }

    @Transactional
    public void updatePassword(Long userId, UserUpdatePasswordRequestDto request) {
        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        user.changePassword(encoder.encode(request.getPassword()));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
