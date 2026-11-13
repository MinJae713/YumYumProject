package com.ssafy.yumyum.security;

import com.ssafy.yumyum.domain.user.repository.user.UserVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;
	// 로그인된 유저 정보들 중 필요해 보이는 것만 필드값으로 지정
    private final Long userId;
    private final String password;
    private final String email;
    private final String name;
    private final String userRoleName;
    private final boolean isActive;

    public UserPrincipal(UserVO userVO) {
        this.userId = userVO.getUserId();
        this.email = userVO.getEmail();
        this.password = userVO.getPassword();
        this.name = userVO.getName();
        this.userRoleName = userVO.getUserRoleName();
        this.isActive = userVO.getIsActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // user_role_id 또는 role_name 중 원하는 값으로 권한 지정
        String role = "ROLE_" + this.userRoleName;
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // 스프링 시큐리티에서 로그인 ID를 Username이라는 이름으로 호출해감  -> 그래서 Username인데 email 반환
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return this.isActive; }
}
