package com.ssafy.yumyum.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupInviteListResponse {
    private Long groupInviteId;
    private Long groupId;
    private String groupName;
    private String leaderName;
    private LocalDateTime sentAt;
}
