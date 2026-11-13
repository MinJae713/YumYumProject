package com.ssafy.yumyum.domain.badge.dto.response;

import com.ssafy.yumyum.domain.badge.types.StreakType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStreakRecordResponse {
	private StreakType streakType;
	private String name;
	private Long longestStreak;
}
