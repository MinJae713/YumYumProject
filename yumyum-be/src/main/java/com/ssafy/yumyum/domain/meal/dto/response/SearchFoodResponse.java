package com.ssafy.yumyum.domain.meal.dto.response;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchFoodResponse {

    @NotNull
    private Long foodId;

    @NotBlank
    private String name;

    @NotBlank
    private String unitName;

    @PositiveOrZero
    private Double amount;
    // 음식 DB에 있는 단위 기준당 수치로 들어옴
    // 바꿀 것은 크게 없을 듯?

    @PositiveOrZero
    private Double calorie;

    @PositiveOrZero
    private Double carbohydrate;

    @PositiveOrZero
    private Double protein;

    @PositiveOrZero
    private Double fat;
}

