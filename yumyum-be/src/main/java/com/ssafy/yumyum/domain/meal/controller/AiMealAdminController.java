package com.ssafy.yumyum.domain.meal.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.meal.service.AiMealAdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/ai-meal")
@RequiredArgsConstructor
@Slf4j
public class AiMealAdminController {

    private final AiMealAdminService aiMealAdminService;

    // AI 식단 전체 조회 API
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> getAllAiMealList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Admin: Fetching AI Meal List - page: {}, size: {}", page, size);
        Map<String, Object> result = aiMealAdminService.getAllAiMealList(page, size);

        // 프로젝트 공통 응답 포맷(ApiUtils 등)이 있다면 감싸서 반환하세요.
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
