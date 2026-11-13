package com.ssafy.yumyum.domain.badge.repository.badgeProgress;

import java.time.LocalDate;

import com.ssafy.yumyum.domain.badge.types.StreakType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadgeProgressVO {
    private Long badgeProgressId;
    private StreakType streakType;
    private LocalDate startDate; // 연속 시작 일자
    private Long longestStreak; // 연속 기록 일수
    private LocalDate lastDate; // 연속 종료 일자
    private Long userId;
}
