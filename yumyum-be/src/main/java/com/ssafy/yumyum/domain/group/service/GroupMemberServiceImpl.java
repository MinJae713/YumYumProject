package com.ssafy.yumyum.domain.group.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.common.exception.commonExceptions.DeleteFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.group.dto.request.GroupInviteActionRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupInviteRequest;
import com.ssafy.yumyum.domain.group.dto.response.GroupInviteListResponse;
import com.ssafy.yumyum.domain.group.exception.GroupInviteAlreadyExistsException;
import com.ssafy.yumyum.domain.group.exception.GroupInviteAlreadyResponseException;
import com.ssafy.yumyum.domain.group.exception.GroupInviteNoFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupMemberAlreadyExistsException;
import com.ssafy.yumyum.domain.group.exception.GroupMemberNotFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupMemberScoreUpdateException;
import com.ssafy.yumyum.domain.group.exception.GroupNotFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupPermissionDeniedException;
import com.ssafy.yumyum.domain.group.repository.group.GroupMapper;
import com.ssafy.yumyum.domain.group.repository.group.GroupVO;
import com.ssafy.yumyum.domain.group.repository.groupInvite.GroupInviteMapper;
import com.ssafy.yumyum.domain.group.repository.groupInvite.GroupInviteVO;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberMapper;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberVO;
import com.ssafy.yumyum.domain.group.service.dto.GroupInfoDTO;
import com.ssafy.yumyum.domain.group.types.GroupRole;
import com.ssafy.yumyum.domain.group.types.InviteStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupInviteMapper groupInviteMapper;
    private final GroupMemberMapper groupMemberMapper;
    private final GroupMapper groupMapper;

    @Override
    @Transactional
    public boolean updateGroupMemberPoint(Long groupMemberId, Integer benefit) {
        ////// 그룹원 점수 수정 (수정 로직이 수정됨) /////
        /// 그룹원 테이블은 추천에 따른 benefit 점수를 받음
        /// 해당 benefit 점수는 benefit 점수 변화량
        /// 즉, 그룹장 추천 식단 선택이면 benefit 만큼 +
        /// 그룹장 추천 식단 선택 삭제면 benefit 만큼 -가 됨
    	GroupMemberVO groupMemberVO = groupMemberMapper.findById(groupMemberId)
    			.orElseThrow(GroupMemberNotFoundException::new);
    	groupMemberVO.setPoints(groupMemberVO.getPoints()+benefit);
    	if (groupMemberMapper.updateById(groupMemberVO) < 1)
          throw new GroupMemberScoreUpdateException();
        return true;
    }

    @Override
    @Transactional
    public boolean checkInGroup(Long userId) {
        // 이거도 한 번만 만들게...!!
        // 운동, 식단에서 그룹 유저인지 확인하는게 있어서..
        List<GroupMemberVO> groupMemberVOs =
                groupMemberMapper.findAllByUserId(userId);
        return !(groupMemberVOs == null || groupMemberVOs.isEmpty());
    }

    @Override
    @Transactional
    public void inviteGroupMember(GroupInviteRequest groupInviteRequest, Long groupId, Long userId) {

        Long inviteUserID = groupInviteRequest.getUserId();
        // 현재 유저 groupMember 정보 받아오기
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupNotFoundException::new);

        // 현재 유저가 leader인지 확인
        if(!groupMemberVO.getRole().equals("LEADER")){
            throw new GroupPermissionDeniedException();
        }

        // 초대하고자 하는 유저가 그룹멤버에 있는지 확인
        if(groupMemberMapper.existsByGroupIdAndUserId(groupId, inviteUserID)){
            // 이미 그룹에 가입된 유저입니다.
            throw new GroupMemberAlreadyExistsException();
        }

        // 초대하고자 하는 유저한테 보낸 초대가 이미 있는지 확인
        if(groupInviteMapper.existPendingByGroupIdAndUserId(groupId, inviteUserID)){
            // 이미 요청을 보냈습니다.
            throw new GroupInviteAlreadyExistsException();
        }
        // 없다면 invite 생성
        GroupInviteVO groupInviteVO = GroupInviteVO.builder()
                .status(InviteStatus.PENDING.name())
                .sentAt(LocalDateTime.now())
                .userId(inviteUserID)
                .groupId(groupId)
                .build();

        groupInviteMapper.insert(groupInviteVO);
    }

    @Override
    @Transactional
    public List<GroupInviteListResponse> getInviteList(Long userId) {
        List<GroupInviteListResponse> inviteList = groupInviteMapper.findPendingInvitesByUserId(userId);

        if(inviteList == null || inviteList.isEmpty()){
            return new ArrayList<>();
        }
        return inviteList;
    }

    @Override
    @Transactional
    public String responseToInvite(GroupInviteActionRequest groupInviteActionRequest, Long userId) {
        // invite 정보 받아오기
        GroupInviteVO groupInviteVO = groupInviteMapper.findById(groupInviteActionRequest.getGroupInviteId())
                .orElseThrow(GroupInviteNoFoundException::new);

        // invite에 있는 유저정보와 현재 로그인 유저가 같은지 확인
        if(!groupInviteVO.getUserId().equals(userId)){
            throw new GroupPermissionDeniedException();
        }

        // 해당 invite가 pending상태인지 확인
        if(!groupInviteVO.getStatus().equals(InviteStatus.PENDING.name())){
            throw new GroupInviteAlreadyResponseException();
        }

        String responseMessage;
        // accepted 정보에 따라
        if(groupInviteActionRequest.isAccept()){  // 수락
            // 멤버 등록
            insertGroupMember(groupInviteVO, userId);
            // invite값 accepted로 변경
            groupInviteVO.setStatus(InviteStatus.ACCEPTED.name());
            responseMessage = "초대를 수락하였습니다.";
        }else{  // 거절
            // invite값 rejected로 변경
            groupInviteVO.setStatus(InviteStatus.REJECTED.name());
            responseMessage = "초대를 거절하였습니다.";
        }

        // responseAt 값 지금으로 변경
        groupInviteVO.setRespondedAt(LocalDateTime.now());
        // 변경된 정보 반영
        if(groupInviteMapper.updateById(groupInviteVO) != 1){
            throw new UpdateFailedException("초대 목록 업데이트에 실패했습니다");
        }

        return responseMessage;
    }

    @Override
    @Transactional
    public void withdrawGroupMember(Long groupId, Long userId) {
        // 그룹멤버정보 조회
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // 현재 로그인 유저가 Leader면 탈퇴 불가
        if(GroupRole.LEADER.name().equals(groupMemberVO.getRole())){
            throw new GroupPermissionDeniedException();
        }

        if(groupMemberMapper.softDeleteById(groupMemberVO.getGroupMemberId()) != 1 ){
            throw new DeleteFailedException("그룹 탈퇴 처리에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void expelGroupMember(Long groupId, Long groupMemberId, Long userId) {
        // 그룹멤버정보 조회 - Leader
        GroupMemberVO leaderMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // 현재 로그인 유저가 Leader가 아니면 강퇴 불가
        if(!GroupRole.LEADER.name().equals(leaderMemberVO.getRole())){
            throw new GroupPermissionDeniedException();
        }

        // 그룹멤버정보 조회 - Member
        GroupMemberVO targetMemberVO = groupMemberMapper.findById(groupMemberId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // 찾은 멤버가 현재 그룹의 멤버가 맞는지 확인
        if(!targetMemberVO.getGroupId().equals(groupId)){
            throw new GroupPermissionDeniedException();
        }

        // 찾은 멤버가 Leader인지 검증
        if(targetMemberVO.getRole().equals("LEADER")){
            throw new GroupPermissionDeniedException();
        }

        // SoftDelete
        if(groupMemberMapper.softDeleteById(groupMemberId) != 1){
            throw new UpdateFailedException("그룹 멤버 강퇴 처리에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public Integer getBestRanking(Long userId) {
        // 유저의 모든 그룹 활동 중 최고 랭킹 조회
        // 최고 등수 (1이 1등), 없으면 null
        return groupMemberMapper.findBestRankingByUserId(userId);
    }


    private void insertGroupMember(GroupInviteVO groupInviteVO, Long userId){

        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserIdWithInActive(groupInviteVO.getGroupId(), userId).orElse(null);

        // 이미 존재함
        if(groupMemberVO != null){
            if(groupMemberVO.getIsActive()){
                // 이미 활성화 완료
                throw new GroupMemberAlreadyExistsException();
            }else{
                // 해당 그룹에 이전에 존재했던 멤버이므로 isActive를 1로 업데이트
                if(groupMemberMapper.rejoinGroupMember(groupMemberVO.getGroupMemberId()) < 1){
                    throw new UpdateFailedException("그룹 멤버 활성화에 실패했습니다.");
                }
                return;
            }

        }else{  // 처음 등록되는 멤버
            groupMemberVO = GroupMemberVO.builder()
                    .role(GroupRole.MEMBER.name())
                    .isActive(true)
                    .points(0)
                    .userId(userId)
                    .groupId(groupInviteVO.getGroupId())
                    .build();

            if(groupMemberMapper.insert(groupMemberVO) != 1){
                throw new InsertFailedException("그룹 멤버 추가에 실패했습니다");
            }
        }
    }

	@Override
	public boolean checkValidMember(Long groupMemberId, Long userId) {
		// 민재가 만듬
		// 식단에서 groupMemberId 받아올 때 유효한 멤버인지 확인
		// 여기서 확인하는게 맞는 것 같아서 만들었어
		// 충돌날 수도 있음
		if (groupMemberId == null || userId == null) return false;
		GroupMemberVO groupMemberVO = 
				groupMemberMapper.findById(groupMemberId).orElse(null);
		return groupMemberVO != null && groupMemberVO.getUserId() == userId;
	}

	@Override
	public GroupInfoDTO getGroupInfoDTOByGroupMemberId(Long groupMemberId) {
		GroupMemberVO groupMemberVO = groupMemberMapper.findById(groupMemberId)
				.orElseThrow(GroupMemberNotFoundException::new);
		GroupVO groupVO = groupMapper.findById(groupMemberVO.getGroupId())
				.orElseThrow(GroupNotFoundException::new);
		return GroupInfoDTO.builder()
				.groupName(groupVO.getGroupName())
				.userId(groupVO.getUserId())
				.build();
	}
}
