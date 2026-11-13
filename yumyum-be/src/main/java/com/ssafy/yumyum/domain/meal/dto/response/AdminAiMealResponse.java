package com.ssafy.yumyum.domain.meal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAiMealResponse {
    private Long mealId;
    private Long userId;
    private String userName;        // 유저 이름
    private LocalDate mealDate;     // 식단 날짜
    private Double totalKcal;       // 총 칼로리
    private Double totalCarbo;      // 탄수화물
    private Double totalProtein;    // 단백질
    private Double totalFat;        // 지방
    private List<String> foodNames; // 음식 이름 리스트 (예: ["현미밥", "닭가슴살"])
}
