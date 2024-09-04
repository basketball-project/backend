package com.example.basketballmatching.admin.controller;


import com.example.basketballmatching.admin.dto.BlackListDto;
import com.example.basketballmatching.admin.service.BlackListService;
import com.example.basketballmatching.global.commonResponse.ApiResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blacklist")
public class BlackListController {

    private final BlackListService blackListService;
    private final ApiResponseFactory apiResponseFactory;

    @PostMapping("/black-list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveBlackList(@RequestBody BlackListDto request) {

        blackListService.saveBlackList(request);

        return ResponseEntity.ok().body(
                apiResponseFactory.createSuccessResponse("블랙리스트 등록")
        );
    }

    @GetMapping("/black-list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBlackList(
            @RequestParam String loginId
    ) {
        blackListService.getBlackList(loginId);

        return ResponseEntity.ok()
                .body(apiResponseFactory.createSuccessWithDetailResponse(
                        "블랙리스트", "true"
                ));
    }



}
