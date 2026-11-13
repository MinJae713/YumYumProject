package com.ssafy.yumyum.domain.badge.repository.badgeCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadgeCategoryVO {
    private Long badgeCategoryId;
    private String badgeCategoryName;
}
