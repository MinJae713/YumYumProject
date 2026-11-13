package com.ssafy.yumyum.domain.user.controller;

import com.ssafy.yumyum.domain.user.dto.request.*;
import com.ssafy.yumyum.domain.user.dto.response.*;
import com.ssafy.yumyum.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	
	private final UserService userService;

	/**
	 * 회원가입
	 */
	@PostMapping("/join")
	public ResponseEntity<ApiResponse<JoinResponse>> join(@Validated @RequestBody JoinRequest joinRequest){

		TokenResponse tokenResponse = userService.join(joinRequest);

		// 회원가입 시 자동 로그인
		// RefreshToken → HttpOnly 쿠키 저장
		ResponseCookie cookie = ResponseCookie
				.from("refreshToken", tokenResponse.getRefreshToken())
				.httpOnly(true)
				.secure(false)        // HTTPS 환경이면 true로 변경
				.sameSite("Strict")
				.path("/")
				.maxAge(1209600)
				.build();

		JoinResponse joinResponse = JoinResponse.builder()
				.message("회원가입에 성공했습니다.")
				.accessToken(tokenResponse.getAccessToken())
				.user(tokenResponse.getUser())
				.build();

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.header("Set-Cookie", cookie.toString())
				.body(ApiResponse.created(joinResponse));
	}
	
	/**
	 * 로그인
	 */
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Validated @RequestBody LoginRequest loginRequest) {

		TokenResponse tokenResponse = userService.login(loginRequest);

		// 3. RefreshToken → HttpOnly 쿠키 저장
		ResponseCookie cookie = ResponseCookie
				.from("refreshToken", tokenResponse.getRefreshToken())
				.httpOnly(true)
				.secure(false)        // HTTPS 환경이면 true로 변경
				.sameSite("Strict")
				.path("/")
				.maxAge(1209600)
				.build();

		LoginResponse loginResponse = LoginResponse.builder()
				.message("로그인에 성공했습니다.")
				.accessToken(tokenResponse.getAccessToken())
				.user(tokenResponse.getUser())
				.build();

		return ResponseEntity.ok()
				.header("Set-Cookie", cookie.toString())
				.body(ApiResponse.ok(loginResponse));
	}

	/**
	 * AccessToken 재발급
	 */
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<String>> reissueToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
		log.info("access token 재발급");
		return ResponseEntity.ok(ApiResponse.ok(userService.reissueToken(refreshToken)));
	}


	/**
	 * 로그아웃 -- refreshToken 삭제
	 * -> 해당 메서드에서 200 ok를 받으면 프론트에서 accessToken도 삭제 하는 로직
	 */
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<String>> logout(){
		// 1. refreshToken 쿠키 삭제 (maxAge=0)
		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
				.httpOnly(true)
				.secure(false)         // HTTPS 환경이라면 true
				.sameSite("Strict")
				.path("/")
				.maxAge(0)             // 쿠키 즉시 제거
				.build();

		return ResponseEntity.ok()
				.header("Set-Cookie", deleteCookie.toString())
				.body(ApiResponse.ok("로그아웃 성공"));
	}
	
	/**
	 * 유저 정보 조회
	 */
	@GetMapping("/detail")
	public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetail(@AuthenticationPrincipal UserPrincipal user){
		return ResponseEntity.ok(ApiResponse.ok(userService.getUserDetail(user.getUserId())));
	}
	
	/**
	 * 유저 정보 수정
	 */
	@PatchMapping("/update")
	public ResponseEntity<ApiResponse<?>>updateUserInfo(@RequestBody UserUpdateRequest userUpdateRequest, @AuthenticationPrincipal UserPrincipal user){
		return ResponseEntity.ok(ApiResponse.ok(userService.updateUserInfo(userUpdateRequest, user.getUserId())));
	}

	/**
	 * 비밀번호 변경
	 */
	@PatchMapping("/update-password")
	public ResponseEntity<ApiResponse<String>> changePassword(@Validated @RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal UserPrincipal user){
		userService.changePassword(changePasswordRequest, user.getUserId());
		return ResponseEntity.ok(ApiResponse.ok("비밀번호 변경 완료"));

	}

	/**
	 * 회원 탈퇴
	 */
	@DeleteMapping
	public ResponseEntity<ApiResponse<?>> deleteUser(@AuthenticationPrincipal UserPrincipal user){
		userService.deleteUser(user.getUserId());

		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
				.httpOnly(true)
				.secure(false)         // HTTPS 환경이라면 true
				.sameSite("Strict")
				.path("/")
				.maxAge(0)             // 쿠키 즉시 제거
				.build();

		return ResponseEntity.ok()
				.header("Set-Cookie", deleteCookie.toString())
				.body(ApiResponse.ok("회원 탈퇴에 성공했습니다."));
	}

	/**
	 * 초기 리서치 정보 받아오기
	 */
	@PatchMapping("/research")
	public ResponseEntity<ApiResponse<String>> updateUserResearch(@Validated @RequestBody UserResearchRequest researchRequest, @AuthenticationPrincipal UserPrincipal user){
		userService.updateUserResearch(researchRequest, user.getUserId());
		return ResponseEntity.ok(ApiResponse.ok("초기 리서치 저장에 성공했습니다."));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<UserSimpleResponse>>> searchUserByEmail(@RequestParam String email){
		return ResponseEntity.ok(ApiResponse.ok(userService.searchUserByEmail(email)));
	}

	@PostMapping("/weight")
	public ResponseEntity<ApiResponse<UserWeightListResponse>> updateUserWeight(@Validated @RequestBody UpdateUserWeightRequest updateUserWeightRequest, @AuthenticationPrincipal UserPrincipal user){
		UserWeightListResponse userWeightListResponse =  userService.updateUserWeight(updateUserWeightRequest, user.getUserId());
		return ResponseEntity.ok(ApiResponse.ok(userWeightListResponse));
	}

	@PatchMapping("/change-role")
	public ResponseEntity<ApiResponse<UserDetailResponse>> changeUserRole(@RequestBody @Validated ChangeUserRoleRequest changeUserRoleRequest, @AuthenticationPrincipal UserPrincipal user){
		return ResponseEntity.ok(ApiResponse.ok(userService.changeUserRole(changeUserRoleRequest, user.getUserId())));
	}

}
