package com.ssafy.yumyum.domain.group.repository.exerciseFeedback;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseFeedbackVO {
    private Long exerciseFeedbackId;
    private String content;
    private LocalDateTime createdAt;
    private Long exerciseRecordId;
    private Long groupMemberId;
}
