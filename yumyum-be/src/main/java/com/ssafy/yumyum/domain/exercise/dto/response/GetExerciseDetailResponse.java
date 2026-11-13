package com.ssafy.yumyum.domain.exercise.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetExerciseDetailResponse {

    @NotNull
    private List<ExerciseCategoryInfo> exerciseCategoryList;

    @NotNull
    private Integer exerciseCategoryIndex;

    @NotNull
    private List<GetExerciseListResponse> exerciseList;

    @NotNull
    private Integer exerciseIndex;

    @NotNull
    @PositiveOrZero
    private Double calories;

    @PositiveOrZero
    private Integer points;

    private Integer setNo;

    private Integer reps;

    private Integer weightKg;

    private Integer durationMin;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExerciseCategoryInfo {

        @NotNull
        private Long exerciseCategoryId;

        @NotNull
        private String exerciseCategoryName;
    }
}
