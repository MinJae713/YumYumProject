package com.ssafy.yumyum.domain.meal.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodAmountDTO {

	@NotNull(message = "음식 아이디는 비어있으면 안됩니다.")
    @Positive(message = "음식 아이디는 양수여야 합니다.")
	private Long foodId;

	@NotNull(message = "음식 섭취량은 비어있으면 안됩니다.")
    @Positive(message = "음식 섭취량은 반드시 양수여야 합니다.")
    private Double amount; 
    // 그 사람이 실제 먹은 양을 받음
}
