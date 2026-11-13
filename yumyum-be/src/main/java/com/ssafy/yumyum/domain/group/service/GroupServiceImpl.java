package com.ssafy.yumyum.domain.group.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.common.exception.commonExceptions.DeleteFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeProgressException;
import com.ssafy.yumyum.domain.badge.service.BadgeService;
import com.ssafy.yumyum.domain.badge.types.StreakType;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.exercise.exception.NoExerciseRecordException;
import com.ssafy.yumyum.domain.exercise.service.ExerciseService;
import com.ssafy.yumyum.domain.group.dto.request.GroupCreateRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupUpdateRequest;
import com.ssafy.yumyum.domain.group.dto.response.FeedbackResponse;
import com.ssafy.yumyum.domain.group.dto.response.GetRecordedDateResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupDailyLogResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupExerciseDetailResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupInfoResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupListResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupMealDetailResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupMemberInfoResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupMemberRankingInfoResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupUpdateResponse;
import com.ssafy.yumyum.domain.group.exception.GroupMemberNotFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupNotFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupPermissionDeniedException;
import com.ssafy.yumyum.domain.group.repository.exerciseFeedback.ExerciseFeedbackMapper;
import com.ssafy.yumyum.domain.group.repository.group.GroupMapper;
import com.ssafy.yumyum.domain.group.repository.group.GroupVO;
import com.ssafy.yumyum.domain.group.repository.groupInvite.GroupInviteMapper;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberMapper;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberVO;
import com.ssafy.yumyum.domain.group.repository.mealFeedback.MealFeedbackMapper;
import com.ssafy.yumyum.domain.group.service.dto.GroupMemberInfoDTO;
import com.ssafy.yumyum.domain.group.types.GroupRole;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse.GetMealOfDateMeal;
import com.ssafy.yumyum.domain.meal.exception.NoMealDataException;
import com.ssafy.yumyum.domain.meal.service.MealService;
import com.ssafy.yumyum.domain.meal.types.MealType;
import com.ssafy.yumyum.domain.user.service.UserService;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{

	private final UserService userService;
	private final MealService mealService;
	private final ExerciseService exerciseService;
	private final BadgeService badgeService;

	private final GroupMemberMapper groupMemberMapper;
	private final GroupMapper groupMapper;
	private final GroupInviteMapper groupInviteMapper;
	private final MealFeedbackMapper mealFeedbackMapper;
	private final ExerciseFeedbackMapper exerciseFeedbackMapper;

	@Override
	@Transactional
	public GroupInfoResponse createGroup(GroupCreateRequest groupCreateRequest, Long userId) {
		// 현재 로그인된 유저의 role 확인
		UserInfoDTO userInfoDTO = userService.getUserInfo(userId);

		if(!("BUSINESS").equals(userInfoDTO.getUserRoleName())) {
			//권한없음
			throw new GroupPermissionDeniedException();
		}

		// 그룹생성
		GroupVO groupVO = GroupVO.builder()
				.groupName(groupCreateRequest.getGroupName())
				.userId(userId)
				.build();

		if(groupMapper.insert(groupVO) != 1){
			throw new InsertFailedException("그룹 생성에 실패했습니다.");
		}


		// 그룹멤버에 현재 유저 leader로 넣기
		GroupMemberVO groupMemberVO = GroupMemberVO.builder()
				.role(GroupRole.LEADER.name())
				.isActive(true)
				.joinedAt(LocalDateTime.now())
				.points(0)
				.userId(userId)
				.groupId(groupVO.getGroupId())
				.build();

		if(groupMemberMapper.insert(groupMemberVO) != 1)
			throw new InsertFailedException("그룹원 생성에 실패하였습니다.");

		return GroupInfoResponse.builder()
				.groupId(groupVO.getGroupId())
				.groupName(groupVO.getGroupName())
				.build();
	}

	@Override
	@Transactional
	public List<GroupListResponse> getGroupList(Long userId) {
		return groupMemberMapper.findGroupListByUserId(userId);
	}

	@Override
	@Transactional
	public GroupUpdateResponse updateGroupName(GroupUpdateRequest groupUpdateRequest, Long groupId, Long userId) {
		// 그룹 찾아오기
		GroupVO groupVO = groupMapper.findById(groupId)
				.orElseThrow(GroupNotFoundException::new);

		//그룹테이블에서 해당 그룹 값을 찾고, 해당 그룹의 userId(leader)가 userId와 같은지 확인
		if(!groupVO.getUserId().equals(userId)){
			throw new GroupPermissionDeniedException();
		}

		// 그룹 이름 변경
		groupVO.setGroupName(groupUpdateRequest.getNewGroupName());
		if(groupMapper.updateById(groupVO) < 1){
			throw new UpdateFailedException("그룹명 수정에 실패하였습니다.");
		}

		// 변경된 이름이랑 그룹아이디 반환
		return GroupUpdateResponse.builder()
				.groupId(groupVO.getGroupId())
				.groupName(groupVO.getGroupName())
				.build();
	}

	@Override
	@Transactional
	public void deleteGroup(Long groupId, Long userId) {
		// 삭제 권한 체크
		// 그룹 찾아오기
		GroupVO groupVO = groupMapper.findById(groupId)
				.orElseThrow(GroupNotFoundException::new);
		System.out.println("그룹장 ID " + groupVO.getUserId());
		//그룹테이블에서 해당 그룹 값을 찾고, 해당 그룹의 userId(leader)가 userId와 같은지 확인
		if(!groupVO.getUserId().equals(userId)){
			throw new GroupPermissionDeniedException();
		}

		// 하위 테이블들엔 관련 데이터가 없을 수 있으므로 삭제 확인 X
		// 피드백도 모두 삭제
		mealFeedbackMapper.deleteByGroupId(groupId);
		exerciseFeedbackMapper.deleteByGroupId(groupId);
		// 그룹 삭제 시 그룹초대 테이블도 다 지움
		groupInviteMapper.deleteByGroupId(groupId);
		// 그룹 삭제시 그룹 멤버테이블도 다 지움
		groupMemberMapper.deleteByGroupId(groupId);

		// 그룹 삭제
		if(groupMapper.deleteById(groupId) < 1){
			throw new DeleteFailedException("그룹 데이터 삭제에 실패했습니다.");
		}
	}

	@Override
	@Transactional
	public List<GroupMemberRankingInfoResponse> getGroupMemberRanking(Long groupId, Long userId) {
		// 현재 유저가 해당 그룹의 유저인지 확인
		if(!groupMemberMapper.existsByGroupIdAndUserId(groupId, userId)){
			throw new GroupPermissionDeniedException();
		}
		// 그룹 아이디로 그룹멤버들 조회 -> 리더는 안가져와도됨
		List<GroupMemberInfoDTO> groupMemberList = groupMemberMapper.findMembersByGroupId(groupId);

		// 비어있을 경우 빈 리스트 반환
		if(groupMemberList == null || groupMemberList.isEmpty()){
			return List.of();
		}

		// 내림차순 정렬
		groupMemberList.sort(Comparator.comparingInt(GroupMemberInfoDTO::getPoints).reversed());

		return IntStream.range(0, groupMemberList.size())
				.mapToObj(i -> {
					GroupMemberInfoDTO member = groupMemberList.get(i);
					UserInfoDTO userInfoDTO = userService.getUserInfo(member.getUserId());
					int rank = i + 1; // 0번 인덱스 = 1등
					Long mealStreak = 0L;
					Long exerciseStreak = 0L;

					try {
						// BadgeService 호출
						List<GetStreakRecordResponse> streaks = badgeService.getStreakRecord(member.getUserId());

						for (GetStreakRecordResponse record : streaks) {
							if (StreakType.MEAL_RECORD.equals(record.getStreakType())) {
								mealStreak = record.getLongestStreak();
							} else if (StreakType.EXERCISE_RECORD.equals(record.getStreakType())) {
								exerciseStreak = record.getLongestStreak();
							}
						}
					} catch (NoBadgeProgressException e) {
						// 기록 없음 -> 0으로 유지
					}

					return GroupMemberRankingInfoResponse.builder()
							.groupMemberId(member.getGroupMemberId())
							.userId(member.getUserId())
							.name(member.getName())
							.groupRole(member.getGroupRole())
							.goalType(member.getGoalType())
							.currentWeight(member.getCurrentWeight())
							.points(member.getPoints()+userInfoDTO.getPoint())
							.ranking(rank)
							.isMe(member.getUserId() == userId)
							.mealStreak(mealStreak)
							.exerciseStreak(exerciseStreak)
							.build();
				})
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<GroupMemberInfoResponse> getGroupMemberList(Long groupId, Long userId) {
		// 현재 유저가 해당 그룹의 유저인지 확인
		if(!groupMemberMapper.existsByGroupIdAndUserId(groupId, userId)){
			throw new GroupPermissionDeniedException();
		}
		// 현재 유저가 이 그룹의 리더인지 확인
		GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
				.orElseThrow(GroupMemberNotFoundException::new);

		if(!groupMemberVO.getRole().equals(GroupRole.LEADER.name())){
			throw new GroupPermissionDeniedException();
		}

		// groupId로 그룹멤버 정보 조회(리더 제외)
		List<GroupMemberInfoDTO> groupMemberList = groupMemberMapper.findMembersByGroupId(groupId);

		// 비어있을 경우 빈 리스트 반환
		if(groupMemberList == null || groupMemberList.isEmpty()){
			return List.of();
		}

		// GroupMemberInfoResponse로 변환
		return groupMemberList.stream()
				.map(m -> {
					Long mealStreak = 0L;
					Long exerciseStreak = 0L;
					try {
						List<GetStreakRecordResponse> streaks = badgeService.getStreakRecord(m.getUserId());

						for (GetStreakRecordResponse record : streaks) {
							if (StreakType.MEAL_RECORD.equals(record.getStreakType())) {
								mealStreak = record.getLongestStreak();
							} else if (StreakType.EXERCISE_RECORD.equals(record.getStreakType())) {
								exerciseStreak = record.getLongestStreak();
							}
						}
					} catch (NoBadgeProgressException e) {
						// 기록 없음 -> 0으로 유지
					}

					return GroupMemberInfoResponse.builder()
							.groupMemberId(m.getGroupMemberId())
							.userId(m.getUserId())
							.name(m.getName())
							.goalType(m.getGoalType())
							.currentWeight(m.getCurrentWeight())
							.targetWeight(m.getTargetWeight())
							.mealStreak(mealStreak)
							.exerciseStreak(exerciseStreak)
							.build();
				})
				.toList();
	}

	@Override
	@Transactional
	public GroupDailyLogResponse getDailyLogs(Long groupId, Long groupMemberId, LocalDate date, Long userId) {
		GroupMemberVO requester = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
				.orElseThrow(GroupMemberNotFoundException::new);

		Long targetGroupMemberId;

		// groupMemberId가 안 넘어왔으면 로그인 한 본인, groupMemberId가 넘어오면 Leader가 접근
		if (groupMemberId == null) {
			targetGroupMemberId = requester.getGroupMemberId();
		} else {
			targetGroupMemberId = groupMemberId;
		}

		// 실제 보고 싶은 대상 조회
		GroupMemberVO target = groupMemberMapper.findById(targetGroupMemberId)
				.orElseThrow(GroupMemberNotFoundException::new);

		if (!target.getGroupId().equals(groupId)) {
			throw new GroupMemberNotFoundException();
		}

		// 4. 권한 체크
		boolean isMyLog = requester.getGroupMemberId().equals(target.getGroupMemberId());
		boolean isLeader = GroupRole.LEADER.name().equals(requester.getRole());

		if (!isMyLog && !isLeader) {
			throw new GroupPermissionDeniedException();
		}

		// 타겟 대상 유저 인포
		UserInfoDTO userInfo = userService.getUserInfo(target.getUserId());

		List<GroupDailyLogResponse.GroupMealLogDTO> mealLogs = new ArrayList<>();
		Double totalIntakeCalorie = 0.0;

		try {
			GetMealOfDateResponse mealData = mealService.getMealOfDate(target.getUserId(), date);

			if (mealData != null) {
				totalIntakeCalorie = mealData.getMealTotalCalorie(); // 총 섭취 칼로리

				if (mealData.getMeals() != null) {
					List<GetMealOfDateMeal>	getMealOfDateMeals = mealData.getMeals();
					getMealOfDateMeals = getMealOfDateMeals.stream().sorted(new Comparator<GetMealOfDateMeal>() {
						@Override
						public int compare(GetMealOfDateMeal o1, GetMealOfDateMeal o2) {
							MealType mealType1 = MealType.getMealTypeFromLabel(o1.getMealTypeName());
							MealType mealType2 = MealType.getMealTypeFromLabel(o2.getMealTypeName());
							return mealType1.ordinal() - mealType2.ordinal();
						}
					}).toList(); // 추가
					mealLogs = getMealOfDateMeals.stream()
							.map(meal -> {
								boolean hasFeedback = mealFeedbackMapper.existsByMealId(meal.getMealId());

								// 음식 이름 리스트 추출
								List<String> foodNames = meal.getFoods().stream()
										.map(food -> food.getName())
										.collect(Collectors.toList());

								return GroupDailyLogResponse.GroupMealLogDTO.builder()
										.mealId(meal.getMealId())
										.mealType(meal.getMealTypeName())
										.totalCalorie(meal.getTotalCalorie())
										.foodNames(foodNames)
										.hasFeedback(hasFeedback)
										.build();
							})
							.collect(Collectors.toList());
				}
			}
		} catch (NoMealDataException e) {
			// 식단 기록이 없으면 빈 리스트 반환
		}

		// --- 운동 로그 조회 ---
		List<GroupDailyLogResponse.GroupExerciseLogDTO> exerciseLogs = new ArrayList<>();
		Double totalBurnedCalorie = 0.0;

		try {
			// ExerciseService 호출
			GetExercisesOfDateResponse exerciseData = exerciseService.getExercisesOfDate(target.getUserId(), date);

			if (exerciseData != null) {
				totalBurnedCalorie = exerciseData.getTotalCalories(); // 총 소모 칼로리

				if (exerciseData.getExercises() != null) {
					exerciseLogs = exerciseData.getExercises().stream()
							.map(ex -> {
								boolean hasFeedback = exerciseFeedbackMapper.existsByExerciseRecordId(ex.getExerciseRecordId());

								return GroupDailyLogResponse.GroupExerciseLogDTO.builder()
										.exerciseRecordId(ex.getExerciseRecordId())
										.exerciseName(ex.getExerciseName())
										.durationMin(ex.getDurationMin())
										.burnedCalorie(ex.getCalories())
										.hasFeedback(hasFeedback)
										.build();
							})
							.collect(Collectors.toList());
				}
			}
		} catch (NoExerciseRecordException e) {
			// 운동 기록이 없으면 빈 리스트 반환
		}

		return GroupDailyLogResponse.builder()
				.userName(userInfo.getName())
				.goalType(userInfo.getGoalType())
				.currentWeight(userInfo.getCurrentWeight())
				.targetWeight(userInfo.getTargetWeight())
				.mealLogs(mealLogs)
				.exerciseLogs(exerciseLogs)
				.totalIntakeCalorie(totalIntakeCalorie)
				.totalBurnedCalorie(totalBurnedCalorie)
				.build();
	}

	@Override
	@Transactional
	public GroupMealDetailResponse getGroupMealDetail(Long groupId, Long mealId, Long userId) {
		// 멤버 확인 (로그인한 유저가 이 그룹 멤버인지)
		if (!groupMemberMapper.existsByGroupIdAndUserId(groupId, userId)) {
			throw new GroupMemberNotFoundException();
		}

		// 식단 상세 정보 가져오기
		GetMealDetailResponse mealDetail = mealService.getMealDetail(mealId);

		// 피드백 목록 가져오기
		List<FeedbackResponse> feedbacks = mealFeedbackMapper.findAllByGroupIdAndMealId(groupId, mealId);

		return GroupMealDetailResponse.builder()
				.mealDetail(mealDetail)
				.feedbacks(feedbacks)
				.build();
	}

	@Override
	@Transactional
	public GroupExerciseDetailResponse getGroupExerciseDetail(Long groupId, Long exerciseRecordId, Long userId) {
		// 멤버 확인
		if (!groupMemberMapper.existsByGroupIdAndUserId(groupId, userId)) {
			throw new GroupMemberNotFoundException();
		}

		// 운동 상세 정보 가져오기
		GetExerciseDetailResponse exerciseDetail = exerciseService.getExerciseDetail(exerciseRecordId);

		// 피드백 목록 가져오기
		List<FeedbackResponse> feedbacks = exerciseFeedbackMapper.findAllByGroupIdAndExerciseRecordId(groupId, exerciseRecordId);

		return GroupExerciseDetailResponse.builder()
				.exerciseDetail(exerciseDetail)
				.feedbacks(feedbacks)
				.build();
	}
	
	@Override
	public GetRecordedDateResponse getRecordedDate(Long groupId, Long groupMemberId, Long userId) {
		GroupMemberVO requester = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
				.orElseThrow(GroupMemberNotFoundException::new); 

		Long targetGroupMemberId;

		if (groupMemberId == null) { // 그룹원 쪽 요청인 경우 (userId로 GroupMember 조회)
			targetGroupMemberId = requester.getGroupMemberId();
		} else { // 그룹장 쪽 요청인 경우 (해당 그룹의 그룹원 아이디 지정)
			targetGroupMemberId = groupMemberId;
		}

		// 실제 보고 싶은 대상 조회
		GroupMemberVO target = groupMemberMapper.findById(targetGroupMemberId)
				.orElseThrow(GroupMemberNotFoundException::new);

		if (!target.getGroupId().equals(groupId)) { // 그룹 아이디 확인
			throw new GroupMemberNotFoundException();
		}

		// 4. 권한 체크 (기록 조회 권한이 있는지 확인)
		boolean isMyLog = requester.getGroupMemberId().equals(target.getGroupMemberId());
		boolean isLeader = GroupRole.LEADER.name().equals(requester.getRole());

		if (!isMyLog && !isLeader) {
			throw new GroupPermissionDeniedException();
		}
		
		List<LocalDate> mealDates = mealService.getRecordedDate(target.getUserId());
		List<LocalDate> exerciseDates = exerciseService.getRecordedDates(target.getUserId());
		
		return GetRecordedDateResponse.builder()
				.mealDates(mealDates)
				.exerciseDates(exerciseDates)
				.build();
	}
}
