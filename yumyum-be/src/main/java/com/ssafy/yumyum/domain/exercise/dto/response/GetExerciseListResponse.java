package com.ssafy.yumyum.domain.exercise.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetExerciseListResponse {

    @NotNull
    private Long exerciseId;

    @NotNull
    private String exerciseName;
}
