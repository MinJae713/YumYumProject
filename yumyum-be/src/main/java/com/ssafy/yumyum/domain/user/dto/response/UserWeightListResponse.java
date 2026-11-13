package com.ssafy.yumyum.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWeightListResponse {
    private List<WeightLog> weightLogList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeightLog {
        private Long weightLogId;
        private LocalDate logDate;
        private Double weightKg;
    }
}
