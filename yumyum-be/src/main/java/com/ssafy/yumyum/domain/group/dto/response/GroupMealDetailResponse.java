package com.ssafy.yumyum.domain.group.dto.response;

import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GroupMealDetailResponse {
    private GetMealDetailResponse mealDetail; // 기존 식단 상세 정보
    private List<FeedbackResponse> feedbacks; // 피드백 리스트
}
