package com.ssafy.yumyum.domain.meal.repository.foodCache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCacheVO {
    private Long foodCacheId;
    private String foodName;    // AI에서 나온 음식 이름
    private Long foodId;        // AI에서 나온 음식 이름과 매칭시킬 foodId
    private String status;      // PENDING(매칭 전), MATCHED(매칭 완)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
