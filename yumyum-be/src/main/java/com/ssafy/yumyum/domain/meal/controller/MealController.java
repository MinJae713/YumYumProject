package com.ssafy.yumyum.domain.meal.controller;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.meal.dto.request.AddMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.ApplyRecommendMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.TotalFoodRequest;
import com.ssafy.yumyum.domain.meal.dto.request.UpdateMealRequest;
import com.ssafy.yumyum.domain.meal.dto.response.AddMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.ApplyRecommendMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.DeleteMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetAiMealListResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.SearchFoodResponse;
import com.ssafy.yumyum.domain.meal.dto.response.TotalFoodResponse;
import com.ssafy.yumyum.domain.meal.dto.response.UpdateMealResponse;
import com.ssafy.yumyum.domain.meal.service.MealAiService;
import com.ssafy.yumyum.domain.meal.service.MealService;
import com.ssafy.yumyum.security.UserPrincipal;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;
	private final MealAiService mealAiService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<GetMealOfDateResponse>> getMealOfDate(
    		@AuthenticationPrincipal UserPrincipal user,
    		//@RequestParam @PastOrPresent(message = "유효한 날짜를 입력해주세요.") LocalDate mealDate) {
			@RequestParam LocalDate mealDate) {	// AI 테스트 용
        // 선택 날짜 식단 조회
    	GetMealOfDateResponse getMealOfDateResponse = 
    			mealService.getMealOfDate(user.getUserId(), mealDate);
    	return ResponseEntity.ok(ApiResponse.ok(getMealOfDateResponse));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<AddMealResponse>> addMeal(
    		@AuthenticationPrincipal UserPrincipal user,
    		@Validated @RequestBody AddMealRequest addMealRequest) {
        // 식단 추가
    	AddMealResponse addMealResponse = 
    			mealService.addMeal(user.getUserId(), addMealRequest);
    	return ResponseEntity.ok(ApiResponse.ok(addMealResponse));
    }
    
    @GetMapping("/{mealId}")
    public ResponseEntity<ApiResponse<GetMealDetailResponse>> getMealDetail(
    		@AuthenticationPrincipal UserPrincipal user,
    		@PathVariable @Positive(message = "식단 아이디는 양수여야 합니다.") Long mealId) {
        // 식단 세부 내용 조회
    	GetMealDetailResponse getMealDetailResponse = 
    			mealService.getMealDetail(mealId);
    	return ResponseEntity.ok(ApiResponse.ok(getMealDetailResponse));
    }
    
    @PatchMapping
    public ResponseEntity<ApiResponse<UpdateMealResponse>> updateMeal(
    		@AuthenticationPrincipal UserPrincipal user,
    		@Validated @RequestBody UpdateMealRequest updateMealRequest) {
        // 식단 수정
    	UpdateMealResponse updateMealResponse = 
    			mealService.updateMeal(updateMealRequest);
    	return ResponseEntity.ok(ApiResponse.ok(updateMealResponse));
    }
    
    @DeleteMapping("/{mealId}")
    public ResponseEntity<ApiResponse<DeleteMealResponse>> deleteMeal(
    		@AuthenticationPrincipal UserPrincipal user,
    		@PathVariable @Positive(message = "식단 아이디는 양수여야 합니다.") Long mealId) {
        // 식단 삭제
    	DeleteMealResponse deleteMealResponse = 
    			mealService.deleteMeal(mealId);
    	return ResponseEntity.ok(ApiResponse.ok(deleteMealResponse));
    }
    
    @PatchMapping("/apply-recommend")
    public ResponseEntity<ApiResponse<ApplyRecommendMealResponse>> applyRecommendMeal(
    		@AuthenticationPrincipal UserPrincipal user,
    		@Validated @RequestBody ApplyRecommendMealRequest applyRecommendMealRequest) {
        // 추천 식단 추가
    	ApplyRecommendMealResponse applyRecommendMealResponse = 
    			mealService.applyRecommendMeal(applyRecommendMealRequest);
    	return ResponseEntity.ok(ApiResponse.ok(applyRecommendMealResponse));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SearchFoodResponse>>> searchFood(
    		@AuthenticationPrincipal UserPrincipal user,
    		@RequestParam String foodName) {
        // 음식 검색 -> 프론트 만들면서 정규표현식 붙일 수도 있음
    	List<SearchFoodResponse> searchFoodResponses = 
    			mealService.searchFood(foodName);
    	return ResponseEntity.ok(ApiResponse.ok(searchFoodResponses));
    }
    
    @PostMapping("/total")
    public ResponseEntity<ApiResponse<TotalFoodResponse>> totalFood(
    		@AuthenticationPrincipal UserPrincipal user,
    		@Validated @RequestBody TotalFoodRequest totalFoodRequests) {
        // 음식 합계 계산
    	TotalFoodResponse totalFoodResponse = 
    			mealService.totalFood(user.getUserId(), totalFoodRequests);
    	return ResponseEntity.ok(ApiResponse.ok(totalFoodResponse));
    }

    @GetMapping("/month-record")
	public ResponseEntity<ApiResponse<List<LocalDate>>> getMonthRecord(
			@AuthenticationPrincipal UserPrincipal user,
			@RequestParam 
			@Range(min = 1900, max = 2100, message = "최소 1900년, 최대 2100년 사이만 가능합니다.")
			Integer year,
			@RequestParam 
			@Range(min = 1, max = 12, message = "최소 1월, 최대 12월 사이만 가능합니다.")
			Integer month) {
		return ResponseEntity.ok(ApiResponse.ok(mealService.getMonthRecord(user.getUserId(), year, month)));
	}
    @PostMapping("/recommend/{targetUserId}")
    public ResponseEntity<ApiResponse<AddMealResponse>> addRecommendMeal(
    		@AuthenticationPrincipal UserPrincipal user,
    		@PathVariable Long targetUserId,
    		@Validated @RequestBody AddMealRequest addMealRequest) {
    	AddMealResponse addMealResponse = 
    			mealService.addMeal(targetUserId, addMealRequest);
    	return ResponseEntity.ok(ApiResponse.ok(addMealResponse));
    }

	/// /////////////////////////////////////////////////////////////////////////
	// ------------------------- AI 기능을 위해 임시 추가 ------------------------- //
	/// /////////////////////////////////////////////////////////////////////////
	@GetMapping("/recommend/ai")
	public ResponseEntity<ApiResponse<GetAiMealListResponse>> getAiMealList(
			@AuthenticationPrincipal UserPrincipal user,
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		GetAiMealListResponse response = mealAiService.getAiMealList(user.getUserId(), date);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}