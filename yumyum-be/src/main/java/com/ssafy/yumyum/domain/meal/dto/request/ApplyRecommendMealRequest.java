package com.ssafy.yumyum.domain.meal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ApplyRecommendMealRequest extends UpdateMealRequest{
	// 추천 식단이면 비로소 meal type을 받음!
	@NotBlank(message = "식사 시점은 비어있으면 안됩니다.") 
    @Pattern(
        regexp = "^(아침|점심|저녁|간식)$",
        message = "식사 시점은 아침, 점심, 저녁, 간식 중 하나여야 합니다."
    )
    private String mealTypeName;
}

