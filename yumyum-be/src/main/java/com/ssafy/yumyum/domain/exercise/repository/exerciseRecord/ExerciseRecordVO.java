package com.ssafy.yumyum.domain.exercise.repository.exerciseRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseRecordVO {
    private Long exerciseRecordId;
    private LocalDate exerciseDate;
    private Integer durationMin;
    private Double caloriesKcal;
    private Integer points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer setNo;
    private Integer reps;
    private Integer weightKg;
    private Long exerciseId;
    private Long userId;
}
