package com.ssafy.yumyum.domain.user.repository.weightLog;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeightLogVO {
    private Long weightLogId;
    private LocalDate logDate;
    private Double weightKg;
    private Long userId;
}
