package com.ssafy.yumyum.domain.meal.repository.mealType;

import com.ssafy.yumyum.domain.meal.types.MealType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealTypeVO {
    private Long mealTypeId;
    private MealType mealTypeName;
}
