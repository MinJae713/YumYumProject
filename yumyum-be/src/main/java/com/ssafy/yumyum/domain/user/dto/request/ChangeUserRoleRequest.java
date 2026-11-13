package com.ssafy.yumyum.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUserRoleRequest {
    @NotBlank(message = "권한 이름은 필수 입력 값입니다.")
    @Pattern(regexp = "USER|BUSINESS|ADMIN", message = "권한은 USER, BUSINESS, ADMIN 중 하나여야 합니다.")
    private String roleName;
}
