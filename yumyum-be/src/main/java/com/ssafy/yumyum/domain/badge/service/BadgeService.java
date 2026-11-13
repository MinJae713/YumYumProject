package com.ssafy.yumyum.domain.badge.service;

import java.util.List;

import com.ssafy.yumyum.domain.badge.dto.response.CheckBadgeResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetBadgeListResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;
import com.ssafy.yumyum.domain.badge.types.CommandType;
import com.ssafy.yumyum.domain.badge.types.StreakType;

public interface BadgeService {
	GetBadgeListResponse getBadgeList(Long userId);
	List<GetBadgeListBadgeDTO> getRecentBadgeList(Long userId);
	List<GetStreakRecordResponse> getStreakRecord(Long userId);
	CheckBadgeResponse checkBadge(Long userId, CommandType commandType);
	void undoStreak(Long userId, StreakType streakType);
}
