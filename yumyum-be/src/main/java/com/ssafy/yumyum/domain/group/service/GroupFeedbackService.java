package com.ssafy.yumyum.domain.group.service;

import com.ssafy.yumyum.domain.group.dto.request.FeedbackCreateRequest;
import com.ssafy.yumyum.domain.group.dto.request.FeedbackUpdateRequest;
import com.ssafy.yumyum.domain.group.dto.response.FeedbackResponse;

import java.util.List;

public interface GroupFeedbackService {
    FeedbackResponse createMealFeedback(Long groupId, FeedbackCreateRequest feedbackCreateRequest, Long userId);

    FeedbackResponse createExerciseFeedback(Long groupId, FeedbackCreateRequest feedbackCreateRequest, Long userId);

    List<FeedbackResponse> getMealFeedback(Long groupId, Long mealId);

    List<FeedbackResponse> getExerciseFeedback(Long groupId, Long exerciseRecordId);

    FeedbackResponse updateMealFeedback(Long groupId, Long mealFeedbackId, FeedbackUpdateRequest feedbackUpdateRequest, Long userId);

    FeedbackResponse updateExerciseFeedback(Long groupId, Long exerciseFeedbackId, FeedbackUpdateRequest feedbackUpdateRequest, Long userId);

    void deleteMealFeedback(Long groupId, Long mealFeedbackId, Long userId);

    void deleteExerciseFeedback(Long groupId, Long exerciseFeedbackId, Long userId);
}
