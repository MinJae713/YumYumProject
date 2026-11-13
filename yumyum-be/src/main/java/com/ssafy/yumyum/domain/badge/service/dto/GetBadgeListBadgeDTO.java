package com.ssafy.yumyum.domain.badge.service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBadgeListBadgeDTO {

    private Long badgeId;

    private String badgeTitle;

    private String description;

    private String badgeCategoryName;

    private String badgeImageUrl;

    private Boolean isAchieved;

    private LocalDateTime achievedAt;
}
