package com.ssafy.yumyum.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequest {
	
    @NotBlank(message = "이름은 필수 값입니다.")
    @Size(min = 2, max = 20, message = "이름은 2~20자로 입력해주세요.")
	private String name;
    
    @NotBlank(message = "이메일은 필수 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
    
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*]).+$",
        message = "비밀번호는 영어, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다."
    )
	private String password;
    
    @NotBlank(message = "role은 필수 값입니다.")
    @Pattern(
        regexp = "^(USER|BUSINESS|ADMIN)$",
        message = "role은 USER, BUSINESS, ADMIN 중 하나여야 합니다."
    )
	private String role;

    // 이 값은 클라이언트에서 입력 받는 값이 아닌, JS에서 감지해서 넘겨주도록.
    @NotBlank(message = "timezone은 필수 값입니다.")
    private String timezone;
}
