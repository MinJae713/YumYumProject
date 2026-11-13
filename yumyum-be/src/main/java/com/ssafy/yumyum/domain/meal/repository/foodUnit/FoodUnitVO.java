package com.ssafy.yumyum.domain.meal.repository.foodUnit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodUnitVO {
    private Long foodUnitId;
    private String unitName;
}
