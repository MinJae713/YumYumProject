package com.ssafy.yumyum.domain.badge.repository.userBadge;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBadgeVO {
    private Long userBadgeId;
    private LocalDateTime achievedAt;
    private Long userId;
    private Long badgeId;
}
