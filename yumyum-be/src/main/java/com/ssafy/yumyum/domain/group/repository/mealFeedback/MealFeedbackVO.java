package com.ssafy.yumyum.domain.group.repository.mealFeedback;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealFeedbackVO {
    private Long mealFeedbackId;
    private String content;
    private LocalDateTime createdAt;
    private Long mealId;
    private Long groupMemberId;
}
