package com.ssafy.yumyum.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssafy.yumyum.domain.user.dto.request.JoinRequest;
import com.ssafy.yumyum.domain.user.dto.request.LoginRequest;
import com.ssafy.yumyum.domain.user.dto.request.UserResearchRequest;
import com.ssafy.yumyum.domain.user.dto.response.TokenResponse;
import com.ssafy.yumyum.domain.user.dto.response.UserDetailResponse;
import com.ssafy.yumyum.domain.user.exception.InvalidPasswordException;
import com.ssafy.yumyum.domain.user.exception.UserAlreadyExistException;
import com.ssafy.yumyum.domain.user.exception.UserNotFoundException;
import com.ssafy.yumyum.domain.user.repository.user.UserMapper;
import com.ssafy.yumyum.domain.user.repository.user.UserVO;
import com.ssafy.yumyum.domain.user.repository.userRole.UserRoleMapper;
import com.ssafy.yumyum.domain.user.repository.userRole.UserRoleVO;
import com.ssafy.yumyum.domain.user.repository.weightLog.WeightLogMapper;
import com.ssafy.yumyum.domain.user.repository.weightLog.WeightLogVO;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;
import com.ssafy.yumyum.domain.user.types.Gender;
import com.ssafy.yumyum.security.JwtTokenProvider;
import com.ssafy.yumyum.security.UserPrincipal;

