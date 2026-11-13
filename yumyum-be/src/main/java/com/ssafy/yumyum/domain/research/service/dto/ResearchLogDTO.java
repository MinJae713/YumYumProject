package com.ssafy.yumyum.domain.research.service.dto;

import com.ssafy.yumyum.domain.research.types.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchLogDTO {
    private Long researchId;
    private Long userId;
    private LocalDate researchDate;
    private DietPerformance dietPerformance;
    private ExerciseIntensity exerciseIntensity;
    private DigestionState digestionState;
    private TomorrowActivity tomorrowActivity;
    private FreeMeal freeMeal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
