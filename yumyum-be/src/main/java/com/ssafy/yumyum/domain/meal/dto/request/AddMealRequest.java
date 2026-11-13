package com.ssafy.yumyum.domain.meal.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMealRequest {

    @NotNull(message = "식단 날짜는 비어있으면 안됩니다.")
    @PastOrPresent(message = "유효한 날짜를 입력해주세요.")
    private LocalDate mealDate;

//    @NotBlank(message = "식사 시점은 비어있으면 안됩니다.") -> 추천 식단은 null이 들어올 수 있음
    @Pattern(
        regexp = "^(아침|점심|저녁|간식)$",
        message = "식사 시점은 아침, 점심, 저녁, 간식 중 하나여야 합니다."
    )
    private String mealTypeName;

    @NotBlank(message = "식단 생성 주체는 비어있으면 안됩니다.")
    @Pattern(
        regexp = "^(USER|AI|GROUP)$",
        message = "식단 생성 주체는 USER, AI, GROUP 중 하나여야 합니다."
    )
    private String mealCreatorName;
    
    @Positive(message = "그룹원 아이디는 음수면 안됩니다.")
    private Long groupMemberId;

    @PositiveOrZero(message = "점수는 음수면 안됩니다.")
    private Integer totalPoint;

    @NotNull(message = "칼로리는 비어있으면 안됩니다.")
    @PositiveOrZero(message = "칼로리는 음수면 안됩니다.")
    private Double totalCalorie;

    @Valid
    @NotNull(message = "음식 목록은 비어있으면 안됩니다.")
    @Size(min = 1, message = "음식 목록은 최소 하나 이상 있어야 합니다.")
    private List<FoodRequest> foods;

    // ---------------------- FoodRequest ----------------------
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FoodRequest {

        @NotNull(message = "음식 아이디는 비어있으면 안됩니다.")
        @Positive(message = "음식 아이디는 양수여야 합니다.")
        private Long foodId;

        @NotNull(message = "음식 섭취량은 비어있으면 안됩니다.")
        @Positive(message = "음식 섭취량은 양수여야 합니다.")
        private Double amount; 
    }
}

