package com.ssafy.yumyum.domain.exercise.repository.exerciseCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseCategoryVO {
    private Long exerciseCategoryId;
    private String exerciseCategoryName;
}
