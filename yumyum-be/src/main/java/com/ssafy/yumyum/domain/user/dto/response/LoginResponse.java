package com.ssafy.yumyum.domain.user.dto.response;

import com.ssafy.yumyum.domain.user.service.dto.LoginUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private String accessToken;
    private LoginUserDTO user;
}
