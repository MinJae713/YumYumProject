package com.ssafy.yumyum.domain.meal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodMatchRequest {
    @NotBlank(message = "AI 음식 이름(aiFoodName)은 필수입니다.")
    private String aiFoodName;

    @NotNull(message = "매칭할 음식 ID(foodId)는 필수입니다.")
    private Long foodId;
}
