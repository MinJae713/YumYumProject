package com.ssafy.yumyum.domain.badge.repository.badge;

import com.ssafy.yumyum.domain.badge.types.BadgeType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadgeVO {
    private Long badgeId;
    private BadgeType badgeType;
    private String badgeTitle;
    private String description;
    private String imageUrl;
    private Long badgeCategoryId;
}
