package com.ssafy.yumyum.domain.group.repository.groupInvite;

import java.util.List;
import java.util.Optional;

import com.ssafy.yumyum.domain.group.dto.response.GroupInviteListResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupInviteMapper {
    List<GroupInviteVO> findAll();
    Optional<GroupInviteVO> findById(Long groupInviteId);
    int insert(GroupInviteVO groupInviteVO);
    int updateById(GroupInviteVO groupInviteVO);
    int deleteById(Long groupInviteId);
    int count();
	long maxId();
    int deleteByGroupId(Long groupId);
    boolean existPendingByGroupIdAndUserId(Long groupId, Long userId);
    List<GroupInviteListResponse> findPendingInvitesByUserId(Long userId);
}
