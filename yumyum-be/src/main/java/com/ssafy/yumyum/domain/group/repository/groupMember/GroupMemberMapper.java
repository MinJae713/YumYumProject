package com.ssafy.yumyum.domain.group.repository.groupMember;

import java.util.List;
import java.util.Optional;

import com.ssafy.yumyum.domain.group.dto.response.GroupListResponse;
import com.ssafy.yumyum.domain.group.service.dto.GroupMemberInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMemberMapper {
    List<GroupMemberVO> findAll();
    Optional<GroupMemberVO> findById(Long groupMemberId);
    Optional<GroupMemberVO> findByGroupIdAndUserId(Long groupId,Long userId);
    int insert(GroupMemberVO groupMemberVO);
    int updateById(GroupMemberVO groupMemberVO);
    int deleteById(Long groupMemberId);
    int count();
	long maxId();
	List<GroupMemberVO> findAllByUserId(Long userId);
    List<GroupListResponse> findGroupListByUserId(Long userId);
    int deleteByGroupId(Long groupId);
    List<GroupMemberInfoDTO> findMembersByGroupId(Long groupId);
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);
    int softDeleteById(Long groupMemberId);
    String findUserNameByGroupMemberId(Long groupMemberId);
    Integer findBestRankingByUserId(@Param("userId") Long userId);
    Optional<GroupMemberVO> findByGroupIdAndUserIdWithInActive(Long groupId, Long userId);
    int rejoinGroupMember(Long groupMemberId);
}
