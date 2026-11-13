package com.ssafy.yumyum.domain.meal.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMealOfDateFoodDTO {

    @NotNull
    private Long foodId;

    @NotBlank
    private String name;

    @NotBlank
    private String unitName;

    @PositiveOrZero
    private Double amount; // g 단위
    // 그 음식을 먹은 양에 해당
    // meal item에 있는 amount를 넣을 것
}
