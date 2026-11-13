package com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseCatalogVO {
    private Long exerciseId;
    private String name;
    private Double metValue;
    private Long exerciseCategoryId;
}
