package com.example.basketballmatching.gameUsers.controller;


import com.example.basketballmatching.gameCreator.type.CityName;
import com.example.basketballmatching.gameCreator.type.FieldStatus;
import com.example.basketballmatching.gameCreator.type.Gender;
import com.example.basketballmatching.gameCreator.type.MatchFormat;
import com.example.basketballmatching.gameUsers.dto.GameSearchDto;
import com.example.basketballmatching.gameUsers.dto.UserCancelGameDto;
import com.example.basketballmatching.gameUsers.dto.UserJoinGameDto;
import com.example.basketballmatching.gameUsers.service.GameUserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game-user")
public class GameUserController {

    private final GameUserService gameUserService;

    @GetMapping("/search")
    public ResponseEntity<Page<GameSearchDto>> findFilteredGame(
            @RequestParam LocalDate localDate,
            @RequestParam(required = false) CityName cityName,
            @RequestParam(required = false) FieldStatus fieldStatus,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) MatchFormat matchFormat,
            @RequestParam(value = "page", defaultValue = "0") @Positive int page,
            @RequestParam(value = "size", defaultValue = "5") @Positive int size
            ) {

        return ResponseEntity.ok(
                gameUserService.findFilteredGame(localDate, cityName, fieldStatus, gender, matchFormat, page, size));
    }



    @GetMapping("/search-address")
    public ResponseEntity<Page<GameSearchDto>> searchAddress(
            @RequestParam String address,
            @RequestParam(value = "page", defaultValue = "0") @Positive int page,
            @RequestParam(value = "size", defaultValue = "5") @Positive int size
    ) {
        return ResponseEntity.ok(
                gameUserService.searchAddress(address, page, size)
        );
    }


    @PostMapping("/participant")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserJoinGameDto.Response participantGame(
            @RequestBody @Validated UserJoinGameDto.Request request
    ) {
        return UserJoinGameDto.Response.from(
                gameUserService.participantGame(request.getGameId())
        );
    }

    @PostMapping("/participant/cancel")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserCancelGameDto.Response participantCancelGame(
            @RequestBody @Validated UserCancelGameDto.Request request
    ) {
        return UserCancelGameDto.Response.from(
                gameUserService.participantCancelGame(request.getGameId())
        );
    }

}
