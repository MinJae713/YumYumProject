package com.ssafy.yumyum.domain.user.repository.userRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoleVO {
    private Long userRoleId;
    private String userRoleName;
}
