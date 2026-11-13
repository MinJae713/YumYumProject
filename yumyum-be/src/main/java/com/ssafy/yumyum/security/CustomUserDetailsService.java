package com.ssafy.yumyum.security;

import com.ssafy.yumyum.domain.user.exception.UserNotFoundException;
import com.ssafy.yumyum.domain.user.repository.user.UserMapper;
import com.ssafy.yumyum.domain.user.repository.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {

        UserVO userVo = userMapper.findByEmailWithProviderAndRole(email)
                .orElseThrow(UserNotFoundException::new);

        return new UserPrincipal(userVo);
    }
}
