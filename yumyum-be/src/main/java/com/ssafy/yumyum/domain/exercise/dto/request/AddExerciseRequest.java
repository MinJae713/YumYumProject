package com.ssafy.yumyum.domain.exercise.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddExerciseRequest {

    @NotNull(message = "운동 날짜는 필수값입니다.")
    @PastOrPresent(message = "유효한 날짜를 입력하세요")
    private LocalDate exerciseDate;

    @NotNull(message = "운동은 반드시 선택해 주세요.")
    @Positive(message = "운동 아이디는 반드시 양수여야 합니다.")
    private Long exerciseId;

    @NotNull(message = "소비 칼로리를 입력해 주세요.")
    @Positive(message = "소비 칼로리는 반드시 양수여야 합니다.")
    private Double calories;

    private Integer points;

    private Integer setNo;

    private Integer reps;

    private Integer weightKg;

    @NotNull(message = "운동 시간은 반드시 입력되어야 합니다.")
    @Positive(message = "운동 시간은 양수여야 합니다.")
    private Integer durationMin;
}
