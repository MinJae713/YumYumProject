package com.ssafy.yumyum.domain.research.controller;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.research.dto.request.ResearchLogRequest;
import com.ssafy.yumyum.domain.research.service.ResearchLogService;
import com.ssafy.yumyum.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/research")
public class ResearchController {

    private final ResearchLogService researchLogService;

    /**
     *  데일리  리서치 입력
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<String>> submitResearchLog(@Validated @RequestBody ResearchLogRequest researchLogRequest, @AuthenticationPrincipal UserPrincipal user) {
        researchLogService.submitResearchLog(researchLogRequest, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ok("데일리 리서치가 정상적으로 제출되었습니다."));
    }

}
