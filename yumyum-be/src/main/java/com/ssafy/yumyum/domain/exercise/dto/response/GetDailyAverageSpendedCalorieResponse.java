package com.ssafy.yumyum.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDailyAverageSpendedCalorieResponse {
	// 필드명 수정
    private Double spendedAverageCalorie;
    private Double lastWeekSpendedAverageCalorie;
    private Double percentFromLastWeek;
}
