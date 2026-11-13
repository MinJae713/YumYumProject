package com.ssafy.yumyum.domain.group.service;

import java.util.List;

import com.ssafy.yumyum.domain.group.dto.request.GroupInviteActionRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupInviteRequest;
import com.ssafy.yumyum.domain.group.dto.response.GroupInviteListResponse;
import com.ssafy.yumyum.domain.group.service.dto.GroupInfoDTO;

public interface GroupMemberService {

    boolean updateGroupMemberPoint(Long groupMemberId, Integer benefit);

    boolean checkInGroup(Long userId);

    void inviteGroupMember(GroupInviteRequest groupInviteRequest, Long groupId, Long userId);

    List<GroupInviteListResponse> getInviteList(Long userId);

    String responseToInvite(GroupInviteActionRequest groupInviteActionRequest, Long userId);
    // 본인 탈퇴
    void withdrawGroupMember(Long groupId, Long userId);
    // 강퇴
    void expelGroupMember(Long groupId, Long groupMemberId, Long userId);

    Integer getBestRanking(Long userId);
    
    boolean checkValidMember(Long groupMemberId, Long userId);
    
    GroupInfoDTO getGroupInfoDTOByGroupMemberId(Long groupMemberId);
    // 순환 참조 때문에 일단 여기 만듬
}
