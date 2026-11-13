package com.ssafy.yumyum.domain.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupUpdateRequest {
    @NotBlank(message = "새로운 그룹 이름을 입력해주세요.")
    @Size(min = 2, max = 20, message = "그룹 이름은 2자 이상 20자 이하로 입력해주세요.")
    private String newGroupName;
}
