package com.ssafy.yumyum.domain.meal.dto.request;

import java.util.List;

import com.ssafy.yumyum.domain.meal.service.dto.FoodAmountDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TotalFoodRequest {

    @NotNull(message = "간식 여부는 비어있으면 안됩니다.")
    private Boolean isSnack;
    
    @Valid
    @NotNull(message = "음식 목록은 비어있으면 안됩니다.")
    @Size(min = 1, message = "음식 목록은 최소 하나 이상 있어야 합니다.")
    private List<FoodAmountDTO> foods;
}

