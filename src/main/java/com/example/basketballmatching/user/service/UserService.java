package com.example.basketballmatching.user.service;

import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.global.service.MailService;
import com.example.basketballmatching.user.dto.UserDto;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static com.example.basketballmatching.global.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginIdAndDeletedAtNull(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public UserDto getUserInfo(String loginId) {
        UserEntity user = userRepository.findByLoginIdAndDeletedAtNull(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return UserDto.fromEntity(user);
    }

    public String findId(String email) {

        UserEntity user = userRepository.findByEmailAndDeletedAtNull(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        return user.getLoginId();
    }

    public boolean findPassword(String loginId) throws NoSuchAlgorithmException {

        UserEntity user = userRepository.findByLoginIdAndDeletedAtNull(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String newPassword = createCertificationNumber();

        String encode = passwordEncoder.encode(newPassword);

        user.setPassword(encode);

        userRepository.save(user);

        String email = user.getEmail();

        boolean isSuccess = mailService.TemporaryPassword(email, newPassword);


        if (!isSuccess) {
            throw new CustomException(MAIL_SEND_FAIL);
        }

        return isSuccess;
    }

    private String createCertificationNumber() throws NoSuchAlgorithmException {

        String certificationNumber;

        int sum = SecureRandom.getInstanceStrong().nextInt(999999);

        certificationNumber = String.format("%06d", sum);

        return certificationNumber;
    }
}
