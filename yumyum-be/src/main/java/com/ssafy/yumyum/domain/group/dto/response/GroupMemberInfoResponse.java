package com.ssafy.yumyum.domain.group.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberInfoResponse {
    private Long groupMemberId;
    private Long userId;
    private String name;
    private String goalType;
    private Double currentWeight;
    private Double targetWeight;
    private Long mealStreak;
    private Long exerciseStreak;
    private LocalDate lastLogUpdateAt;
}
