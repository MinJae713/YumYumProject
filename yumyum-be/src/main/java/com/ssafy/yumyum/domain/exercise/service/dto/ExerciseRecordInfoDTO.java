package com.ssafy.yumyum.domain.exercise.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRecordInfoDTO {
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
