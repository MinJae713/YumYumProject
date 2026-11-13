package com.ssafy.yumyum.domain.group.dto.response;

import com.ssafy.yumyum.domain.group.types.GroupRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GroupMemberRankingInfoResponse {
    private int ranking;
    private Long groupMemberId;
    private Long userId;
    private String name;
    private GroupRole groupRole;
    private String goalType;
    private Double currentWeight;
    private int points;
    private Boolean isMe;

    private Long mealStreak;
    private Long exerciseStreak;
}
