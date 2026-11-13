package com.ssafy.yumyum.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetWeeklyTotalDurationResponse {

    private Integer exerciseTimeSameWeek;
    private Integer exerciseTimeLastWeek;
    private Double percentFromLastWeek;
    
}
