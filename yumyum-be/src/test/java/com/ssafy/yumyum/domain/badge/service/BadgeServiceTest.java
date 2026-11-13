package com.ssafy.yumyum.domain.badge.service;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.badge.dto.response.GetBadgeListResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;
import com.ssafy.yumyum.domain.badge.types.CommandType;

@SpringBootTest
public class BadgeServiceTest {
	@Autowired
	private BadgeService badgeService;
	
	@Test
	@DisplayName("getBadgeList 테스트")
	void getBadgeList() {
		Long userId = 11L;
		GetBadgeListResponse getBadgeListResponse = badgeService.getBadgeList(userId);

	    StringBuffer buffer = new StringBuffer("============================\n");
	    buffer.append(String.format("userId: %d\n", userId));
	    buffer.append(String.format("fullCount: %d\n", getBadgeListResponse.getFullCount()));
	    buffer.append(String.format("achievedCount: %d\n", getBadgeListResponse.getAchievedCount()));
	    buffer.append(String.format("achievedRate: %.2f\n", getBadgeListResponse.getAchievedRate()));

	    buffer.append("badges: [\n");
	    getBadgeListResponse.getBadges().forEach(badge -> {
	        buffer.append("\t{\n");
	        buffer.append(String.format("\t\tbadgeId: %d\n", badge.getBadgeId()));
	        buffer.append(String.format("\t\tbadgeTitle: %s\n", badge.getBadgeTitle()));
	        buffer.append(String.format("\t\tdescription: %s\n", badge.getDescription()));
	        buffer.append(String.format("\t\tbadgeCategoryName: %s\n", badge.getBadgeCategoryName()));
	        buffer.append(String.format("\t\tbadgeImageUrl: %s\n", badge.getBadgeImageUrl()));
	        buffer.append(String.format("\t\tisAchieved: %s\n", badge.getIsAchieved()));
	        buffer.append(String.format("\t\tachievedAt: %s\n", badge.getAchievedAt()));
	        buffer.append("\t}\n");
	    });
	    buffer.append("]");

	    System.out.println(buffer);
	}

	@Test
	@DisplayName("getRecentBadgeList 테스트")
	void getRecentBadgeList() {
		Long userId = 11L;
		List<GetBadgeListBadgeDTO> getBadgeListBadge = 
				badgeService.getRecentBadgeList(userId);
		StringBuffer buffer = new StringBuffer("============================\n");
	    buffer.append("[\n");

	    getBadgeListBadge.forEach(badge -> {
	        buffer.append("\t{\n");
	        buffer.append(String.format("\t\tbadgeId: %d\n", badge.getBadgeId()));
	        buffer.append(String.format("\t\tbadgeTitle: %s\n", badge.getBadgeTitle()));
	        buffer.append(String.format("\t\tdescription: %s\n", badge.getDescription()));
	        buffer.append(String.format("\t\tbadgeCategoryName: %s\n", badge.getBadgeCategoryName()));
	        buffer.append(String.format("\t\tbadgeImageUrl: %s\n", badge.getBadgeImageUrl()));
	        buffer.append(String.format("\t\tisAchieved: %s\n", badge.getIsAchieved()));
	        buffer.append(String.format("\t\tachievedAt: %s\n", badge.getAchievedAt()));
	        buffer.append("\t}\n");
	    });

	    buffer.append("]");

	    System.out.println(buffer);
	}
	
	@Test
	@DisplayName("getStreakRecord 테스트")
	void getStreakRecord() {
		List<GetStreakRecordResponse> getStreakRecordResponses = 
				badgeService.getStreakRecord(13L);
		StringBuffer buffer = new StringBuffer("============================\n");
	    buffer.append("[\n");
	    getStreakRecordResponses.forEach(response -> {
	    	buffer.append("\t{\n");
	    	buffer.append(String.format("\t\tstreakType: %s\n", response.getStreakType()));
	    	buffer.append(String.format("\t\tname: %s\n", response.getName()));
	    	buffer.append(String.format("\t\tlongestStreak: %d\n", response.getLongestStreak()));
	    	buffer.append("\t}\n");
	    });
	    buffer.append("]");
	    System.out.println(buffer);
	}

	@Test
	@DisplayName("checkBadge 테스트")
	void checkBadge() {
		Long userId = 11L;
		CommandType commandType = CommandType.ADD_MEAL;
		badgeService.checkBadge(userId, commandType);
	}
}
