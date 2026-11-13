package com.ssafy.yumyum.domain.meal.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointNutrionDTO {
	private Integer zeroCount;
	private Long caloriePoint;
	private Long carbohydratePoint;
	private Long proteinPoint;
	private Long fatPoint;
	private Long totalPoint;
}
