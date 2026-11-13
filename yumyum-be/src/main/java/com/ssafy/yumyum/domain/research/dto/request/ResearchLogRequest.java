package com.ssafy.yumyum.domain.research.dto.request;

import com.ssafy.yumyum.domain.research.types.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchLogRequest {

    @NotNull(message = "dietPerformance 값은 필수입니다.")
    private DietPerformance dietPerformance;

    @NotNull(message = "exerciseIntensity 값은 필수입니다.")
    private ExerciseIntensity exerciseIntensity;

    @NotNull(message = "digestionState 값은 필수입니다.")
    private DigestionState digestionState;

    @NotNull(message = "tomorrowActivity 값은 필수입니다.")
    private TomorrowActivity tomorrowActivity;

    @NotNull(message = "freeMeal 값은 필수입니다.")
    private FreeMeal freeMeal;
}
