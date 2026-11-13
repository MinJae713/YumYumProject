package com.ssafy.yumyum.domain.group.service.dto;

import com.ssafy.yumyum.domain.group.types.GroupRole;
import com.ssafy.yumyum.domain.user.types.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberInfoDTO {
    private Long groupMemberId;
    private Long userId;
    private GroupRole groupRole;
    private int points;

    private String name;
    private Gender gender;
    private Double currentWeight;
    private Double targetWeight;
    private String goalType;
}
