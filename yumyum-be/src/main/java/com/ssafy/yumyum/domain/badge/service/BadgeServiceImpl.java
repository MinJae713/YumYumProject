package com.ssafy.yumyum.domain.badge.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.common.exception.commonExceptions.DeleteFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.badge.dto.response.CheckBadgeResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetBadgeListResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeCategoryException;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeException;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeListException;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeProgressException;
import com.ssafy.yumyum.domain.badge.exception.NoUserBadgeListException;
import com.ssafy.yumyum.domain.badge.repository.badge.BadgeMapper;
import com.ssafy.yumyum.domain.badge.repository.badge.BadgeVO;
import com.ssafy.yumyum.domain.badge.repository.badgeCategory.BadgeCategoryMapper;
import com.ssafy.yumyum.domain.badge.repository.badgeCategory.BadgeCategoryVO;
import com.ssafy.yumyum.domain.badge.repository.badgeProgress.BadgeProgressMapper;
import com.ssafy.yumyum.domain.badge.repository.badgeProgress.BadgeProgressVO;
import com.ssafy.yumyum.domain.badge.repository.userBadge.UserBadgeMapper;
import com.ssafy.yumyum.domain.badge.repository.userBadge.UserBadgeVO;
import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;
import com.ssafy.yumyum.domain.badge.types.BadgeType;
import com.ssafy.yumyum.domain.badge.types.CommandType;
import com.ssafy.yumyum.domain.badge.types.StreakType;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberMapper;
import com.ssafy.yumyum.domain.meal.exception.NoMealDataException;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.meal.MealVO;
import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeVO;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemVO;
import com.ssafy.yumyum.domain.meal.service.MealDataGetService;
import com.ssafy.yumyum.domain.meal.service.dto.FoodAmountDTO;
import com.ssafy.yumyum.domain.meal.service.dto.TotalNutrionDTO;
import com.ssafy.yumyum.domain.meal.types.MealCreatorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService{
	
	// In domain
	private final BadgeMapper badgeMapper;
	private final BadgeCategoryMapper badgeCategoryMapper;
	private final UserBadgeMapper userBadgeMapper;
	private final BadgeProgressMapper badgeProgressMapper;
	private final GroupMemberMapper groupMemberMapper;
	
	// etc domain
	private final MealDataGetService mealDataGetService;

	@Override
	public GetBadgeListResponse getBadgeList(Long userId) {
		List<BadgeVO> badgeVOs = badgeMapper.findAll();
		if (badgeVOs == null || badgeVOs.isEmpty())
			throw new NoBadgeListException();
		Integer fullCount = badgeVOs.size();
		Integer achievedCount = 0;
		List<GetBadgeListBadgeDTO> badges = new ArrayList<>();
		for (BadgeVO badgeVO : badgeVOs) {
			BadgeCategoryVO badgeCategoryVO = 
					badgeCategoryMapper.findById(badgeVO.getBadgeCategoryId())
					.orElseThrow(() -> new NoBadgeCategoryException());
			UserBadgeVO userBadgeVO = userBadgeMapper
					.findByUserIdAndBadgeId(userId, badgeVO.getBadgeId())
					.orElse(null);
			Boolean isAchieved = userBadgeVO != null;
			LocalDateTime achievedAt = null;
			if (isAchieved) {
				achievedAt = userBadgeVO.getAchievedAt();
				achievedCount++;
			}
			badges.add(GetBadgeListBadgeDTO.builder()
					.badgeId(badgeVO.getBadgeId())
					.badgeTitle(badgeVO.getBadgeTitle())
					.description(badgeVO.getDescription())
					.badgeCategoryName(badgeCategoryVO.getBadgeCategoryName())
					.badgeImageUrl(badgeVO.getImageUrl())
					.isAchieved(isAchieved)
					.achievedAt(achievedAt)
					.build());
		}
		Double achievedRate = (double)achievedCount / (double)fullCount;
		return GetBadgeListResponse.builder()
				.fullCount(fullCount)
				.achievedCount(achievedCount)
				.achievedRate(achievedRate)
				.badges(badges)
				.build();
	}

	@Override
	public List<GetBadgeListBadgeDTO> getRecentBadgeList(Long userId) {
		List<UserBadgeVO> userBadgeVOs = userBadgeMapper
				.findTop5ByUserIdOrderByAchievedAtDesc(userId);
		if (userBadgeVOs == null) throw new NoUserBadgeListException();
		List<GetBadgeListBadgeDTO> getBadgeListBadge = new ArrayList<>();
		userBadgeVOs.forEach(userBadgeVO -> {
			Long badgeId = userBadgeVO.getBadgeId();
			BadgeVO badgeVO = badgeMapper.findById(badgeId)
					.orElseThrow(NoBadgeException::new);
			String badgeTitle = badgeVO.getBadgeTitle();
			String description = badgeVO.getDescription();
			BadgeCategoryVO badgeCategoryVO = 
					badgeCategoryMapper.findById(badgeVO.getBadgeCategoryId())
					.orElseThrow(NoBadgeCategoryException::new);
			String badgeCategoryName = badgeCategoryVO.getBadgeCategoryName();
			LocalDateTime achievedAt = userBadgeVO.getAchievedAt();
			getBadgeListBadge.add(GetBadgeListBadgeDTO.builder()
					.badgeId(badgeId)
					.badgeTitle(badgeTitle)
					.description(description)
					.badgeCategoryName(badgeCategoryName)
					.badgeImageUrl(badgeVO.getImageUrl())
					.isAchieved(true)
					.achievedAt(achievedAt)
					.build());
		});
		return getBadgeListBadge;
	}
	
	@Override
	public List<GetStreakRecordResponse> getStreakRecord(Long userId) {
		List<BadgeProgressVO> badgeProgressVOs = 
				badgeProgressMapper.findAllByUserId(userId);
		if (badgeProgressVOs == null)
			throw new NoBadgeProgressException();
		List<GetStreakRecordResponse> getStreakRecordResponses = new ArrayList<>();
		badgeProgressVOs.forEach(badgeProgressVO -> {
			StreakType streakType = badgeProgressVO.getStreakType();
			getStreakRecordResponses.add(
					GetStreakRecordResponse.builder()
						.streakType(streakType)
						.name(streakType.getName())
						.longestStreak(badgeProgressVO.getLongestStreak())
						.build());
		});
		return getStreakRecordResponses;
	}

	@Override
	@Transactional
	public CheckBadgeResponse checkBadge(Long userId, CommandType commandType) {
		BadgeType[] badgeTypes = commandType.getBadgeTypes();
		for (BadgeType badgeType : badgeTypes) {
			if (checkAchieved(userId, badgeType)) continue;
			switch (badgeType) {
				case MEAL_STREAK_RECORD_WEEK -> checkStreakRecord(userId, badgeType, StreakType.MEAL_RECORD, 7);
		        case MEAL_STREAK_RECORD_FULL_MOON -> checkStreakRecord(userId, badgeType, StreakType.MEAL_RECORD, 15);
		        case MEAL_STREAK_RECORD_MONTH -> checkStreakRecord(userId, badgeType, StreakType.MEAL_RECORD, 30);
		        
		        case EXERCISE_STREAK_RECORD_WEEK -> checkStreakRecord(userId, badgeType, StreakType.EXERCISE_RECORD, 7);
		        case EXERCISE_STREAK_RECORD_FULL_MOON -> checkStreakRecord(userId, badgeType, StreakType.EXERCISE_RECORD, 15);
		        case EXERCISE_STREAK_RECORD_MONTH -> checkStreakRecord(userId, badgeType, StreakType.EXERCISE_RECORD, 30);
		        
		        case NUTRIENT_EXPERT -> checkNutrientExpert(userId, badgeType);
		        
		        case ACHIEVE_GROUP_THIRD -> checkAchieveGroupThird(userId, badgeType);
		        case ACHIEVE_GROUP_NO_1 -> checkAchieveGroupNo1(userId, badgeType);
		        
		        case SELECT_MEAL_AI_10 -> checkSelectMealByCreator(userId, badgeType, MealCreatorType.AI, 10);
		        case SELECT_MEAL_GROUP_10 -> checkSelectMealByCreator(userId, badgeType, MealCreatorType.GROUP, 10);
		        case FIRST_SIGN_UP, FIRST_MEAL_RECORD,
		        	FIRST_EXERCISE_RECORD, FIRST_SIGN_UP_GROUP,
		        	WELL_DONE -> insertUserBadge(userId, badgeType);
			}
		}
		
		return null;
	}
	private void checkStreakRecord(Long userId, BadgeType badgeType, 
			StreakType streakType, int checkDates) {
		LocalDate today = LocalDate.now();
		BadgeProgressVO badgeProgressVO = 
				badgeProgressMapper
				.findByUserIdAndStreakType(userId, streakType)
				.orElse(null);
		if (badgeProgressVO == null) {
			badgeProgressVO = BadgeProgressVO.builder()
					.streakType(streakType)
					.startDate(today)
					.longestStreak(1L)
					.lastDate(today)
					.userId(userId)
					.build();
			if (badgeProgressMapper.insert(badgeProgressVO) < 1)
				throw new InsertFailedException("뱃지 연속 기록 삽입에 실패했습니다.");
		} else {
			Long betweenLastAndNow = ChronoUnit.DAYS.between(
					badgeProgressVO.getLastDate(), today);
			if (betweenLastAndNow == 0) return; 
			else if (betweenLastAndNow > 1) { 
				badgeProgressVO.setStartDate(today);
				badgeProgressVO.setLongestStreak(1L);
				badgeProgressVO.setLastDate(today);
				if (badgeProgressMapper.updateById(badgeProgressVO) < 1)
					throw new UpdateFailedException("뱃지 연속 기록 수정에 실패했습니다");
			} else { 
				Long longestStreak = badgeProgressVO.getLongestStreak();
				badgeProgressVO.setLongestStreak(++longestStreak);
				badgeProgressVO.setLastDate(today);
				if (badgeProgressMapper.updateById(badgeProgressVO) < 1)
					throw new UpdateFailedException("뱃지 연속 기록 수정에 실패했습니다");
				if (longestStreak == checkDates) insertUserBadge(userId, badgeType);
			}
		}
	}
	private void checkSelectMealByCreator(Long userId, BadgeType badgeType, 
			MealCreatorType creatorName, int checkCount) {
		List<MealVO> mealVOs = mealDataGetService.findMealByUserId(userId);
		if (mealVOs == null) throw new NoMealDataException();
		int count = 0;
		for (MealVO mealVO : mealVOs) {
			MealCreatorTypeVO mealCreatorTypeVO = 
					mealDataGetService.findMealCreatorTypeById(mealVO.getMealCreatorTypeId());
			
			count += mealCreatorTypeVO.getMealCreatorTypeName() == creatorName 
					&& !mealVO.getIsRecommendation() ? 1 : 0;
		}
		if (count >= checkCount) insertUserBadge(userId, badgeType);
	}
	// 누적 섭취량 달성 -> 아래는 차후 properties로 분리 예정
	private Double targetCumulativeCarbohydrate = 500.0;
	private Double targetCumulativeProtein = 300.0;
	private Double targetCumulativeFat = 200.0;
	private Double targetCumulativeCalcium = 103.0;
	private Double targetCumulativeSugar = 21.0;
	private Double targetCumulativeFiber = 12.0;
	private void checkNutrientExpert(Long userId, BadgeType badgeType) {
		List<MealVO> mealVOs = mealDataGetService.findMealByUserId(userId);
		List<FoodAmountDTO> foodAmountDTOs = new ArrayList<>();
		mealVOs.stream()
			.filter(mealVO -> !mealVO.getIsRecommendation())
			.forEach(mealVO -> {
				List<MealItemVO> mealItemVOs = 
						mealDataGetService.findMealItemByMealId(mealVO.getMealId());
				mealItemVOs.forEach(mealItemVO -> 
					foodAmountDTOs.add(FoodAmountDTO.builder()
							.foodId(mealItemVO.getFoodId())
							.amount(mealItemVO.getAmount())
							.build()));
			});
		TotalNutrionDTO totalNutrion = getTotalNutrion(foodAmountDTOs);
		boolean carbohydrateAchieved = 
				totalNutrion.getTotalCarbohydrate() >= 
				targetCumulativeCarbohydrate;
		boolean proteinAchieved = 
				totalNutrion.getTotalProtein() >=
				targetCumulativeProtein;
		boolean fatAchieved = 
				totalNutrion.getTotalFat() >=
				targetCumulativeFat;
		boolean calciumAchieved = 
				totalNutrion.getTotalCalcium() >=
				targetCumulativeCalcium;
		boolean sugarAchieved = 
				totalNutrion.getTotalSugar() >= 
				targetCumulativeSugar;
		boolean fiberAchieved = 
				totalNutrion.getTotalFiber() >= 
				targetCumulativeFiber;
		if (carbohydrateAchieved && proteinAchieved && 
				fatAchieved && calciumAchieved && 
				sugarAchieved && fiberAchieved)
			insertUserBadge(userId, badgeType);
	}
	private TotalNutrionDTO getTotalNutrion(List<FoodAmountDTO> totalFoodRequests) {
		TotalNutrionDTO totalNutrion = new TotalNutrionDTO(); 
		for (FoodAmountDTO foodUnit : totalFoodRequests) {
			Long foodId = foodUnit.getFoodId();
			FoodVO foodVO = mealDataGetService.findFoodById(foodId);
			Double amount = foodUnit.getAmount();
			totalNutrion.addTotalNutrion(foodVO, amount);
		}
		return totalNutrion;
	}
	// 그룹 내 달성 -> 그룹원 10명 이상이라는 조건이 있긴 하나.... 일단 묻어둡니다
	private void checkAchieveGroupThird(Long userId, BadgeType badgeType) {
		Integer rank = groupMemberMapper.findBestRankingByUserId(userId);
	    if (rank != null && rank <= 3) insertUserBadge(userId, badgeType);
	}
	private void checkAchieveGroupNo1(Long userId, BadgeType badgeType) {
		Integer rank = groupMemberMapper.findBestRankingByUserId(userId);
	    if (rank != null && rank == 1) insertUserBadge(userId, badgeType);
	}
	@Override
	public void undoStreak(Long userId, StreakType streakType) {
		// 식단 및 운동 기록 삭제 시, 연속 기록 날짜를 전날로 돌리기
		BadgeProgressVO badgeProgressVO = 
				badgeProgressMapper
				.findByUserIdAndStreakType(userId, streakType)
				.orElseThrow(NoBadgeProgressException::new);
		Long longestStreak = badgeProgressVO.getLongestStreak();
		// 연속 기록 시작 - 끝 날짜 비교
		if (longestStreak == 1L) {
			// 첫 기록이었거나 그날 기록 했는데 연속 기록이 깨져버린 경우 -> 기록 삭제
			Long badgeProgressId = badgeProgressVO.getBadgeProgressId();
			if (badgeProgressMapper.deleteById(badgeProgressId) < 1)
				throw new DeleteFailedException("뱃지 연속 기록 삭제에 실패했습니다.");
		} else {
			// 연속 기록이 이틀 이상 지속된 경우 -> 전날로 돌림
			badgeProgressVO.setLastDate(badgeProgressVO.getLastDate().minusDays(1));
			badgeProgressVO.setLongestStreak(badgeProgressVO.getLongestStreak()-1);
			if (badgeProgressMapper.updateById(badgeProgressVO) < 1)
				throw new UpdateFailedException("뱃지 연속 기록 수정에 실패했습니다.");
		}
	}
	// common
	private boolean checkAchieved(Long userId, BadgeType badgeType) {
		List<UserBadgeVO> userBadgeVOs = userBadgeMapper.findAllByUserId(userId);
		for (UserBadgeVO userBadgeVO : userBadgeVOs) {
			BadgeVO badgeVO = badgeMapper.findById(userBadgeVO.getBadgeId())
					.orElseThrow(NoBadgeException::new);
			if (badgeVO.getBadgeType() == badgeType) return true;
		}
		return false;
	}
	private void insertUserBadge(Long userId, BadgeType badgeType) {
		BadgeVO badgeVO = badgeMapper.findByBadgeType(badgeType)
				.orElseThrow(NoBadgeException::new);
		UserBadgeVO userBadgeVO = UserBadgeVO.builder()
				.userId(userId)
				.badgeId(badgeVO.getBadgeId())
				.build();
		if (userBadgeMapper.insert(userBadgeVO) < 1)
			throw new InsertFailedException();
	}
}
