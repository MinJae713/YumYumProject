package com.ssafy.yumyum.domain.meal.controller;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.meal.dto.request.FoodMatchRequest;
import com.ssafy.yumyum.domain.meal.dto.response.FoodMatchResponse;
import com.ssafy.yumyum.domain.meal.dto.response.FoodSearchPageResponse;
import com.ssafy.yumyum.domain.meal.service.FoodMatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/food")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FoodAdminController {

    private final FoodMatchingService foodMatchingService;

    @GetMapping("/pendingList")
    public ResponseEntity<ApiResponse<List<FoodMatchResponse>>> getPendingList(){
        return ResponseEntity.ok(ApiResponse.ok(foodMatchingService.getPendingList()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<FoodSearchPageResponse>> searchFoodList(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(ApiResponse.ok(foodMatchingService.searchFoodList(keyword, page, size)));
    }

    @PostMapping("/match")
    public ResponseEntity<ApiResponse<String>> matchFood(@RequestBody @Valid List<FoodMatchRequest> foodMatchRequestList){
        int count = foodMatchingService.matchFoods(foodMatchRequestList);
        return ResponseEntity.ok(ApiResponse.ok(count + " 건의 음식이 매칭되었습니다"));
    }
}
