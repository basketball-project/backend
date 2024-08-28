package com.example.basketballmatching.gameCreator.controller;


import com.example.basketballmatching.gameCreator.dto.GameDto;
import com.example.basketballmatching.gameCreator.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
    @RestController
    @RequestMapping("/api/game-creator")
    @RequiredArgsConstructor
    public class GameController {

        private final GameService gameService;

        /**
         * 게임 생성
         */
        @PostMapping("/game/create")
        @PreAuthorize("hasRole('ROLE_USER')")
        public ResponseEntity<?> createGame(@RequestBody @Validated GameDto.CreateRequest request
                                            ) throws Exception {
            GameDto.CreateResponse result = this.gameService.createGame(request);
            return ResponseEntity.ok(result);
        }


}
