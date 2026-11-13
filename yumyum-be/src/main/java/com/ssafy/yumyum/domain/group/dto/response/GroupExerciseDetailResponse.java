package com.ssafy.yumyum.domain.group.dto.response;

import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GroupExerciseDetailResponse {
    private GetExerciseDetailResponse exerciseDetail; // 기존 운동 상세 정보
    private List<FeedbackResponse> feedbacks; // 피드백 리스트
}
