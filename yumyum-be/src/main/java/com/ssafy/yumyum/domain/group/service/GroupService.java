package com.ssafy.yumyum.domain.group.service;

import com.ssafy.yumyum.domain.group.dto.request.GroupCreateRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupUpdateRequest;
import com.ssafy.yumyum.domain.group.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface GroupService {

    GroupInfoResponse createGroup(GroupCreateRequest groupCreateRequest, Long userId);

	List<GroupListResponse> getGroupList(Long userId);

	GroupUpdateResponse updateGroupName(GroupUpdateRequest groupUpdateRequest, Long groupId, Long userId);

	void deleteGroup(Long groupId, Long userId);

    List<GroupMemberRankingInfoResponse> getGroupMemberRanking(Long groupId, Long userId);

    List<GroupMemberInfoResponse> getGroupMemberList(Long groupId, Long userId);

    GroupDailyLogResponse getDailyLogs(Long groupId, Long groupMemberId, LocalDate date, Long userId);

    GroupMealDetailResponse getGroupMealDetail(Long groupId, Long mealId, Long userId);

    GroupExerciseDetailResponse getGroupExerciseDetail(Long groupId, Long exerciseRecordId, Long userId);

	GetRecordedDateResponse getRecordedDate(Long groupId, Long groupMemberId, Long userId);
}
