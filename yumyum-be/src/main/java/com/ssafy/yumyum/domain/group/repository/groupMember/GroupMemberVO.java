package com.ssafy.yumyum.domain.group.repository.groupMember;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupMemberVO {
    private Long groupMemberId;
    private String role;
    private Boolean isActive;
    private LocalDateTime joinedAt;
    private Integer points;
    private Long userId;
    private Long groupId;
}
