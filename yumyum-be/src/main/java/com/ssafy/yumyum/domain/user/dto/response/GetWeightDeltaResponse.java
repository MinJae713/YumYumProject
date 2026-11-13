package com.ssafy.yumyum.domain.user.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetWeightDeltaResponse {

    private Double startWeight;

    private Double currentWeight;

    private Double lossWeight;

    private Double targetWeight;

    private List<WeightRecordDTO> dateOfWeight;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeightRecordDTO {

        private LocalDate recordedDate;

        private Double weight;
    }
}
