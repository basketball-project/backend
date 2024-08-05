package com.example.basketballmatching.global.service;


import com.example.basketballmatching.global.dto.SendMailResponse;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.example.basketballmatching.global.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {


    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    private final UserRepository userRepository;

    private static final int CODE_LENGTH = 6;

    private static final Long EMAIL_TOKEN_EXPIRATION = 600000L;

    private static final String EMAIL_PREFIX = "Email-Auth: ";


    public SendMailResponse sendAuthMail(String email) {

        String code = createRandomCode();


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("회원가입 이메일 인증코드입니다.");

            String msg = "<div style='margin:20px;'>"
                    + "<h1> 안녕하세요 농구 매칭 서비스입니다. </h1>"
                    + "<br>"
                    + "<p>아래 코드를 입력해주세요<p>"
                    + "<br>"
                    + "<p>감사합니다.<p>"
                    + "<br>"
                    + "<div align='center' style='border:1px solid black; font-family:verdana';>"
                    + "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>"
                    + "<div style='font-size:130%'>"
                    + "CODE : <strong>" + code + "</strong><div><br/> "
                    + "</div>";


            mimeMessageHelper.setText(msg, true);

            javaMailSender.send(mimeMessage);
            redisService.setDataExpire(EMAIL_PREFIX + email, code, EMAIL_TOKEN_EXPIRATION);

        } catch (MessagingException e) {

            throw new CustomException(INTERNAL_SERVER_ERROR);
        }

        return SendMailResponse.builder()
                .email(email)
                .code(code)
                .build();

    }

    public void verifyEmail(String email, String code) {

        if (!isVerify(email, code)) {

            throw new CustomException(INVALID_AUTH_CODE);
        }

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        userEntity.changeEmailAuth();
        userRepository.save(userEntity);

        redisService.deleteData(EMAIL_PREFIX + email);

    }

    private String createRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        while (sb.length() < CODE_LENGTH) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private boolean isVerify(String email, String code) {
        String data = redisService.getData(EMAIL_PREFIX + email);


        if (data == null) {

            throw new CustomException(EMAIL_NOT_FOUND);
        }


        return data.equals(code);
    }




}
