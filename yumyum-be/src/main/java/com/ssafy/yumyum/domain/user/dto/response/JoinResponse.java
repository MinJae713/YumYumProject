package com.ssafy.yumyum.domain.user.dto.response;

import com.ssafy.yumyum.domain.user.service.dto.LoginUserDTO;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinResponse {
    private String message;
    private String accessToken;
    private LoginUserDTO user;
}
