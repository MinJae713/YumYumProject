package com.ssafy.yumyum.domain.group.repository.group;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupVO {
    private Long groupId;
    private String groupName;
    private LocalDateTime createdAt;
    private Long userId;
}
