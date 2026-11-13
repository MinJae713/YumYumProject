package com.ssafy.yumyum.domain.meal.repository.mealCreatorType;

import com.ssafy.yumyum.domain.meal.types.MealCreatorType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealCreatorTypeVO {
    private Long mealCreatorTypeId;
    private MealCreatorType mealCreatorTypeName;
}
