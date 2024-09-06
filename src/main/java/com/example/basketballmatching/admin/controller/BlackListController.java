package com.example.basketballmatching.admin.controller;


import com.example.basketballmatching.admin.dto.BlackListDto;
import com.example.basketballmatching.admin.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blacklist")
public class BlackListController {

    private final BlackListService blackListService;

    @PostMapping("/black-list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveBlackList(@RequestBody BlackListDto request) {

        blackListService.saveBlackList(request);

        return ResponseEntity.ok()
                .body("블랙 리스트 등록 완료");
    }

    @GetMapping("/black-list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBlackList(
            @RequestParam String loginId
    ) {
        blackListService.getBlackList(loginId);

        return ResponseEntity.ok()
                .body("블랙 리스트 유저 확인");
    }



}
