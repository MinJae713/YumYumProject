package com.ssafy.yumyum.domain.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserDTO {
    private Long userId;
    private String name;
    private String role;
}
