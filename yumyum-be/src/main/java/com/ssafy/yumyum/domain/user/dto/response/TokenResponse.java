package com.ssafy.yumyum.domain.user.dto.response;

import com.ssafy.yumyum.domain.user.service.dto.LoginUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private LoginUserDTO user;
}
