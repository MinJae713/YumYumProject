package com.ssafy.yumyum.domain.exercise.service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.domain.badge.service.BadgeService;
import com.ssafy.yumyum.domain.badge.types.CommandType;
import com.ssafy.yumyum.domain.badge.types.StreakType;
import com.ssafy.yumyum.domain.exercise.dto.request.AddExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.ScoreExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.UpdateExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.response.AddExerciseResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.DeleteExerciseResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetDailyAverageSpendedCalorieResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse.ExerciseCategoryInfo;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseListResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse.GetExercisesOfDateExercise;
import com.ssafy.yumyum.domain.exercise.dto.response.GetWeeklyTotalDurationResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.ScoreExerciseResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.UpdateExerciseResponse;
import com.ssafy.yumyum.domain.exercise.exception.ExcerciseDateInputException;
import com.ssafy.yumyum.domain.exercise.exception.ExerciseRecordDeleteException;
import com.ssafy.yumyum.domain.exercise.exception.ExerciseRecordInsertException;
import com.ssafy.yumyum.domain.exercise.exception.ExerciseRecordUpdateException;
import com.ssafy.yumyum.domain.exercise.exception.NoExerciseCatalogException;
import com.ssafy.yumyum.domain.exercise.exception.NoExerciseCategoryException;
import com.ssafy.yumyum.domain.exercise.exception.NoExerciseRecordException;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog.ExerciseCatalogMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog.ExerciseCatalogVO;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCategory.ExerciseCategoryMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCategory.ExerciseCategoryVO;
import com.ssafy.yumyum.domain.exercise.repository.exerciseRecord.ExerciseRecordMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseRecord.ExerciseRecordVO;
import com.ssafy.yumyum.domain.exercise.service.dto.ExerciseRecordInfoDTO;
import com.ssafy.yumyum.domain.group.service.GroupMemberService;
import com.ssafy.yumyum.domain.user.exception.UserWeightException;
import com.ssafy.yumyum.domain.user.service.UserService;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService{
	
	// domain repositories
	private final ExerciseCatalogMapper exerciseCatalogMapper;
	private final ExerciseCategoryMapper exerciseCategoryMapper;
	private final ExerciseRecordMapper exerciseRecordMapper;
	
	// etc domain repositories
	private final UserService userService;
	private final GroupMemberService groupMemberService;
	private final BadgeService badgeService;

	@Override
	public GetExercisesOfDateResponse getExercisesOfDate(
			Long userId, LocalDate exerciseDate) {
		boolean inGroup = groupMemberService.checkInGroup(userId); // 소속 그룹 있는지 확인
		List<ExerciseRecordVO> exerciseRecordVOs = 
				exerciseRecordMapper.findAllByUserIdAndExerciseDate(userId, exerciseDate);
		if (exerciseRecordVOs == null)
			throw new NoExerciseRecordException();
		Double totalCalories = 0.0;
		Integer totalPoints = 0;
		Integer totalDurationMin = 0;
		List<LocalDate> recordedDates = getMonthRecord(userId, 
				exerciseDate.getYear(), exerciseDate.getMonth().getValue());
		List<GetExercisesOfDateExercise> exercises = new ArrayList<>();
		for (ExerciseRecordVO exerciseRecordVO : exerciseRecordVOs) {
			Long exerciseRecordId = exerciseRecordVO.getExerciseRecordId();
			ExerciseCatalogVO exerciseCatalogVO = 
					exerciseCatalogMapper.findById(exerciseRecordVO.getExerciseId())
					.orElseThrow(NoExerciseCatalogException::new);
			String exerciseName = exerciseCatalogVO.getName();
			
			ExerciseCategoryVO exerciseCategoryVO = 
					exerciseCategoryMapper.findById(exerciseCatalogVO.getExerciseCategoryId())
					.orElseThrow(NoExerciseCategoryException::new);
			String exerciseCategoryName = exerciseCategoryVO.getExerciseCategoryName();
			Double calories = exerciseRecordVO.getCaloriesKcal();
			totalCalories += calories;
			Integer points = exerciseRecordVO.getPoints();
			totalPoints += points;
			
			Integer durationMin = exerciseRecordVO.getDurationMin();
			Integer setNo = exerciseRecordVO.getSetNo();
			Integer reps = exerciseRecordVO.getReps();
			Integer weightKg = exerciseRecordVO.getWeightKg();
			
			String exerciseOptionDetail = null;
			if (exerciseCategoryName.equals("근력 운동") || 
					exerciseCategoryName.equals("맨몸 운동")) {
				exerciseOptionDetail = "";
				switch (exerciseCategoryName) {
				case "근력 운동": {
					exerciseOptionDetail = String.format(" x %dkg", weightKg);
				}
				case "맨몸 운동": {
					exerciseOptionDetail = String.format("%d세트 x %d회%s", 
							setNo, reps, exerciseOptionDetail);
				}
				}
			}
			totalDurationMin += durationMin;
			exercises.add(GetExercisesOfDateExercise.builder()
					.exerciseRecordId(exerciseRecordId)
					.exerciseName(exerciseName)
					.exerciseCategoryName(exerciseCategoryName)
					.calories(calories)
					.points(inGroup ? points : null)
					.exerciseOptionDetail(exerciseOptionDetail)
					.durationMin(durationMin)
					.build());
		}
		return GetExercisesOfDateResponse.builder()
				.exerciseDate(exerciseDate)
				.totalCalories(totalCalories)
				.totalPoints(inGroup ? totalPoints : null)
				.totalDurationMin(totalDurationMin)
				.recordedDates(recordedDates)
				.exercises(exercises)
				.build();
	}

	@Override
	public GetExerciseDetailResponse getExerciseDetail(Long exerciseRecordId) {
		///////////////////// 운동 카테고리 목록 검색 ///////////////////
		List<ExerciseCategoryInfo> exerciseCategoryList = new ArrayList<>();
		List<ExerciseCategoryVO> exerciseCategoryVOs = exerciseCategoryMapper.findAll();
		if (exerciseCategoryVOs == null || exerciseCategoryVOs.isEmpty())
			throw new NoExerciseCategoryException();
		exerciseCategoryVOs.forEach(exerciseCategoryVO -> 
			exerciseCategoryList.add(ExerciseCategoryInfo.builder()
					.exerciseCategoryId(exerciseCategoryVO.getExerciseCategoryId())
					.exerciseCategoryName(exerciseCategoryVO.getExerciseCategoryName())
					.build()));
		////////////////////////////////////////////////////////////
		
		ExerciseRecordVO exerciseRecordVO = 
				exerciseRecordMapper.findById(exerciseRecordId)
				.orElseThrow(() -> new NoExerciseRecordException());
		Long exerciseId = exerciseRecordVO.getExerciseId();
		///////////////////// 운동 카테고리 인덱스 검색 ///////////////////
		ExerciseCatalogVO exerciseCatalogVO = 
				exerciseCatalogMapper.findById(exerciseId)
				.orElseThrow(NoExerciseCatalogException::new);
		Long exerciseCategoryId = exerciseCatalogVO.getExerciseCategoryId();
		Integer exerciseCategoryIndex = 0;
		for (ExerciseCategoryVO exerciseCategoryVO : exerciseCategoryVOs) {
			if (exerciseCategoryVO.getExerciseCategoryId() != exerciseCategoryId)
				exerciseCategoryIndex++;
			else break;
		}
		if (exerciseCategoryVOs.size() == exerciseCategoryIndex)
			throw new NoExerciseCategoryException();
		//////////////////////////////////////////////////////////////
		
		///////////////////// 운동 목록 및 인덱스 검색 ////////////////////
		List<ExerciseCatalogVO> exerciseCatalogVOs = 
				exerciseCatalogMapper.findAllByExerciseCategoryId(exerciseCategoryId);
		if (exerciseCatalogVOs == null || exerciseCatalogVOs.isEmpty())
			throw new NoExerciseCatalogException();
		Integer exerciseIndex = 0;
		List<GetExerciseListResponse> exerciseList = new ArrayList<>();
		for (int index=0; index<exerciseCatalogVOs.size(); index++) {
			exerciseCatalogVO = exerciseCatalogVOs.get(index);
			exerciseList.add(GetExerciseListResponse.builder()
					.exerciseId(exerciseCatalogVO.getExerciseId())
					.exerciseName(exerciseCatalogVO.getName()).build());
			if (exerciseCatalogVO.getExerciseId() == exerciseId)
				exerciseIndex = index;
		}
		//////////////////////////////////////////////////////////////
		
		////////////////////////// 그 외 검색 //////////////////////////
		boolean inGroup = groupMemberService.checkInGroup(exerciseRecordVO.getUserId());
		// 소속 그룹 있는지 확인
		Integer durationMin = exerciseRecordVO.getDurationMin();
		Double calories = exerciseRecordVO.getCaloriesKcal();
		Integer points = inGroup ? exerciseRecordVO.getPoints() : null;
		Integer setNo = exerciseRecordVO.getSetNo();
		Integer reps = exerciseRecordVO.getReps();
		Integer weightKg = exerciseRecordVO.getWeightKg();
		//////////////////////////////////////////////////////////////
		return GetExerciseDetailResponse.builder()
				.exerciseCategoryList(exerciseCategoryList)
				.exerciseCategoryIndex(exerciseCategoryIndex)
				.exerciseList(exerciseList)
				.exerciseIndex(exerciseIndex)
				.calories(calories)
				.points(points)
				.setNo(setNo)
				.reps(reps)
				.weightKg(weightKg)
				.durationMin(durationMin)
				.build();
	}

	@Override
	public List<GetExerciseListResponse> getExerciseList(Long exerciseCategoryId) {
		List<ExerciseCatalogVO> exerciseCatalogVOs = 
				exerciseCatalogMapper.findAllByExerciseCategoryId(exerciseCategoryId);
		if (exerciseCatalogVOs == null || exerciseCatalogVOs.isEmpty())
			throw new NoExerciseCatalogException();
		List<GetExerciseListResponse> exerciseList = new ArrayList<>();
		exerciseCatalogVOs.forEach(exerciseCatalogVO -> 
			exerciseList.add(GetExerciseListResponse.builder()
				.exerciseId(exerciseCatalogVO.getExerciseId())
				.exerciseName(exerciseCatalogVO.getName()).build()));
		return exerciseList;
	}

	@Override
	@Transactional
	public AddExerciseResponse addExercise(
			Long userId, AddExerciseRequest addExerciseRequest) {
		Long exerciseId = addExerciseRequest.getExerciseId();
		exerciseCatalogMapper.findById(exerciseId)
			.orElseThrow(NoExerciseCatalogException::new);
		LocalDate exerciseDate = addExerciseRequest.getExerciseDate();
		Integer points = addExerciseRequest.getPoints();
		ExerciseRecordVO exerciseRecordVO = ExerciseRecordVO.builder()
				.exerciseDate(exerciseDate)
				.durationMin(addExerciseRequest.getDurationMin())
				.caloriesKcal(addExerciseRequest.getCalories())
				.points(points)
				.setNo(addExerciseRequest.getSetNo())
				.reps(addExerciseRequest.getReps())
				.weightKg(addExerciseRequest.getWeightKg())
				.exerciseId(exerciseId)
				.userId(userId)
				.build();
		if (exerciseRecordMapper.insert(exerciseRecordVO) < 1)
			throw new ExerciseRecordInsertException();
		// 같은 달 기록인 경우만 점수 반영
		if (LocalDate.now().getMonth() == exerciseDate.getMonth())
			userService.updateUserPoint(userId, points);
		if (LocalDate.now().equals(exerciseDate)) // 그 날 당일 기록만 인정
			badgeService.checkBadge(userId, CommandType.ADD_EXERCISE);
		return AddExerciseResponse.builder().build();
	}

	@Override
	@Transactional
	public UpdateExerciseResponse updateExercise(
			UpdateExerciseRequest updateExerciseRequest) {
		Long exerciseRecordId = updateExerciseRequest.getExerciseRecordId();
		ExerciseRecordVO exerciseRecordVO = 
				exerciseRecordMapper.findById(exerciseRecordId)
				.orElseThrow(NoExerciseRecordException::new);
		Long userId = exerciseRecordVO.getUserId();
		LocalDate exerciseDate = exerciseRecordVO.getExerciseDate();
		Integer newPoints = updateExerciseRequest.getPoints();
		Integer pointDelta = newPoints - exerciseRecordVO.getPoints();
		Long exerciseId = updateExerciseRequest.getExerciseId();
		exerciseCatalogMapper.findById(exerciseId)
			.orElseThrow(NoExerciseCatalogException::new);
		ExerciseRecordVO newExerciseRecordVO = ExerciseRecordVO.builder()
				.exerciseRecordId(exerciseRecordVO.getExerciseRecordId())
				.exerciseDate(exerciseDate)
				.durationMin(updateExerciseRequest.getDurationMin())
				.caloriesKcal(updateExerciseRequest.getCalories())
				.points(newPoints)
				.setNo(updateExerciseRequest.getSetNo())
				.reps(updateExerciseRequest.getReps())
				.weightKg(updateExerciseRequest.getWeightKg())
				.exerciseId(exerciseId)
				.userId(userId)
				.build();
		if (exerciseRecordMapper.updateById(newExerciseRecordVO) < 1)
			throw new ExerciseRecordUpdateException();
		// 같은 달 기록인 경우만 점수 반영
		if (LocalDate.now().getMonth() == exerciseDate.getMonth())
			userService.updateUserPoint(userId, pointDelta);
		return UpdateExerciseResponse.builder().build();
	}

	@Override
	@Transactional
	public DeleteExerciseResponse deleteExercise(Long exerciseRecordId) {
		ExerciseRecordVO exerciseRecordVO = 
				exerciseRecordMapper.findById(exerciseRecordId)
				.orElseThrow(NoExerciseRecordException::new);
		LocalDate exerciseDate = exerciseRecordVO.getExerciseDate();
		if (exerciseRecordMapper.deleteById(exerciseRecordId) < 1)
			throw new ExerciseRecordDeleteException();
		// 같은 달 기록인 경우만 점수 반영
		Long userId = exerciseRecordVO.getUserId();
		if (LocalDate.now().getMonth() == exerciseDate.getMonth())
			userService.updateUserPoint(userId, 
					-exerciseRecordVO.getPoints());
		boolean isEmpty = exerciseRecordMapper
				.findAllByUserIdAndExerciseDate(userId, exerciseDate)
				.stream().count() == 0;
		if (LocalDate.now().equals(exerciseDate) && isEmpty)
			badgeService.undoStreak(userId, StreakType.EXERCISE_RECORD);
		return DeleteExerciseResponse.builder().build();
	}
	
	@Override
	public ScoreExerciseResponse scoreExercise(
			Long userId, ScoreExerciseRequest scoreExerciseRequest) {
		UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
		ExerciseCatalogVO exerciseCatalogVO = 
				exerciseCatalogMapper.findById(scoreExerciseRequest.getExerciseId())
				.orElseThrow(NoExerciseCatalogException::new);
		Integer durationMin = scoreExerciseRequest.getDurationMin();
		Double metValue = exerciseCatalogVO.getMetValue();
		Double weight = userInfoDTO.getCurrentWeight();
		if (weight == null) throw new UserWeightException();
		Double calories = weight == 0 ? null : 
			((metValue*3.5*weight*durationMin)/1000)*5; 
		return ScoreExerciseResponse.builder()
				.calories(weight == 0 ? null : calories)
				.points(weight == 0 ? null : Math.round(calories/10))
				.build();
	}

	@Override
	public List<LocalDate> getMonthRecord(
			Long userId, Integer year, Integer month) {
		if (year < 1900 || 2100 < year || month < 1 || 12 < month)
			throw new ExcerciseDateInputException();
		return exerciseRecordMapper.
				findExerciseDateByUserIdAndExerciseYearAndExerciseMonth(
						userId, year, month);
		// 검증할 것이 뭐가 있을지 몰라서 일단 이렇게 반환
	}

	@Override
	public ExerciseRecordInfoDTO getExerciseRecordInfo(Long exerciseRecordId) {
		ExerciseRecordVO exerciseRecordVO = exerciseRecordMapper.findById(exerciseRecordId)
				.orElseThrow(NoExerciseRecordException::new);

		return ExerciseRecordInfoDTO.builder()
				.exerciseRecordId(exerciseRecordVO.getExerciseRecordId())
				.exerciseDate(exerciseRecordVO.getExerciseDate())
				.durationMin(exerciseRecordVO.getDurationMin())
				.caloriesKcal(exerciseRecordVO.getCaloriesKcal())
				.points(exerciseRecordVO.getPoints())
				.createdAt(exerciseRecordVO.getCreatedAt())
				.updatedAt(exerciseRecordVO.getUpdatedAt())
				.setNo(exerciseRecordVO.getSetNo())
				.reps(exerciseRecordVO.getReps())
				.weightKg(exerciseRecordVO.getWeightKg())
				.exerciseId(exerciseRecordVO.getExerciseId())
				.userId(exerciseRecordVO.getUserId())
				.build();
	}

	@Override
	public GetDailyAverageSpendedCalorieResponse getDailyAverageSpendedCalorie(Long userId) {
		// 그 주 하루 평균 소비 칼로리
		List<ExerciseRecordVO> exerciseRecordVOs = getSameWeekRecords(userId);
		Double spendedAverageCalorie = getTotalCalorieOfDaysAverage(exerciseRecordVOs);
		// 지난주 하루 평균 소비 칼로리
		exerciseRecordVOs = getLastWeekRecords(userId);
		Double lastWeekSpendedAverageCalorie = getTotalCalorieOfDaysAverage(exerciseRecordVOs);
		
		return GetDailyAverageSpendedCalorieResponse.builder()
				.spendedAverageCalorie(spendedAverageCalorie)
				.lastWeekSpendedAverageCalorie(lastWeekSpendedAverageCalorie)
				.percentFromLastWeek(spendedAverageCalorie/lastWeekSpendedAverageCalorie)
				.build();
	}

	private Double getTotalCalorieOfDaysAverage(List<ExerciseRecordVO> exerciseRecordVOs) {
		List<Double> totalCalorieOfDays = new ArrayList<>();
		int pointer = -1;
		LocalDate currentDate = LocalDate.of(0, 1, 1);
		for (ExerciseRecordVO exerciseRecordVO : exerciseRecordVOs) {
			if (!currentDate.equals(exerciseRecordVO.getExerciseDate())) {
				// 다른 날짜 -> pointer 증가, currentDate 업데이트, 배열에 빈 값 추가
				pointer++;
				currentDate = exerciseRecordVO.getExerciseDate();
				totalCalorieOfDays.add(0.0);
			}
			// pointer 위치 값 올리기
			totalCalorieOfDays.set(pointer, 
					totalCalorieOfDays.get(pointer)+
					exerciseRecordVO.getCaloriesKcal());
		}
		Double averageCalorie = totalCalorieOfDays.stream()
				.mapToDouble(Double::doubleValue)
				.average().orElse(0);
		return averageCalorie;
	}

	@Override
	public GetWeeklyTotalDurationResponse getWeeklyTotalDuration(Long userId) {
		List<ExerciseRecordVO> exerciseRecordVOsSameWeek = getSameWeekRecords(userId);
		Integer exerciseTimeSameWeek = exerciseRecordVOsSameWeek.stream()
				.map(ExerciseRecordVO::getDurationMin)
				.mapToInt(Integer::intValue).sum();
		List<ExerciseRecordVO> exerciseRecordVOsLastWeek = getLastWeekRecords(userId);
		Integer exerciseTimeLastWeek = exerciseRecordVOsLastWeek.stream()
				.map(ExerciseRecordVO::getDurationMin)
				.mapToInt(Integer::intValue).sum();
		Double percentFromLastWeek = 
				exerciseTimeLastWeek == 0 ? 0 : 
				(double)exerciseTimeSameWeek / 
				(double)exerciseTimeLastWeek;
		return GetWeeklyTotalDurationResponse.builder()
				.exerciseTimeSameWeek(exerciseTimeSameWeek)
				.exerciseTimeLastWeek(exerciseTimeLastWeek)
				.percentFromLastWeek(percentFromLastWeek)
				.build();
	}
	
	private List<ExerciseRecordVO> getSameWeekRecords(Long userId) {
		LocalDate today = LocalDate.now();
		int dayOfWeek = today.get(ChronoField.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek == 7 ? 0 : dayOfWeek;
		LocalDate sunday = today.minusDays(dayOfWeek); // 그 주의 일요일
		List<ExerciseRecordVO> exerciseRecordVOs = exerciseRecordMapper
				.findAllByUserIdAndExerciseDateBetweenStartDateAndEndDateOrderByExerciseDate(userId, sunday, today);
		if (exerciseRecordVOs == null) throw new NoExerciseRecordException();
		return exerciseRecordVOs;
	}
	
	private List<ExerciseRecordVO> getLastWeekRecords(Long userId) {
		LocalDate today = LocalDate.now().minusDays(1);
		int dayOfWeek = today.get(ChronoField.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek == 7 ? 0 : dayOfWeek;
		LocalDate sunday = today.minusDays(dayOfWeek); // 그 주의 일요일
		LocalDate lastSaturday = sunday.minusDays(1);
		LocalDate lastSunday = sunday.minusDays(7);
		List<ExerciseRecordVO> exerciseRecordVOs = exerciseRecordMapper
				.findAllByUserIdAndExerciseDateBetweenStartDateAndEndDateOrderByExerciseDate(userId, lastSunday, lastSaturday);
		if (exerciseRecordVOs == null) throw new NoExerciseRecordException();
		return exerciseRecordVOs;
	}

	@Override
	public List<LocalDate> getRecordedDates(Long userId) {
		List<ExerciseRecordVO> exerciseRecordVOs = 
				exerciseRecordMapper.findAllByUserId(userId);
		if (exerciseRecordVOs == null)
			throw new NoExerciseRecordException();
		List<LocalDate> recordedDates = new ArrayList<>();
		exerciseRecordVOs.stream()
			.map(ExerciseRecordVO::getExerciseDate)
			.filter(exerciseDate -> !recordedDates.contains(exerciseDate))
			.forEach(exerciseDate -> recordedDates.add(exerciseDate));
		return recordedDates;
	}
}
