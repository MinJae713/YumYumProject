package com.ssafy.yumyum.domain.badge.dto.response;

import java.util.List;

import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBadgeListResponse {

    private Integer fullCount;

    private Integer achievedCount;

    private Double achievedRate;

    private List<GetBadgeListBadgeDTO> badges;
}