@ExtendWith(MockitoExtension.class) // Mockito 환경 사용
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService; // 테스트할 타겟

    // --- 가짜 의존성들 (Mocks) ---
    @Mock private UserMapper userMapper;
    @Mock private UserRoleMapper userRoleMapper;
    @Mock private WeightLogMapper weightLogMapper;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private PasswordEncoder passwordEncoder;

    // ----------------------------------------------------------------
    // 1. 회원가입 테스트
    // ----------------------------------------------------------------
    @Test
    @DisplayName("회원가입 - 성공")
    void join_Success() {
        // given
        JoinRequest req = JoinRequest.builder()
                .email("test@test.com")
                .name("테스트")
                .password("1234")
                .role("ROLE_USER")
                .timezone("Asia/Seoul")
                .build();

        UserRoleVO mockRole = UserRoleVO.builder().userRoleId(1L).userRoleName("ROLE_USER").build();

        // 행동 정의
        given(userMapper.existsByEmail(req.getEmail())).willReturn(0); // 중복 아님
        given(userRoleMapper.findByUserRoleName(req.getRole())).willReturn(Optional.of(mockRole)); // Role 존재함
        given(passwordEncoder.encode(req.getPassword())).willReturn("encodedPassword"); // 비번 암호화
        given(userMapper.insert(any(UserVO.class))).willReturn(1); // Insert 성공
        given(jwtTokenProvider.createAccessToken(any(), any())).willReturn("access-token");
        given(jwtTokenProvider.createRefreshToken(any())).willReturn("refresh-token");

        // when
        TokenResponse response = userService.join(req);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        verify(userMapper).insert(any(UserVO.class)); // insert가 호출되었는지 검증
    }

    @Test
    @DisplayName("회원가입 - 실패 (이메일 중복)")
    void join_Fail_DuplicateEmail() {
        // given
        JoinRequest req = JoinRequest.builder().email("duplicate@test.com").build();
        given(userMapper.existsByEmail(req.getEmail())).willReturn(1); // 이미 존재함

        // when & then
        assertThrows(UserAlreadyExistException.class, () -> userService.join(req));
    }

    // ----------------------------------------------------------------
    // 2. 로그인 테스트
    // ----------------------------------------------------------------
    @Test
    @DisplayName("로그인 - 성공")
    void login_Success() {
// given
        LoginRequest req = LoginRequest.builder().email("test@test.com").password("1234").build();

        // Mock Authentication & UserPrincipal
        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        given(userPrincipal.getUsername()).willReturn("test@test.com");

        // [수정된 부분] (Collection)으로 캐스팅하여 제네릭 불일치 해결
        given(userPrincipal.getAuthorities()).willReturn(
                (Collection) Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        given(authentication.getPrincipal()).willReturn(userPrincipal);

        // 인증 매니저 설정
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        given(jwtTokenProvider.createAccessToken(any(), any())).willReturn("access-token");
        given(jwtTokenProvider.createRefreshToken(any())).willReturn("refresh-token");

        // when
        TokenResponse response = userService.login(req);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("로그인 - 실패 (비밀번호 불일치)")
    void login_Fail_BadCredentials() {
        // given
        LoginRequest req = LoginRequest.builder().email("test@test.com").password("wrong").build();

        // 인증 매니저가 BadCredentialsException을 던지도록 설정
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("비번 틀림"));

        // when & then
        assertThrows(InvalidPasswordException.class, () -> userService.login(req));
    }

    // ----------------------------------------------------------------
    // 3. 유저 상세 조회 테스트
    // ----------------------------------------------------------------
    @Test
    @DisplayName("유저 상세 조회 - 성공")
    void getUserDetail_Success() {
        // given
        Long userId = 1L;
        UserVO mockUser = UserVO.builder()
                .userId(userId)
                .email("test@test.com")
                .name("홍길동")
                .point(500)
                .providerName("local")
                .userRoleName("ROLE_USER")
                .build();

        given(userMapper.findByIdWithProviderAndRole(userId)).willReturn(Optional.of(mockUser));

        // when
        UserDetailResponse result = userService.getUserDetail(userId);

        // then
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getPoint()).isEqualTo(500);
    }

    // ----------------------------------------------------------------
    // 4. 초기 리서치 업데이트 테스트 (VO 업데이트 + WeightLog 인서트)
    // ----------------------------------------------------------------
    @Test
    @DisplayName("유저 리서치 정보 업데이트 - 성공")
    void updateUserResearch_Success() {
        // given
        Long userId = 1L;
        UserResearchRequest req = UserResearchRequest.builder()
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 1, 1))
                .height(180.0)
                .currentWeight(75.0)
                .goalType("DIET")
                .mealsPerDay(3)
                .build();

        UserVO mockUser = UserVO.builder().userId(userId).build();

        given(userMapper.findById(userId)).willReturn(Optional.of(mockUser));
        given(userMapper.updateUserResearchById(any(UserVO.class))).willReturn(1); // 업데이트 성공
        given(weightLogMapper.insert(any(WeightLogVO.class))).willReturn(1); // 몸무게 로그 저장 성공

        // when
        userService.updateUserResearch(req, userId);

        // then
        // 1. 유저 정보가 업데이트 메서드에 전달되었는지 확인
        verify(userMapper).updateUserResearchById(any(UserVO.class));
        // 2. 몸무게 로그가 인서트 되었는지 확인
        verify(weightLogMapper).insert(any(WeightLogVO.class));
    }

    // ----------------------------------------------------------------
    // 5. 내부 서비스 통신용 UserInfo 조회 테스트 (DTO 변환 확인)
    // ----------------------------------------------------------------
    @Test
    @DisplayName("UserInfo 조회 - 성공 (DTO 변환 검증)")
    void getUserInfo_Success() {
        // given
        Long userId = 1L;

        // 변환 로직을 꼼꼼히 체크하기 위해 더미 데이터를 꽉 채웁니다.
        UserVO mockUser = UserVO.builder()
                .userId(userId)
                .email("info@test.com")
                .name("정보조회")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1990, 5, 5))
                .height(175.5)
                .currentWeight(70.0)
                .goalType("DIET") // 이전에 말씀하신 목표 타입
                .mealsPerDay(3)
                .point(1200)
                .userRoleId(1L)
                .userRoleName("ROLE_USER")
                .isActive(true)
                .build();

        given(userMapper.findByIdWithProviderAndRole(userId)).willReturn(Optional.of(mockUser));

        // when
        UserInfoDTO result = userService.getUserInfo(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("info@test.com");

        // 핵심: VO -> DTO 변환이 잘 되었는지 주요 헬스케어 데이터 검증
        assertThat(result.getGender()).isEqualTo(Gender.MALE);
        assertThat(result.getGoalType()).isEqualTo("DIET");
        assertThat(result.getCurrentWeight()).isEqualTo(70.0);
        assertThat(result.getPoint()).isEqualTo(1200);
        assertThat(result.getIsActive()).isTrue();

        // 비밀번호 같은 민감한 정보는 DTO에 없어야 함 (UserInfoDTO에 필드가 아예 없을 테지만, null 체크 등)
        verify(userMapper).findByIdWithProviderAndRole(userId);
    }

    @Test
    @DisplayName("UserInfo 조회 - 실패 (존재하지 않는 유저)")
    void getUserInfo_NotFound() {
        // given
        Long userId = 999L;
        given(userMapper.findByIdWithProviderAndRole(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.getUserInfo(userId));
    }

    @Test
    @DisplayName("포인트 업데이트 - 성공 (정상 적립)")
    void updateUserPoint_Success_Add() {
        // given
        Long userId = 1L;
        int initialPoint = 100;
        int pointDelta = 50; // 50점 추가

        UserVO mockUser = UserVO.builder()
                .userId(userId)
                .point(initialPoint)
                .build();

        given(userMapper.findById(userId)).willReturn(Optional.of(mockUser));
        given(userMapper.updatePoint(userId, 150)).willReturn(1); // 100 + 50 = 150 예상

        // when
        userService.updateUserPoint(userId, pointDelta);

        // then
        verify(userMapper).updatePoint(userId, 150); // 150점으로 업데이트 호출되었는지 검증
    }

    @Test
    @DisplayName("포인트 업데이트 - 성공 (차감 및 음수 방어)")
    void updateUserPoint_Success_Subtract_NegativeGuard() {
        // given
        Long userId = 1L;
        int initialPoint = 30;
        int pointDelta = -50; // 50점 차감 (결과가 -20이 됨 -> 0으로 보정되어야 함)

        UserVO mockUser = UserVO.builder()
                .userId(userId)
                .point(initialPoint)
                .build();

        given(userMapper.findById(userId)).willReturn(Optional.of(mockUser));
        // 음수가 나오면 0으로 업데이트해야 함
        given(userMapper.updatePoint(userId, 0)).willReturn(1);

        // when
        userService.updateUserPoint(userId, pointDelta);

        // then
        verify(userMapper).updatePoint(userId, 0); // 0점으로 업데이트 호출되었는지 검증
    }
}