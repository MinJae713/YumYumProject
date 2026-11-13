package com.ssafy.yumyum.domain.research.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssafy.yumyum.domain.research.types.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ResearchLogVO {
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
