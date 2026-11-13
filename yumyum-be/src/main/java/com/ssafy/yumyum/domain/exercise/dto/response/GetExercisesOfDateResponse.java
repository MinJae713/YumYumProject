package com.ssafy.yumyum.domain.exercise.dto.response;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetExercisesOfDateResponse {

    @NotNull
    private LocalDate exerciseDate;

    @NotNull
    @PositiveOrZero
    private Double totalCalories;

    @PositiveOrZero
    private Integer totalPoints; 
    // 그룹원이면 지정, 아니면 null
    
    @NotNull
    private Integer totalDurationMin; // 추가
    
    private List<LocalDate> recordedDates; // 추가

    @NotNull
    private List<GetExercisesOfDateExercise> exercises;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetExercisesOfDateExercise {

        @NotNull
        private Long exerciseRecordId;

        @NotNull
        private String exerciseName;

        @NotNull
        private String exerciseCategoryName;

        @NotNull
        @PositiveOrZero
        private Double calories;

        @PositiveOrZero
        private Integer points;

        private String exerciseOptionDetail;
        
        @NotNull
        private Integer durationMin; // 추가
    }
}
