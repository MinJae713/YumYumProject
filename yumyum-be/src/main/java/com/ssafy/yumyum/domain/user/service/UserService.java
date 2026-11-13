package com.ssafy.yumyum.domain.user.service;

import java.util.List;

import com.ssafy.yumyum.domain.user.dto.request.ChangePasswordRequest;
import com.ssafy.yumyum.domain.user.dto.request.ChangeUserRoleRequest;
import com.ssafy.yumyum.domain.user.dto.request.JoinRequest;
import com.ssafy.yumyum.domain.user.dto.request.LoginRequest;
import com.ssafy.yumyum.domain.user.dto.request.UpdateUserWeightRequest;
import com.ssafy.yumyum.domain.user.dto.request.UserResearchRequest;
import com.ssafy.yumyum.domain.user.dto.request.UserUpdateRequest;
import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse;
import com.ssafy.yumyum.domain.user.dto.response.TokenResponse;
import com.ssafy.yumyum.domain.user.dto.response.UserDetailResponse;
import com.ssafy.yumyum.domain.user.dto.response.UserSimpleResponse;
import com.ssafy.yumyum.domain.user.dto.response.UserWeightListResponse;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;

public interface UserService {

	TokenResponse join(JoinRequest joinRequest);

	TokenResponse login(LoginRequest loginRequest);

	String reissueToken(String refreshToken);

    UserDetailResponse getUserDetail(Long userId);

	UserDetailResponse updateUserInfo(UserUpdateRequest userUpdateRequest, Long userId);

	void changePassword(ChangePasswordRequest updatePasswordRequest, Long userId);

	void deleteUser(Long userId);

    void updateUserResearch(UserResearchRequest researchRequest, Long userId);

	UserInfoDTO getUserInfo(Long userId);

    List<UserSimpleResponse> searchUserByEmail(String email);

	void updateUserPoint(Long userId, Integer pointDelta);

	List<UserInfoDTO> findUsersWhoDidNotSubmitResearch();

	UserDetailResponse changeUserRole(ChangeUserRoleRequest changeUserRoleRequest, Long userId);

	GetWeightDeltaResponse getWeightDelta(Long userId); // 추가

    UserWeightListResponse updateUserWeight(UpdateUserWeightRequest updateUserWeightRequest, Long userId);
}
