package com.ssafy.yumyum.domain.group.repository.groupInvite;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupInviteVO {
    private Long groupInviteId;
    private String status;
    private LocalDateTime sentAt;
    private LocalDateTime respondedAt;
    private Long userId;
    private Long groupId;
}
