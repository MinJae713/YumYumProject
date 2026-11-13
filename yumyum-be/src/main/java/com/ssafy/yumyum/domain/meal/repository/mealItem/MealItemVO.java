package com.ssafy.yumyum.domain.meal.repository.mealItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealItemVO {
    private Long mealItemId;
    private Double amount;
    private Long mealId;
    private Long foodId;
}
