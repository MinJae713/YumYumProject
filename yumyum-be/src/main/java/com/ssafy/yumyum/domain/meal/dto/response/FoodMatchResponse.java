package com.ssafy.yumyum.domain.meal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodMatchResponse {
    private Long foodCacheId;
    private String foodName;
    private Long foodId;
    private String status;
}
