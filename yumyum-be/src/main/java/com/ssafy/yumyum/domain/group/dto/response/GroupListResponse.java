package com.ssafy.yumyum.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupListResponse {
    private Long groupId;
    private String groupName;
    private String roleName;
    private int memberCount;
}
