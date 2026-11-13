package com.ssafy.yumyum.domain.user.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.yumyum.domain.user.dto.request.*;
import com.ssafy.yumyum.domain.user.dto.response.UserWeightListResponse;
import com.ssafy.yumyum.domain.user.service.dto.LoginUserDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.badge.service.BadgeService;
import com.ssafy.yumyum.domain.badge.types.CommandType;
import com.ssafy.yumyum.domain.user.dto.request.ChangePasswordRequest;
import com.ssafy.yumyum.domain.user.dto.request.JoinRequest;
import com.ssafy.yumyum.domain.user.dto.request.LoginRequest;
import com.ssafy.yumyum.domain.user.dto.request.UserResearchRequest;
import com.ssafy.yumyum.domain.user.dto.request.UserUpdateRequest;
import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse;
import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse.WeightRecordDTO;
import com.ssafy.yumyum.domain.user.dto.response.TokenResponse;
import com.ssafy.yumyum.domain.user.dto.response.UserDetailResponse;
import com.ssafy.yumyum.domain.user.dto.response.UserSimpleResponse;
import com.ssafy.yumyum.domain.user.exception.InvalidPasswordException;
import com.ssafy.yumyum.domain.user.exception.InvalidUserRoleException;
import com.ssafy.yumyum.domain.user.exception.UserAlreadyExistException;
import com.ssafy.yumyum.domain.user.exception.UserNotFoundException;
import com.ssafy.yumyum.domain.user.exception.UserWeightException;
import com.ssafy.yumyum.domain.user.exception.WeightLogNotFoundException;
import com.ssafy.yumyum.domain.user.repository.user.UserMapper;
import com.ssafy.yumyum.domain.user.repository.user.UserVO;
import com.ssafy.yumyum.domain.user.repository.userRole.UserRoleMapper;
import com.ssafy.yumyum.domain.user.repository.userRole.UserRoleVO;
import com.ssafy.yumyum.domain.user.repository.weightLog.WeightLogMapper;
import com.ssafy.yumyum.domain.user.repository.weightLog.WeightLogVO;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;
import com.ssafy.yumyum.security.JwtTokenProvider;
import com.ssafy.yumyum.security.UserPrincipal;
import com.ssafy.yumyum.security.exception.RefreshTokenInvalidException;
import com.ssafy.yumyum.security.exception.RefreshTokenMissingException;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;

	private final UserMapper userMapper;
	private final UserRoleMapper userRoleMapper;
	private final WeightLogMapper weightLogMapper;

	private final BadgeService badgeService;

	@Override
	@Transactional
	public TokenResponse join(JoinRequest joinRequest) {
		// 이미 존재하는 회원인지 확인
		if (userMapper.existsByEmail(joinRequest.getEmail()) != 0) {
			throw new UserAlreadyExistException();
		}

		// Role값이 유효한지 확인
		UserRoleVO userRoleVO = userRoleMapper.findByUserRoleName(joinRequest.getRole())
				.orElseThrow(InvalidUserRoleException::new);

		// 비밀번호 암호화
		String encryptedPassword = passwordEncoder.encode(joinRequest.getPassword());

		// User 객체 생성
		UserVO userVO = UserVO.builder()
				.email(joinRequest.getEmail())
				.name(joinRequest.getName())
				.password(encryptedPassword)
				.birthDate(LocalDate.of(0, 1, 1))
				.height(0.0)
				.currentWeight(0.0)
				.targetWeight(0.0)
				.isActive(true)
				.timezone(joinRequest.getTimezone())
				.point(0)
				.providerId(1L)              // 로컬 가입 = provider 1
				.userRoleId(userRoleVO.getUserRoleId())
				.build();

		// DB 저장
		if(userMapper.insert(userVO) != 1){
			throw new InsertFailedException("회원가입에 실패하였습니다.");
		}
		badgeService.checkBadge(userMapper.maxId(), CommandType.JOIN);

		Long userId = userVO.getUserId();
		String email = userVO.getEmail();
		String name = userVO.getName();
		String roleName = userRoleVO.getUserRoleName();

		return TokenResponse.builder()
				.accessToken(jwtTokenProvider.createAccessToken(email, "ROLE_" + roleName))
				.refreshToken(jwtTokenProvider.createRefreshToken(email))
				.user(LoginUserDTO.builder()
						.userId(userId)
						.name(name)
						.role(roleName)
						.build())
				.build();
	}

	@Override
	@Transactional
	public TokenResponse login(LoginRequest loginRequest) {
		// 1. 스프링 시큐리티 기본 인증 처리
		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

		try {
			Authentication authentication = authenticationManager.authenticate(authToken);

			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			String email = userPrincipal.getUsername();
			String role = userPrincipal.getAuthorities().iterator().next().getAuthority();

			Long userId = userPrincipal.getUserId();
			String name = userPrincipal.getName();
			String roleName = userPrincipal.getUserRoleName();

            return TokenResponse.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(email, role))
                    .refreshToken(jwtTokenProvider.createRefreshToken(email))
					.user(LoginUserDTO.builder()
							.userId(userId)
							.name(name)
							.role(roleName)
							.build())
                    .build();

		} catch (BadCredentialsException e) {
			throw new InvalidPasswordException();
		}
	}

	@Override
	@Transactional
	public String reissueToken(String refreshToken) {

		// 1. Refresh Token 존재 여부 확인
		if (refreshToken == null) {
			throw new RefreshTokenMissingException();
		}

		// 2. Refresh Token 유효성 검사
		if (!jwtTokenProvider.validateToken(refreshToken)) {
			throw new RefreshTokenInvalidException();
		}

		// 3. refreshToken에서 email 추출
		String email = jwtTokenProvider.getEmail(refreshToken);

		// 4. email로 유저 principal 다시 조회
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		String role = userDetails.getAuthorities().iterator().next().getAuthority();

		// 5. 새로운 accessToken 생성해서 리턴
		return jwtTokenProvider.createAccessToken(email, role);
	}

	@Override
	@Transactional
	public UserDetailResponse getUserDetail(Long userId) {
		UserVO userVo = userMapper.findByIdWithProviderAndRole(userId)
				.orElseThrow(UserNotFoundException::new);

		return getUserDetailResponse(userVo);
	}

	@Override
	@Transactional
	public UserDetailResponse updateUserInfo(UserUpdateRequest userUpdateRequest, Long userId) {
		// 유저 정보 조회해오기

		UserVO userVO = userMapper.findById(userId).orElseThrow(UserNotFoundException::new);

		// request가 null이 아니거나, 기존 값과 다르면 업데이트
		if(userUpdateRequest.getName() != null && !userUpdateRequest.getName().equals(userVO.getName())) {
			userVO.setName(userUpdateRequest.getName());
		}

		if(userUpdateRequest.getTargetWeight() != null &&
				!userUpdateRequest.getTargetWeight().equals(userVO.getTargetWeight())) {
			userVO.setTargetWeight(userUpdateRequest.getTargetWeight());
		}

		if(userUpdateRequest.getRemindTime() != null && !userUpdateRequest.getRemindTime().equals(userVO.getRemindTime())) {
			userVO.setRemindTime(userUpdateRequest.getRemindTime());
		}

		if(userUpdateRequest.getMealsPerDay() != null && !userUpdateRequest.getMealsPerDay().equals(userVO.getMealsPerDay())) {
			userVO.setMealsPerDay(userUpdateRequest.getMealsPerDay());
		}
		
		///////////////////////// 추가 /////////////////////////
		if(userUpdateRequest.getGender() != null && !userUpdateRequest.getGender().equals(userVO.getGender())) {
			userVO.setGender(userUpdateRequest.getGender());
		}
		
		if(userUpdateRequest.getBirthDate() != null && !userUpdateRequest.getBirthDate().equals(userVO.getBirthDate())) {
			userVO.setBirthDate(userUpdateRequest.getBirthDate());
		}
		
		if(userUpdateRequest.getHeight() != null && !userUpdateRequest.getHeight().equals(userVO.getHeight())) {
			userVO.setHeight(userUpdateRequest.getHeight());
		}
		
		if(userUpdateRequest.getCurrentWeight() != null && !userUpdateRequest.getCurrentWeight().equals(userVO.getCurrentWeight())) {
			userVO.setCurrentWeight(userUpdateRequest.getCurrentWeight());
		}
		
		if(userUpdateRequest.getGoalType() != null && !userUpdateRequest.getGoalType().equals(userVO.getGoalType())) {
			userVO.setGoalType(userUpdateRequest.getGoalType());
		}
		///////////////////////////////////////////////////////

		int updated = userMapper.updateById(userVO);

		if(updated != 1) throw new UpdateFailedException("유저 정보 수정에 실패하였습니다.");

		UserVO updateUser = userMapper.findById(userId).orElseThrow(UserNotFoundException::new);

		return getUserDetailResponse(updateUser);
	}

	private UserDetailResponse getUserDetailResponse(UserVO updateUser) {
		return UserDetailResponse.builder()
				.userId(updateUser.getUserId())
				.email(updateUser.getEmail())
				.name(updateUser.getName())
				.gender(updateUser.getGender())
				.birthDate(updateUser.getBirthDate())
				.height(updateUser.getHeight())
				.currentWeight(updateUser.getCurrentWeight())
				.targetWeight(updateUser.getTargetWeight())
				.goalType(updateUser.getGoalType())
				.mealsPerDay(updateUser.getMealsPerDay())
				.point(updateUser.getPoint())
				.remindTime(updateUser.getRemindTime())
				.providerId(updateUser.getProviderId())
				.providerName(updateUser.getProviderName())
				.userRoleId(updateUser.getUserRoleId())
				.userRoleName(updateUser.getUserRoleName())
				.build();
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordRequest updatePasswordRequest, Long userId) {

		UserVO userVO = userMapper.findById(userId).orElseThrow(UserNotFoundException::new);

		//현재 비밀번호와 유저의 비밀번호가 같은지 검증
		if(!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), userVO.getPassword())) {
			throw new InvalidPasswordException();
		}
		// 현재 비밀번호와 새 비밀번호가 같은지 검증
		if(passwordEncoder.matches(updatePasswordRequest.getNewPassword(), userVO.getPassword())) {
			throw new InvalidPasswordException("기존 비밀번호와 동일합니다.");
		}

		// 새 비밀번호 암호화
		String encodedNewPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());

		int updated = userMapper.updatePasswordById(userVO.getUserId(), encodedNewPassword);

		if(updated != 1) throw new UpdateFailedException("비밀번호 변경에 실패했습니다.");
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) {

		UserVO userVO = userMapper.findById(userId).orElseThrow(UserNotFoundException::new);

		int updated = userMapper.softDeleteById(userVO.getUserId());

		if(updated != 1) throw new UpdateFailedException("회원 탈퇴에 실패했습니다.");
	}

	@Override
	@Transactional
	public void updateUserResearch(UserResearchRequest researchRequest, Long userId) {
		UserVO userVO = userMapper.findById(userId).orElseThrow(UserNotFoundException::new);

		// DTO에서 받아온 값들로 VO변경
		userVO.setGender(researchRequest.getGender());
		userVO.setBirthDate(researchRequest.getBirthDate());
		userVO.setHeight(researchRequest.getHeight());
		userVO.setCurrentWeight(researchRequest.getCurrentWeight());
		userVO.setTargetWeight(researchRequest.getTargetWeight());
		userVO.setGoalType(researchRequest.getGoalType());
		userVO.setMealsPerDay(researchRequest.getMealsPerDay());
		userVO.setRemindTime(researchRequest.getRemindTime());

		if(userMapper.updateUserResearchById(userVO) != 1) {
			throw new UpdateFailedException("리서치 정보 저장에 실패했습니다.");
		}

		// weightLog값도 인서트로 넣기
		WeightLogVO weightLogVO = WeightLogVO.builder()
				.logDate(LocalDate.now())
				.weightKg(userVO.getCurrentWeight())
				.userId(userVO.getUserId())
				.build();

		if(weightLogMapper.insert(weightLogVO) != 1){
			throw new InsertFailedException("몸무게 정보 저장에 실패했습니다.");
		}

	}

	@Override
	@Transactional
	public UserInfoDTO getUserInfo(Long userId) {
		UserVO userVO = userMapper.findByIdWithProviderAndRole(userId).orElseThrow(UserNotFoundException::new);
		return convertUserVoToUserInfoDTO(userVO);
	}

	@Override
	@Transactional
	public List<UserSimpleResponse> searchUserByEmail(String email) {
		List<UserVO> userList = userMapper.findByEmailLike(email);

		if(userList == null || userList.isEmpty()){
			return List.of();
		}

		return userList.stream()
				.map(u -> UserSimpleResponse.builder()
								.userId(u.getUserId())
								.email(u.getEmail())
								.name(u.getName())
								.build())
				.toList();
	}

	@Override
	@Transactional
	public void updateUserPoint(Long userId, Integer pointDelta) {
		// 유저 조회
		UserVO userVO = userMapper.findById(userId)
				.orElseThrow(UserNotFoundException::new);

		// 포인트 계산
		int currentPoint = (userVO.getPoint() == null) ? 0 : userVO.getPoint();
		int newPoint = currentPoint + pointDelta;

		if (newPoint < 0) newPoint = 0; // 음수 방지

		// 전체 업데이트 대신 포인트만 업데이트 호출
		if (userMapper.updatePoint(userId, newPoint) != 1) {
			throw new UpdateFailedException("포인트 업데이트에 실패했습니다.");
		}
	}

	@Override
	public List<UserInfoDTO> findUsersWhoDidNotSubmitResearch() {
		LocalDate today = LocalDate.now();
		List<UserVO> userVoList = userMapper.findUsersWithoutResearch(today);

		List<UserInfoDTO> userDtoList = new ArrayList<>();

		for (UserVO user : userVoList) {
			userDtoList.add(convertUserVoToUserInfoDTO(user));
		}

		return userDtoList;
	}

	@Override
	@Transactional
	public UserWeightListResponse updateUserWeight(UpdateUserWeightRequest updateUserWeightRequest, Long userId) {
		//현재 날짜에 weightLog 있는지 체크
		WeightLogVO weightLogVO = weightLogMapper.findByDateAndUserId(updateUserWeightRequest.getRecordDate(), userId).orElse(null);

		if(weightLogVO != null){		// 있으면 기존 값에 업데이트
			weightLogVO.setWeightKg(updateUserWeightRequest.getCurrentWeight());
			if(weightLogMapper.updateById(weightLogVO) != 1) {
				throw new UpdateFailedException("몸무게 정보 수정에 실패하였습니다.");
			}
		}else{		// 없으면 삽입
			WeightLogVO newWeightLogVo = WeightLogVO.builder()
					.logDate(updateUserWeightRequest.getRecordDate())
					.weightKg(updateUserWeightRequest.getCurrentWeight())
					.userId(userId)
					.build();

			if(weightLogMapper.insert(newWeightLogVo) != 1) {
				throw new InsertFailedException("몸무게 정보 생성에 실패하였습니다.");
			}
		}

		// 유저 테이블 값 변경
		// 유저 아이디로 유저 정보 받아오기
		UserVO currentUserVo = userMapper.findById(userId).orElseThrow(UserNotFoundException::new);

		// 현재 weightLog 에서 제일 최신 값 받아오기(sql 이름 뭘로하지?)
		Double latestWeight = weightLogMapper.findLatestWeightByUserId(userId);

		// 위에 값이 현재 user DB의 currentWeight랑 다르면 업데이트
		if(!Objects.equals(latestWeight, currentUserVo.getCurrentWeight())){
			if(userMapper.updateCurrentWeightByUserId(latestWeight, userId) < 1){
				throw new UpdateFailedException("유저 테이블에 현재 몸무게 정보를 수정하는데 실패했습니다.");
			}
		}

		// 지금까지의 weightLog 리턴(날짜 순으로 정렬) - 반영되고 바로(셀렉 해오기)
		List<WeightLogVO> weightLogVOList = weightLogMapper.findAllByUserIdOrderByDate(userId);

		List<UserWeightListResponse.WeightLog> dtoList = new ArrayList<>();

		for(WeightLogVO weightLog : weightLogVOList){
			dtoList.add(UserWeightListResponse.WeightLog .builder()
					.weightLogId(weightLog.getWeightLogId())
					.weightKg(weightLog.getWeightKg())
					.logDate(weightLog.getLogDate())
					.build());
		}
		return UserWeightListResponse.builder()
				.weightLogList(dtoList)
				.build();
	}

	@Override
	@Transactional
	public UserDetailResponse changeUserRole(ChangeUserRoleRequest changeUserRoleRequest, Long userId) {

		// 해당 유저 정보 받아오기
		UserVO userVO = userMapper.findByIdWithProviderAndRole(userId).orElseThrow(UserNotFoundException::new);

		// 변경할 필요가 없다면 회원 정보 리턴
		if(userVO.getUserRoleName().equals(changeUserRoleRequest.getRoleName())){
			return getUserDetailResponse(userVO);
		}

		// role 이름으로 id값 조회
		UserRoleVO userRoleVO = userRoleMapper.findByUserRoleName(changeUserRoleRequest.getRoleName())
				.orElseThrow(InvalidUserRoleException::new);

		// 현재 유저의 롤과 dto 롤이 차이가 나면 update
		if(userMapper.updateRoleByUserId(userRoleVO.getUserRoleId(), userId) < 1){
			throw new UpdateFailedException("유저 권한 변경에 실패하였습니다.");
		}
		// 변경된 유저 정보 리턴
		userVO.setUserRoleId(userRoleVO.getUserRoleId());
		userVO.setUserRoleName(userRoleVO.getUserRoleName());

		return getUserDetailResponse(userVO);
	}


	private UserInfoDTO convertUserVoToUserInfoDTO(UserVO userVO) {
		return UserInfoDTO.builder()
				.userId(userVO.getUserId())
				.email(userVO.getEmail())
				.name(userVO.getName())
				.gender(userVO.getGender())
				.birthDate(userVO.getBirthDate())
				.height(userVO.getHeight())
				.currentWeight(userVO.getCurrentWeight())
				.targetWeight(userVO.getTargetWeight())
				.goalType(userVO.getGoalType())
				.mealsPerDay(userVO.getMealsPerDay())
				.point(userVO.getPoint())
				.remindTime(userVO.getRemindTime())
				.userRoleId(userVO.getUserRoleId())
				.userRoleName(userVO.getUserRoleName())
				.isActive(userVO.getIsActive())
				.createdAt(userVO.getCreatedAt())
				.updatedAt(userVO.getUpdatedAt())
				.build();
	}

	@Override
	public GetWeightDeltaResponse getWeightDelta(Long userId) {
		UserVO userVO = userMapper.findById(userId).orElseThrow(UserNotFoundException::new);
		Double currentWeight = userVO.getCurrentWeight();
		Double targetWeight = userVO.getTargetWeight();
		if (currentWeight == null || targetWeight == null)
			throw new UserWeightException();
		List<WeightLogVO> weightLogVOs =
				weightLogMapper.findAllByUserIdOrderByDate(userId);

		if (weightLogVOs == null) throw new WeightLogNotFoundException();
		List<WeightRecordDTO> dateOfWeight = new ArrayList<>();
		weightLogVOs.forEach(weightLogVO ->
			dateOfWeight.add(WeightRecordDTO.builder()
				.recordedDate(weightLogVO.getLogDate())
				.weight(weightLogVO.getWeightKg())
				.build()));

		Double startWeight = weightLogVOs.size() == 0 ? 
				currentWeight : weightLogVOs.get(0).getWeightKg();
		Double lossWeight = startWeight - currentWeight;
		return GetWeightDeltaResponse.builder()
				.startWeight(startWeight)
				.currentWeight(currentWeight)
				.lossWeight(lossWeight)
				.targetWeight(targetWeight)
				.dateOfWeight(dateOfWeight)
				.build();
	}
}
