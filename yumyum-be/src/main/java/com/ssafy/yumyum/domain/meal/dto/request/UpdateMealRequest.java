package com.ssafy.yumyum.domain.meal.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateMealRequest {

    @NotNull(message = "식단 아이디는 비어있으면 안됩니다.")
    @Positive(message = "식단 아이디는 양수여야 합니다.")
    protected Long mealId;

    @PositiveOrZero(message = "점수는 음수면 안됩니다.")
    protected Integer totalPoint;

    @NotNull(message = "칼로리는 비어있으면 안됩니다.")
    @PositiveOrZero(message = "칼로리는 음수면 안됩니다.")
    protected Double totalCalorie;

    @Valid
    @NotNull(message = "음식 목록은 비어있으면 안됩니다.")
    protected List<UpdateFood> updateFoods;

    // ---------------------- UpdateFood ----------------------
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateFood {

    	@NotNull(message = "음식 아이디는 비어있으면 안됩니다.")
        @Positive(message = "음식 아이디는 양수여야 합니다.")
    	protected Long foodId;

        @NotBlank(message = "수정 옵션은 반드시 있어야 합니다.")
        @Pattern(
            regexp = "^(MODIFY|REMOVE|ADD)$",
            message = "수정 옵션은 MODIFY, REMOVE, ADD 중 하나여야 합니다."
        )
        protected String updateType; // MODIFY, REMOVE, ADD

        @Positive(message = "먹은 수치는 음수가 될 수 없습니다.")
        protected Double amount;
        // 그 사람이 먹은 수치
        // REMOVE는 지정 x, MODIFY, ADD 시 수치가 지정됨
    }
}

