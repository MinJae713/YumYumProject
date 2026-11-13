package com.ssafy.yumyum.domain.user.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserWeightRequest {

    @NotNull(message = "몸무게는 필수 입력 값입니다.")
    @Positive(message = "몸무게는 양수여야 합니다.")
    private Double currentWeight;

    @NotNull(message = "날짜는 필수 입력 값입니다.")
    @PastOrPresent(message = "미래의 날짜는 입력할 수 없습니다.")
    private LocalDate recordDate;
}
