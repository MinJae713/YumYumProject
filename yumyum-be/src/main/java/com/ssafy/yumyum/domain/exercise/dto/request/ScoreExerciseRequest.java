package com.ssafy.yumyum.domain.exercise.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreExerciseRequest {

	@NotNull(message = "운동은 반드시 선택해 주세요.")
	@Positive(message = "운동 아이디는 반드시 양수여야 합니다.")
    private Long exerciseId;

    @NotNull(message = "운동 시간은 반드시 입력되어야 합니다.")
    @Positive(message = "운동 시간은 양수여야 합니다.")
    private Integer durationMin;
}
