package com.example.basketballmatching.user.controller;

import com.example.basketballmatching.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/find/id")
    public ResponseEntity<String> findLoginId (
            @RequestParam String email
    ){

        String loginId = userService.findId(email);

        return ResponseEntity.ok(loginId);
    }

    @PatchMapping("/find/password")
    public ResponseEntity<Boolean> findPassword(@RequestParam String loginId) throws NoSuchAlgorithmException {

        boolean success = userService.findPassword(loginId);

        return ResponseEntity.ok(success);
    }
}
