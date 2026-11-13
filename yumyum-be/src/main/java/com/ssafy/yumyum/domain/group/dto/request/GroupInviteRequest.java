package com.ssafy.yumyum.domain.group.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupInviteRequest {
    @NotNull(message = "초대할 유저 ID는 필수입니다.")
    private Long userId;
}
