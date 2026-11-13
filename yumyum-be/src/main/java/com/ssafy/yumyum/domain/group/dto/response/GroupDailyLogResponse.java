package com.ssafy.yumyum.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDailyLogResponse {

    private String userName;
    private String goalType;
    private Double currentWeight;
    private Double targetWeight;

    private List<GroupMealLogDTO> mealLogs;
    private List<GroupExerciseLogDTO> exerciseLogs;
    private Double totalIntakeCalorie;
    private Double totalBurnedCalorie;

    @Getter
    @Builder
    public static class GroupMealLogDTO {
        private Long mealId;
        private String mealType;
        private Double totalCalorie;
        private List<String> foodNames;
        private boolean hasFeedback;
    }

    @Getter
    @Builder
    public static class GroupExerciseLogDTO {
        private Long exerciseRecordId;
        private String exerciseName;
        private Integer durationMin;
        private Double burnedCalorie;
        private boolean hasFeedback;
    }
}
