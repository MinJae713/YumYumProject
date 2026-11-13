package com.ssafy.yumyum.domain.meal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodSearchPageResponse {
    private List<FoodSimpleInfo> foods;
    private int totalCount;
    private int totalPage;
    private int currentPage;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FoodSimpleInfo {
        private Long foodId;
        private String foodName;
        private Double energy;
        private String servingSize;
    }
}
