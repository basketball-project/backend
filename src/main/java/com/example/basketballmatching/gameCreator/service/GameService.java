package com.example.basketballmatching.gameCreator.service;

import com.example.basketballmatching.auth.security.JwtTokenExtract;
import com.example.basketballmatching.auth.security.TokenProvider;
import com.example.basketballmatching.gameCreator.dto.GameDto;
import com.example.basketballmatching.gameCreator.entity.GameEntity;
import com.example.basketballmatching.gameCreator.repository.GameRepository;
import com.example.basketballmatching.gameCreator.util.Util;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.basketballmatching.global.exception.ErrorCode.*;

@Slf4j
@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final JwtTokenExtract jwtTokenExtract;
    /**
     * 경기 생성
     */
    public GameDto.CreateResponse createGame(GameDto.CreateRequest request) throws Exception {
        log.info("createGame start");
        // 이메일로 유저 조회

        Integer userId = jwtTokenExtract.currentUser().getUserId();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        /**
         * 예) 입력한 주소 : 인천광역시 계양구 안남로 544
         *    입력한 경기 시작 시간 : 2024-08-27 T20:30:00
         *    이를 기준으로 기존에 있던 경기 개수를 찾습니다.
         *    주어진 시작 시간에서 30분 전부터 30분 후까지의 시간 범위를 계산하면
         *    2024-08-27T20:00:00 ~ 2024-08-27T21:00:00 까지 입니다.
         *    이 기간 동안 해당 주소에서 예정된 경기를 찾습니다.
         **/
        LocalDateTime startDatetime = request.getStartDateTime();
        LocalDateTime beforeDatetime = startDatetime.minusMinutes(30);
        LocalDateTime afterDateTime = startDatetime.plusMinutes(30);
        LocalDateTime nowDateTime = LocalDateTime.now();

        Long aroundGameCount = this.gameRepository
                .countByStartDateTimeBetweenAndAddressAndDeletedDateTimeNull
                        (beforeDatetime, afterDateTime, request.getAddress())
                .orElse(0L);

        /**
         * 주어진 시작 시간에서 30분 전부터 30분 후까지의 시간 범위를 계산해
         * 시간 범위에 해당하는 해당 주소에서 예정된 경기를 못찾을시
         */
        if(aroundGameCount == 0) {
            /**
             *  예) 현재 시간 : 2024-08-27T18:00:00
             *      입력한 경기 시작 시간 : 2024-08-27T20:30:00
             *      2024-08-27T20:00:00 보다 2024-08-27T20:20:00 이후 이므로
             *      Exception 발생
             */
            if(beforeDatetime.isBefore(nowDateTime)) {
                throw new CustomException(NOT_AFTER_THIRTY_MINUTE);
            }
        } else { // 시간 범위에 해당하는 해당 주소에서 예정된 경기를 찾을시
            /**
             *   시간 범위에 해당하는 해당 주소에서 이미 열린 경기가 있으므로
             *   Exception 발생
             */
            throw new CustomException(ALREADY_GAME_CREATED);
        }



        // 경기 생성
        GameEntity gameEntity = GameDto.CreateRequest.toEntity(request, userEntity);
        gameEntity.setCityName(Util.getCityName(request.getAddress()));
        gameEntity.setCreatedDateTime(LocalDateTime.now());

        this.gameRepository.save(gameEntity);

        log.info("createGame end");

        return GameDto.CreateResponse.toDto(gameEntity);
    }
}
