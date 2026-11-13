package com.ssafy.yumyum.domain.meal.service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.common.exception.commonExceptions.DeleteFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.common.util.ParsedAmount;
import com.ssafy.yumyum.domain.badge.service.BadgeService;
import com.ssafy.yumyum.domain.badge.types.CommandType;
import com.ssafy.yumyum.domain.badge.types.StreakType;
import com.ssafy.yumyum.domain.group.exception.GroupMemberNotFoundException;
import com.ssafy.yumyum.domain.group.service.GroupMemberService;
import com.ssafy.yumyum.domain.group.service.dto.GroupInfoDTO;
import com.ssafy.yumyum.domain.meal.dto.request.AddMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.AddMealRequest.FoodRequest;
import com.ssafy.yumyum.domain.meal.dto.request.ApplyRecommendMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.TotalFoodRequest;
import com.ssafy.yumyum.domain.meal.dto.request.UpdateMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.UpdateMealRequest.UpdateFood;
import com.ssafy.yumyum.domain.meal.dto.response.AddMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.ApplyRecommendMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.DeleteMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse.IntakeValue;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse.GetMealDetailFood;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse.GetMealOfDateMeal;
import com.ssafy.yumyum.domain.meal.dto.response.GetRecommendMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse.NutrientDTO;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse.PeriodNutritionDTO;
import com.ssafy.yumyum.domain.meal.dto.response.SearchFoodResponse;
import com.ssafy.yumyum.domain.meal.dto.response.TotalFoodResponse;
import com.ssafy.yumyum.domain.meal.dto.response.UpdateMealResponse;
import com.ssafy.yumyum.domain.meal.exception.ExistMealException;
import com.ssafy.yumyum.domain.meal.exception.ExistMealItemException;
import com.ssafy.yumyum.domain.meal.exception.MealDateInputException;
import com.ssafy.yumyum.domain.meal.exception.MealInsertException;
import com.ssafy.yumyum.domain.meal.exception.NoFoodException;
import com.ssafy.yumyum.domain.meal.exception.NoFoodUnitException;
import com.ssafy.yumyum.domain.meal.exception.NoGroupMealException;
import com.ssafy.yumyum.domain.meal.exception.NoMealAIDetailException;
import com.ssafy.yumyum.domain.meal.exception.NoMealCreatorTypeException;
import com.ssafy.yumyum.domain.meal.exception.NoMealDataException;
import com.ssafy.yumyum.domain.meal.exception.NoMealItemDataException;
import com.ssafy.yumyum.domain.meal.exception.NoMealTypeException;
import com.ssafy.yumyum.domain.meal.exception.NoUpdateFoodTypeException;
import com.ssafy.yumyum.domain.meal.repository.food.FoodMapper;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitMapper;
import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitVO;
import com.ssafy.yumyum.domain.meal.repository.groupMeal.GroupMealMapper;
import com.ssafy.yumyum.domain.meal.repository.groupMeal.GroupMealVO;
import com.ssafy.yumyum.domain.meal.repository.meal.MealMapper;
import com.ssafy.yumyum.domain.meal.repository.meal.MealVO;
import com.ssafy.yumyum.domain.meal.repository.mealAiDetail.MealAiDetailMapper;
import com.ssafy.yumyum.domain.meal.repository.mealAiDetail.MealAiDetailVO;
import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeMapper;
import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeVO;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemMapper;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemVO;
import com.ssafy.yumyum.domain.meal.repository.mealType.MealTypeMapper;
import com.ssafy.yumyum.domain.meal.repository.mealType.MealTypeVO;
import com.ssafy.yumyum.domain.meal.service.dto.DailyTargetNutrionDTO;
import com.ssafy.yumyum.domain.meal.service.dto.FoodAmountDTO;
import com.ssafy.yumyum.domain.meal.service.dto.GetMealOfDateFoodDTO;
import com.ssafy.yumyum.domain.meal.service.dto.MealInfoDTO;
import com.ssafy.yumyum.domain.meal.service.dto.MealTargetNutrionDTO;
import com.ssafy.yumyum.domain.meal.service.dto.PointNutrionDTO;
import com.ssafy.yumyum.domain.meal.service.dto.TotalNutrionDTO;
import com.ssafy.yumyum.domain.meal.types.MealCreatorType;
import com.ssafy.yumyum.domain.meal.types.MealType;
import com.ssafy.yumyum.domain.meal.types.UpdateFoodType;
import com.ssafy.yumyum.domain.research.repository.ResearchLogMapper;
import com.ssafy.yumyum.domain.research.repository.ResearchLogVO;
import com.ssafy.yumyum.domain.user.exception.UserHeightException;
import com.ssafy.yumyum.domain.user.service.UserService;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;
import com.ssafy.yumyum.domain.user.types.Gender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService{
	
	// domain repositories
	private final FoodMapper foodMapper;
	private final FoodUnitMapper foodUnitMapper;
	private final MealMapper mealMapper;
	private final MealCreatorTypeMapper mealCreatorTypeMapper;
	private final MealItemMapper mealItemMapper;
	private final MealTypeMapper mealTypeMapper;
	private final GroupMealMapper groupMealMapper;
	private final MealAiDetailMapper mealAiDetailMapper;
	
	// etc domain repositories
	private final UserService userService;
	private final ResearchLogMapper researchLogMapper;
	private final GroupMemberService groupMemberService;
	private final BadgeService badgeService;

	@Override
	public GetMealOfDateResponse getMealOfDate(Long userId, LocalDate mealDate) {
		// 식단 탭 캘린더에서 날짜 선택
    	// 유저 아이디, 선택 날짜 갖고 해당 유저의 그날 식단 목록 반환
    	// 서비스는 날짜, 유저 아이디 받아서 GetMealResponse 반환
		// 영양소 계산은 재고려 필요(UnitNutrion) -> 이거 계산 방식만 정해지면 완성 ㄱㄴ
		boolean inGroup = groupMemberService.checkInGroup(userId);
		List<MealVO> mealVOs = mealMapper.findAllByUserIdAndMealDate(userId, mealDate);
		if (mealVOs == null) throw new NoMealDataException();
		Double mealTotalCalorie = 0.0;
		Integer mealTotalPoint = 0;
		Integer mealTotalBenefitPoint = 0;
		List<LocalDate> recordedDates = getMonthRecord(userId, 
				mealDate.getYear(), mealDate.getMonth().getValue());
		List<GetMealOfDateMeal> meals = new ArrayList<>();
		
		/////////////// 그 날 먹은 세 끼니 정보 검색 ///////////////
		for (MealVO mealVO : mealVOs) {
			if (mealVO.getIsRecommendation()) continue;
			// 식단 하나 (아침, 점심, 저녁, 간식)
			Long mealId = mealVO.getMealId();
			MealTypeVO mealTypeVO = mealTypeMapper.findById(mealVO.getMealTypeId())
					.orElseThrow(NoMealTypeException::new);
			MealCreatorTypeVO mealCreatorTypeVO = mealCreatorTypeMapper.findById(mealVO.getMealCreatorTypeId())
					.orElseThrow(NoMealCreatorTypeException::new);
			MealCreatorType mealCreatorType = mealCreatorTypeVO.getMealCreatorTypeName();
			Integer totalPoint = mealVO.getPoint();
			Integer benefitPoint = mealCreatorType.getBenefitPoint();
			
			List<MealItemVO> mealItemVOs = mealItemMapper.findAllByMealId(mealId);
			if (mealItemVOs == null || mealItemVOs.isEmpty()) 
				throw new NoMealItemDataException();
			TotalNutrionDTO totalNutrion = new TotalNutrionDTO();
			List<GetMealOfDateFoodDTO> foods = new ArrayList<>();
			
			/////////////// 아침, 점심, 저녁, 간식 정보 검색 ///////////////
			for (MealItemVO mealItemVO : mealItemVOs) {
				// 식단 별 음식
				FoodVO foodVO = foodMapper.findById(mealItemVO.getFoodId())
						.orElseThrow(NoFoodException::new);
				Long foodId = foodVO.getFoodId();
				String name = foodVO.getFoodName();
				FoodUnitVO foodUnitVO = foodUnitMapper.findById(foodVO.getFoodUnitId())
						.orElseThrow(NoFoodUnitException::new);
				String unitName = foodUnitVO.getUnitName();
				Double amount = mealItemVO.getAmount();
				totalNutrion.addTotalNutrion(foodVO, amount);
				foods.add(GetMealOfDateFoodDTO.builder()
						.foodId(foodId)
						.name(name)
						.unitName(unitName)
						.amount(amount)
						.build());
			}
			/////////////////////////////////////////////////////////
			mealTotalCalorie += totalNutrion.getTotalCalorie();
			mealTotalPoint += totalPoint;
			mealTotalBenefitPoint += benefitPoint;
			meals.add(GetMealOfDateMeal.builder()
					.mealId(mealId)
					.mealTypeName(mealTypeVO.getMealTypeName().getLabel())
					.mealCreatorName(mealCreatorType)
					.totalPoint(inGroup ? totalPoint : null)
					.benefitPoint(inGroup ? benefitPoint : null)
					.totalCalorie(totalNutrion.getTotalCalorie())
					.totalCarbohydrate(totalNutrion.getTotalCarbohydrate())
					.totalProtein(totalNutrion.getTotalProtein())
					.totalFat(totalNutrion.getTotalFat())
					.foods(foods)
					.build());
		}
		/////////////////////////////////////////////////////////
		return GetMealOfDateResponse.builder()
				.mealTotalCalorie(mealTotalCalorie)
				.mealTotalPoint(inGroup ? mealTotalPoint : null)
				.mealTotalBenefitPoint(inGroup ? mealTotalBenefitPoint : null)
				.recordedDates(recordedDates)
				.meals(meals).build();
	}
	
	private void checkExistMeal(Long userId, LocalDate mealDate, MealType mealType) {
		List<MealVO> currentMealVOs = mealMapper.findAllByUserIdAndMealDate(userId, mealDate);
		currentMealVOs.stream()
			.filter(currentMealVO -> !currentMealVO.getIsRecommendation())
			.forEach(currentMealVO -> {
				// 본인이 먹은 식단인 애들 중,
				// 입력 받은 끼니와 동일 끼니의 식단 데이터가 있으면 예외 발생
				MealTypeVO currentMealTypeVO = mealTypeMapper
						.findById(currentMealVO.getMealTypeId())
						.orElseThrow(NoMealTypeException::new);
				
				if (currentMealTypeVO.getMealTypeName() == mealType)
					throw new ExistMealException();
			});
	}
	
	private MealType getMealTypeByName(String name) {
		for (MealType mealType : MealType.values())
			if (mealType.getLabel().equals(name))
				return mealType;
		return null;
	}

	@Override
	@Transactional
	public AddMealResponse addMeal(Long userId, AddMealRequest addMealRequest) {
		///////////////// 식단 추가 /////////////////
		LocalDate mealDate = addMealRequest.getMealDate();
		
		// 식단 생성 주체 확인
		String mealCreatorName = addMealRequest.getMealCreatorName();
		MealCreatorTypeVO mealCreatorTypeVO = mealCreatorTypeMapper
				.findByMealCreatorTypeName(mealCreatorName)
				.orElseThrow(NoMealCreatorTypeException::new);
		Long mealCreatorTypeId = mealCreatorTypeVO.getMealCreatorTypeId();
		MealCreatorType mealCreatorType = mealCreatorTypeVO.getMealCreatorTypeName();
		
		// 끼니 확인 -> AI, 그룹장 입력이면 끼니(mealTypeId)가 null로 지정됨
		Long mealTypeId = null;
		if (mealCreatorType == MealCreatorType.USER) {
			MealType mealType = getMealTypeByName(addMealRequest.getMealTypeName());
			if (mealType == null) throw new NoMealTypeException();
			MealTypeVO mealTypeVO = mealTypeMapper.findByMealTypeName(mealType)
					.orElseThrow(NoMealTypeException::new);
			// 유저 입력이면 이미 있는 끼니인지 확인 -> 이미 있다면 예외 발생
			checkExistMeal(userId, mealDate, mealType);
			mealTypeId = mealTypeVO.getMealTypeId();
		}
		
		// 그룹장이 등록하는 상황인지 확인 -> 그룹원 아이디 해당 레코드가 있는지 확인
		// 아이디가 없거나, 있어도 레코드가 없다면 예외 발생
		// 레코드가 있어도 해당 값의 유저 아이디가 입력 유저 아이디와 다르면 예외 발생
		Long groupMemberId = addMealRequest.getGroupMemberId(); 
		if (mealCreatorType == MealCreatorType.GROUP && 
				!groupMemberService.checkValidMember(groupMemberId, userId))
			throw new GroupMemberNotFoundException();
		
		// Meal 테이블 레코드 생성
		Integer totalPoint = addMealRequest.getTotalPoint();
		Double totalCalorie = addMealRequest.getTotalCalorie();
		boolean isRecommendaton = mealCreatorType != MealCreatorType.USER;
		MealVO mealVO = MealVO.builder()
				.mealDate(mealDate)
				.totalKcal(totalCalorie)
				.point(totalPoint)
				.isRecommendation(isRecommendaton)
				.userId(userId) // 식단 적용 대상 아이디
				.mealTypeId(mealTypeId)
				.mealCreatorTypeId(mealCreatorTypeId)
				.build();
		if (mealMapper.insert(mealVO) < 1) throw new MealInsertException();
		//////////////////////////////////////////
		
		//////// 입력 식단에 대한 음식 목록 추가 ////////
		Long mealId = mealMapper.maxId();
		List<FoodRequest> foods = addMealRequest.getFoods();
		foods.forEach(food -> {
			Long foodId = food.getFoodId();
			foodMapper.findById(foodId).orElseThrow(NoFoodException::new);
			if (mealItemMapper.findByMeallIdAndFoodId(mealId, foodId).isPresent())
				throw new ExistMealItemException();
			Double amount = food.getAmount();
			MealItemVO mealItemVO = MealItemVO.builder()
					.amount(amount)
					.mealId(mealId)
					.foodId(foodId)
					.build();
			if (mealItemMapper.insert(mealItemVO) < 1)
				throw new InsertFailedException("식단 음식 삽입에 실패했습니다.");
		});
		//////////////////////////////////////////
		// 생성 주체 따라 식단 삽입 후 동작 지정
		switch (mealCreatorType) {
		case USER: {
			// 유저가 만든 식단이면 점수 추가 및 뱃지 체크
			// 같은 달 기록 -> 점수 추가
			if (LocalDate.now().getMonth() == mealDate.getMonth())
				userService.updateUserPoint(userId, totalPoint);
			if (LocalDate.now().equals(mealDate)) // 그 날 당일 기록만 인정
				badgeService.checkBadge(userId, CommandType.ADD_MEAL);
			break;
		}
		case GROUP: {
			// 그룹장 추천 식단이면 어느 그룹원 식단(group meal)인지 데이터도 넣을 것
			if (groupMealMapper.insert(
					GroupMealVO.builder().mealId(mealId)
					.groupMemberId(groupMemberId).build()) < 1)
				throw new InsertFailedException("그룹장 식단 목록 삽입에 실패했습니다.");
			break;
		}
		case AI: {} // AI는 아무것도 안함
		}
		return new AddMealResponse();
	}

	@Override
	public GetMealDetailResponse getMealDetail(Long mealId) {
		// 식단 페이지 식단 상세 조회 및 메인 페이지 추천 식단 상세 조회 시 호출
		// 이에 따라, meal type은 null 허용하도록 수정
		// 추천 식단 상세 조회 시점에는 meal type이 지정되어있지 않기 때문임
		// 단, 추천 식단 등록 시점에는 meal type을 반드시 지정해야 할 것!!
		MealVO mealVO = mealMapper.findById(mealId)
				.orElseThrow(NoMealDataException::new);
		//////////////// 식단 음식들 정보 검색 ////////////////
		List<MealItemVO> mealItemVOs = mealItemMapper.findAllByMealId(mealId);
		if (mealItemVOs == null || mealItemVOs.isEmpty()) 
			throw new NoMealItemDataException();
		List<GetMealDetailFood> foods = new ArrayList<>();
		mealItemVOs.forEach(mealItemVO -> {
			Long foodId = mealItemVO.getFoodId();
			FoodVO foodVO = foodMapper.findById(foodId)
					.orElseThrow(NoFoodException::new);
			FoodUnitVO foodUnitVO = foodUnitMapper
					.findById(foodVO.getFoodUnitId())
					.orElseThrow(NoFoodUnitException::new);
			String name = foodVO.getFoodName();
			String unitName = foodUnitVO.getUnitName();
			Double amount = mealItemVO.getAmount();
			TotalNutrionDTO totalNutrion = new TotalNutrionDTO();
			totalNutrion.addTotalNutrion(foodVO, amount);
			foods.add(GetMealDetailFood.builder()
					.foodId(foodId)
					.name(name)
					.unitName(unitName)
					.amount(amount)
					.calorie(totalNutrion.getTotalCalorie())
					.carbohydrate(totalNutrion.getTotalCarbohydrate())
					.protein(totalNutrion.getTotalProtein())
					.fat(totalNutrion.getTotalFat()).build());
		});
		///////////////////////////////////////////////////
		
		//////////////////// 식단 정보 검색 //////////////////
		LocalDate mealDate = mealVO.getMealDate();
		MealTypeVO mealTypeVO = mealVO.getIsRecommendation() ? null : 
				mealTypeMapper.findById(mealVO.getMealTypeId())
				.orElseThrow(NoMealTypeException::new); 
		// 수정 - 조회하는 식단이 AI, 그룹장이 등록한 식단이라면 식단 종류가 지정되지 않도록!
		MealCreatorTypeVO mealCreatorTypeVO = mealCreatorTypeMapper
				.findById(mealVO.getMealCreatorTypeId())
				.orElseThrow(NoMealCreatorTypeException::new);
		MealCreatorType mealCreatorTypeName = mealCreatorTypeVO.getMealCreatorTypeName();
		boolean inGroup = groupMemberService.checkInGroup(mealVO.getUserId());
		Integer totalPoint = inGroup ? mealVO.getPoint() : null; 
		Integer benefitPoint = inGroup ? mealCreatorTypeName.getBenefitPoint() : null;
		// 점수는 받아서 그대로 입력 or 소속 그룹이 없다면 점수가 안나옴
		///////////////////////////////////////////////////
		return GetMealDetailResponse.builder()
				.mealId(mealId)
				.mealDate(mealDate)
				.mealTypeName(mealTypeVO == null ? null : 
						mealTypeVO.getMealTypeName().getLabel())
				.mealCreatorName(mealCreatorTypeName)
				.totalPoint(totalPoint)
				.benefitPoint(benefitPoint)
				.foods(foods).build();
	}
	
	////////////// 음식 업데이트 옵션별 동작 //////////////
	private void modifyFood(Long mealId, Long foodId, Double amount) {
		foodMapper.findById(foodId).orElseThrow(NoFoodException::new);
		MealItemVO mealItemVO = mealItemMapper
				.findByMeallIdAndFoodId(mealId, foodId)
				.orElseThrow(NoMealItemDataException::new);
		mealItemVO.setAmount(amount);
		if (mealItemMapper.updateById(mealItemVO) < 1)
			throw new UpdateFailedException("식단 음식 수정에 실패했습니다.");
	}

	private void removeFood(Long mealId, Long foodId, Double amount) {
		foodMapper.findById(foodId).orElseThrow(NoFoodException::new);
		MealItemVO mealItemVO = mealItemMapper
				.findByMeallIdAndFoodId(mealId, foodId)
				.orElseThrow(NoMealItemDataException::new);
		if (mealItemMapper.deleteById(mealItemVO.getMealItemId()) < 1)
			throw new DeleteFailedException("식단 음식 삭제에 실패했습니다.");
		List<MealItemVO> mealItemVOs = mealItemMapper.findAllByMealId(mealId);
		if (mealItemVOs == null || mealItemVOs.isEmpty())
			if (mealMapper.deleteById(mealId) < 1)
				throw new DeleteFailedException("식단 삭제에 실패했습니다.");
	}

	private void addFood(Long mealId, Long foodId, Double amount) {
		foodMapper.findById(foodId).orElseThrow(NoFoodException::new);
		if (mealItemMapper.findByMeallIdAndFoodId(mealId, foodId).isPresent())
			throw new ExistMealItemException();
		MealItemVO mealItemVO = MealItemVO.builder()
				.amount(amount)
				.mealId(mealId)
				.foodId(foodId)
				.build();
		if (mealItemMapper.insert(mealItemVO) < 1)
			throw new InsertFailedException("식단 음식 삽입에 실패했습니다.");
	}
	/////////////////////////////////////////////////
	
	@Override
	@Transactional
	public UpdateMealResponse updateMeal(UpdateMealRequest updateMealRequest) {
		////////////// 식단 정보 수정 //////////////
		Long mealId = updateMealRequest.getMealId();
		MealVO mealVO = mealMapper.findById(mealId)
				.orElseThrow(NoMealDataException::new);
		Double totalCalorie = updateMealRequest.getTotalCalorie();
		mealVO.setTotalKcal(totalCalorie);
		Integer totalPoint = updateMealRequest.getTotalPoint();
		Integer previousPoint = mealVO.getPoint(); // 이전 점수
		mealVO.setPoint(totalPoint);
		if (mealMapper.updateById(mealVO) < 1) 
			throw new UpdateFailedException("식단 수정에 실패했습니다.");
		/////////////////////////////////////////
		
		/////////// 식단 내 음식들 정보 수정 /////////
		List<UpdateFood> updateFoods = updateMealRequest.getUpdateFoods();
		updateFoods.forEach(updateFood -> {
			Long foodId = updateFood.getFoodId();
			Double amount = updateFood.getAmount();
			UpdateFoodType updateType = null;
			try {
				updateType = UpdateFoodType.valueOf(updateFood.getUpdateType());
			} catch (IllegalArgumentException e) {
				throw new NoUpdateFoodTypeException();
			}
			// 수정 옵션 따라 다른 방식으로 업데이트
			switch (updateType) {
			case MODIFY -> modifyFood(mealId, foodId, amount);
			case REMOVE -> removeFood(mealId, foodId, amount);
			case ADD -> addFood(mealId, foodId, amount);
			}
		});
		// 같은 달 기록이고, 지가 먹은 상황이면 점수 업데이트
		// 추천 식단인 경우, 지가 먹은 상황이면 여기로 들어올 수 있으나,
		// applyRecommend로 들어오는 상황이면 여기로 들어오지 않음
		if (LocalDate.now().getMonth() == mealVO.getMealDate().getMonth() && 
				!mealVO.getIsRecommendation()) 
			userService.updateUserPoint(mealVO.getUserId(), totalPoint-previousPoint);
		return new UpdateMealResponse();
	}

	@Override
	@Transactional
	public DeleteMealResponse deleteMeal(Long mealId) {
		/////////// 식단 정보 삭제 ///////////
		MealVO mealVO = mealMapper.findById(mealId)
				.orElseThrow(NoMealDataException::new);
		LocalDate mealDate = mealVO.getMealDate();
		MealCreatorTypeVO mealCreatorTypeVO = mealCreatorTypeMapper
				.findById(mealVO.getMealCreatorTypeId())
				.orElseThrow(NoMealCreatorTypeException::new);
		MealCreatorType mealCreatorType = mealCreatorTypeVO.getMealCreatorTypeName();
		
		// 식단 생성 주체 따라 다르게 처리
		if (mealCreatorType == MealCreatorType.USER) {
			// 유저면 식단 내 음식들 삭제
			List<MealItemVO> mealItemVOs = mealItemMapper.findAllByMealId(mealId);
			if (mealItemVOs == null || mealItemVOs.isEmpty())
				throw new NoMealItemDataException();
			/////////// 식단 내 음식들 삭제 ///////////
			mealItemVOs.forEach(mealItemVO -> {
				if (mealItemMapper.deleteById(mealItemVO.getMealItemId()) < 1)
					throw new DeleteFailedException("식단 음식 삭제에 실패했습니다.");
			});
			///////////////////////////////////
			if (mealMapper.deleteById(mealId) < 1)
				throw new DeleteFailedException("식단 삭제에 실패했습니다.");
			///////////////////////////////////
		} else {
			// 추천 식단이면 추천 여부만 변경 (그룹원 식단 테이블은 변동 사항 없음)
			mealVO.setIsRecommendation(true);
			mealVO.setMealTypeId(null);
			if (mealMapper.updateById(mealVO) < 1)
				throw new UpdateFailedException("식단 수정에 실패했습니다.");
			///////////////////////////////////
		}
		
		Long userId = mealVO.getUserId();
		// 생성 주체별 점수 업데이트 - 같은 달 기록인 경우만 점수 반영
		if (LocalDate.now().getMonth() == mealDate.getMonth()) {
			userService.updateUserPoint(userId, -mealVO.getPoint());
			switch (mealCreatorType) {
			case AI:
				userService.updateUserPoint(userId, 
						-mealCreatorType.getBenefitPoint());
				break;
			case GROUP:
				// 그룹장 추천인 경우면 그룹원 테이블에서 점수를 빼야 함
				GroupMealVO groupMealVO = groupMealMapper.findById(mealVO.getMealId())
						.orElseThrow(NoGroupMealException::new);
				groupMemberService.updateGroupMemberPoint(
						groupMealVO.getGroupMemberId(), 
						-mealCreatorType.getBenefitPoint());
				break;
			default:
				break;
			}
		}
		List<MealVO> mealVOs = mealMapper.findAllByUserIdAndMealDate(userId, mealDate);
		boolean isEmpty = mealVOs.stream()
				.filter(mealVOFiltered -> !mealVOFiltered.getIsRecommendation()).count() == 0;
		if (LocalDate.now().equals(mealDate) && isEmpty)
			badgeService.undoStreak(userId, StreakType.MEAL_RECORD);
		return new DeleteMealResponse();
	}

	@Override
	@Transactional
	public ApplyRecommendMealResponse applyRecommendMeal(ApplyRecommendMealRequest applyRecommendMealRequest) {
		MealVO mealVO = mealMapper.findById(applyRecommendMealRequest.getMealId()).orElse(null);
		
		// 날짜 및 끼니 동일 식단 있는지 확인 -> 그 사람이 이미 먹었는가가 기준
		Long userId = mealVO.getUserId();
		LocalDate mealDate = mealVO.getMealDate();
		String mealTypeName = applyRecommendMealRequest.getMealTypeName();
		MealType mealType = getMealTypeByName(mealTypeName);
		if (mealType == null) throw new NoMealTypeException();
		MealTypeVO mealTypeVO = 
				mealTypeMapper.findByMealTypeName(mealType)
				.orElseThrow(NoMealTypeException::new);
		checkExistMeal(userId, mealDate, mealType);
		
		// 실제 먹은 양 기준으로 업데이트 -> 이 시점에 점수 관련 업데이트는 일어나지 않음
		updateMeal(applyRecommendMealRequest);
		
		// 식단 업데이트 이후 추천 식단 여부만 변경 - 새로 업데이트된 레코드 기반에서 업데이트
		mealVO = mealMapper.findById(applyRecommendMealRequest.getMealId()).orElse(null);
		mealVO.setIsRecommendation(false);
		mealVO.setMealTypeId(mealTypeVO.getMealTypeId());
		if (mealMapper.updateById(mealVO) < 1)
			throw new UpdateFailedException("식단 수정에 실패했습니다.");
		
		// 식단 생성 주체 (AI 혹은 그룹장)따른 로직 처리
		// 위 update에서 점수 업데이트가 없었기에, user 테이블 점수 업데이트는 여기서 진행
		MealCreatorTypeVO mealCreatorTypeVO =
				mealCreatorTypeMapper.findById(mealVO.getMealCreatorTypeId())
				.orElseThrow(NoMealCreatorTypeException::new);
		MealCreatorType mealCreatorType = mealCreatorTypeVO.getMealCreatorTypeName();
		
		switch (mealCreatorType) {
		case AI: {
			// AI 추천 선택 경우 -> user 테이블에 식단 원점수 + AI 베네핏 점수 추가
			userService.updateUserPoint(userId, 
					applyRecommendMealRequest.getTotalPoint()+
					mealCreatorType.getBenefitPoint());
			if (LocalDate.now().equals(mealDate)) // 그 날 당일 기록만 인정
				badgeService.checkBadge(userId, CommandType.APPLY_RECOMMEND_MEAL_AI);
			break;
		}
		case GROUP: {
			// 그룹장 추천 선택 경우 -> user 테이블에 식단 원점수 + 그룹원 테이블에 GROUP 베네핏 점수 추가
			// 같은 유저여도 여러 그룹이 있기에, 어느 그룹원 기록에 줄 것인지 확인
			GroupMealVO groupMealVO = groupMealMapper.findById(mealVO.getMealId())
					.orElseThrow(NoGroupMealException::new);
			userService.updateUserPoint(userId, 
					applyRecommendMealRequest.getTotalPoint());
			groupMemberService.updateGroupMemberPoint(
					groupMealVO.getGroupMemberId(), 
					mealCreatorType.getBenefitPoint());
			if (LocalDate.now().equals(mealDate)) // 그 날 당일 기록만 인정
				badgeService.checkBadge(userId, CommandType.APPLY_RECOMMEND_MEAL_GROUP);
			break;
		}
		default:
			break;
		}
		return new ApplyRecommendMealResponse();
	}

	@Override
	public List<SearchFoodResponse> searchFood(String foodName) {
		// 영양소 계산은 재고려 필요(UnitNutrion) -> 이거 계산 방식만 정해지면 완성 ㄱㄴ
		List<FoodVO> foodVOs = foodMapper.findAllByFoodName(foodName);
		List<SearchFoodResponse> searchFoodResponses = new ArrayList<>();
		////////////// 음식 목록 검색 //////////////
		foodVOs.forEach(foodVO -> {
			Long foodId = foodVO.getFoodId();
			String name = foodVO.getFoodName();
			ParsedAmount parsedAmount = new ParsedAmount(foodVO.getServingSize());
			String unitName = parsedAmount.getUnit();
			Double amount = parsedAmount.getNumber();
			searchFoodResponses.add(SearchFoodResponse.builder()
					.foodId(foodId)
					.name(name)
					.unitName(unitName)
					.amount(amount)
					.calorie(foodVO.getEnergy())
					.carbohydrate(foodVO.getCarbohydrate())
					.protein(foodVO.getProtein())
					.fat(foodVO.getFat()).build());
		});
		/////////////////////////////////////////
		return searchFoodResponses;
	}
	// 차후 properties 분리 예정
	private Double averageActivityDefault = 5.0;
	private Double standardWeightRate = 22.0;
	private Double standardCalorie = 25.0;
	private Double activityPoint = 5.0;
	private Double calorieDelta = 5.0;

	@Override
	public TotalFoodResponse totalFood(Long userId, TotalFoodRequest totalFoodRequest) {
		UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
		List<ResearchLogVO> researchLogVOs = researchLogMapper
				.findTop7ByUserIdOrderByResearchDateDesc(userId);
		Double dailyKcalTarget = getTargetCalorie(userInfoDTO, researchLogVOs);
		DailyTargetNutrionDTO dailyTargetNutrion = 
				new DailyTargetNutrionDTO(dailyKcalTarget);
		MealTargetNutrionDTO targetNutrionResponse = 
				dailyTargetNutrion.getMealTargetNutrion(totalFoodRequest.getIsSnack());
		List<FoodAmountDTO> totalFoodRequests = totalFoodRequest.getFoods();
		TotalNutrionDTO totalNutrion = getTotalNutrion(totalFoodRequests);
		PointNutrionDTO getPointNutrionResponse = 
				getPointNutrion(totalNutrion, dailyKcalTarget, 
						targetNutrionResponse);
		return TotalFoodResponse.builder()
				.totalPoint(getPointNutrionResponse.getTotalPoint())
				.totalCalorie(totalNutrion.getTotalCalorie())
				.totalCarbohydrate(totalNutrion.getTotalCarbohydrate())
				.totalProtein(totalNutrion.getTotalProtein())
				.totalFat(totalNutrion.getTotalFat()).build();
	}

	// 칼탄단지 총 섭취량 계산
	private TotalNutrionDTO getTotalNutrion(List<FoodAmountDTO> totalFoodRequests) {
		TotalNutrionDTO totalNutrion = new TotalNutrionDTO(); // 총 섭취량은 여기 있음!
		for (FoodAmountDTO foodUnit : totalFoodRequests) {
			Long foodId = foodUnit.getFoodId();
			FoodVO foodVO = foodMapper.findById(foodId)
					.orElseThrow(NoFoodException::new);
			Double amount = foodUnit.getAmount();
			totalNutrion.addTotalNutrion(foodVO, amount);
		}
		return totalNutrion;
	}
	// 권장 섭취 칼로리 계산
	private Double getTargetCalorie(UserInfoDTO userInfoDTO, 
			List<ResearchLogVO> researchLogVOs) {
		Double averageActivity = researchLogVOs.stream()
			.mapToInt(researchLogVO -> researchLogVO.getTomorrowActivity().getScore())
			.average().orElse(averageActivityDefault); // 리서치의 활동 빈도 기반 활동량 평균 계산
		Double height = userInfoDTO.getHeight();
		if (height == null) throw new UserHeightException();
		Double heightCM = height / 100.0;
		Double standardWeight = heightCM * heightCM * standardWeightRate;
		Double kcalPerKg = standardCalorie + 
			(averageActivity - activityPoint) * calorieDelta; // 무게당 권장 칼로리
		Double dailyKcalTarget = standardWeight * kcalPerKg; // 하루 권장 섭취 칼로리
		return dailyKcalTarget;
	}
	
	private PointNutrionDTO getPointNutrion(TotalNutrionDTO totalNutrion, 
			Double dailyKcalTarget,
			MealTargetNutrionDTO getTargetNutrionResponse) {
		// 칼탄단지 권장 섭취량 근접 정도 계산 -> 점수 합산
		// 특정 영양소만 권장에 딱 맞고, 나머지가 0인 경우, 점수가 높게 나오는 경우가 있음
		Integer zeroCount = 0;
		Long caloriePoint = totalByRatio(totalNutrion.getTotalCalorie(), dailyKcalTarget/3, 60L);
		if (caloriePoint == 0) zeroCount++;
		// 탄수화물 점수
		Long carbohydratePoint = totalByRatio(totalNutrion.getTotalCarbohydrate(), 
				getTargetNutrionResponse.getMealCarbTarget(), 30L);
		if (carbohydratePoint == 0) zeroCount++;
		// 단백질 점수
		Long proteinPoint = totalByRatio(totalNutrion.getTotalProtein(), 
				getTargetNutrionResponse.getMealProteinTarget(), 30L);
		if (proteinPoint == 0) zeroCount++;
		// 지방 점수
		Long fatPoint = totalByRatio(totalNutrion.getTotalFat(), 
				getTargetNutrionResponse.getMealFatTarget(), 30L);
		if (fatPoint == 0) zeroCount++;
		// 점수가 셋 이상 0이 나오면 아얘 0점으로 환산하도록 로직 계산
        Long totalPoint = zeroCount >= 3 ? 0 : 
        			caloriePoint + carbohydratePoint + 
        			proteinPoint + fatPoint;
        return PointNutrionDTO.builder()
        		.zeroCount(zeroCount)
        		.caloriePoint(caloriePoint)
        		.carbohydratePoint(carbohydratePoint)
        		.proteinPoint(proteinPoint)
        		.fatPoint(fatPoint)
        		.totalPoint(totalPoint)
        		.build();
	}
	
	private Long totalByRatio(Double actual, Double target, Long maxScore) {
		// 점수 계산 로직을 설명한다면 여기 기능을 보고 설명하면 됨
		if (target <= 0 || actual <= 0) return 0L;
		double ratio = actual / target;
		// ±10% 이내 → 만점
        if (ratio >= 0.9 && ratio <= 1.1) return maxScore;
        
        double distance = Math.abs(ratio - 1.0);
        // ±50% 이상 벗어나면 0점
        if (distance >= 0.5) return 0L;
        
        double effectiveDistance = distance - 0.1; // 0.1 이후부터 패널티
        double maxDistanceRange = 0.4; // 0.1 ~ 0.5
        if (effectiveDistance <= 0) return maxScore;
        double penaltyRatio = effectiveDistance / maxDistanceRange;
        penaltyRatio = Math.min(1.0, Math.max(0.0, penaltyRatio));
        double totalResult = maxScore * (1.0 - penaltyRatio);
		return Math.round(totalResult);
	}

	@Override
	public List<GetRecommendMealResponse> getRecommendMeal(Long userId, LocalDate mealDate) {
		// 그 날의 추천 식단 목록 반환
		List<MealVO> mealVOs = mealMapper.findAllByUserIdAndMealDate(userId, mealDate);
		if (mealVOs == null) throw new NoMealDataException();
		List<GetRecommendMealResponse> getRecommendMealResponses = new ArrayList<>();
		mealVOs.stream()
			.filter(MealVO::getIsRecommendation)
			.forEach(mealVO -> {
				Long mealId = mealVO.getMealId();
				MealCreatorTypeVO mealCreatorTypeVO = 
						mealCreatorTypeMapper.findById(mealVO.getMealCreatorTypeId())
						.orElseThrow(NoMealCreatorTypeException::new);
				MealCreatorType mealCreatorTypeName = mealCreatorTypeVO.getMealCreatorTypeName();
				
				List<MealItemVO> mealItemVOs = mealItemMapper.findAllByMealId(mealId);
				if (mealItemVOs == null || mealItemVOs.isEmpty()) throw new NoMealItemDataException();
				TotalNutrionDTO totalNutrion = new TotalNutrionDTO();
				List<GetMealOfDateFoodDTO> foods = new ArrayList<>();
				mealItemVOs.forEach(mealItemVO -> {
					// 식단 별 음식
					FoodVO foodVO = foodMapper.findById(mealItemVO.getFoodId())
							.orElseThrow(NoFoodException::new);
					Long foodId = foodVO.getFoodId();
					String name = foodVO.getFoodName();
					FoodUnitVO foodUnitVO = foodUnitMapper.findById(foodVO.getFoodUnitId())
							.orElseThrow(NoFoodUnitException::new);
					String unitName = foodUnitVO.getUnitName();
					Double amount = mealItemVO.getAmount();
					totalNutrion.addTotalNutrion(foodVO, amount);
					foods.add(GetMealOfDateFoodDTO.builder()
							.foodId(foodId)
							.name(name)
							.unitName(unitName)
							.amount(amount)
							.build());
				});
				Integer totalPoint = mealVO.getPoint();
				Integer benefitPoint = mealCreatorTypeName.getBenefitPoint();
				String title = null;
				String description = null;
				if (mealCreatorTypeName == MealCreatorType.AI) {
					MealAiDetailVO mealAiDetailVO = mealAiDetailMapper.findByMealId(mealId)
							.orElseThrow(NoMealAIDetailException::new);
					title = mealAiDetailVO.getReason();
					description = mealAiDetailVO.getDifficulty();
				} else if (mealCreatorTypeName == MealCreatorType.GROUP) {
					// 그룹 추천일 때 지정할 title, description
					GroupMealVO groupMealVO = groupMealMapper.findById(mealId)
							.orElseThrow(NoGroupMealException::new);
					GroupInfoDTO groupInfoDTO = groupMemberService.getGroupInfoDTOByGroupMemberId(groupMealVO.getGroupMemberId());
					title = groupInfoDTO.getGroupName()+"의 추천 식단";
					UserInfoDTO userInfoDTO = userService.getUserInfo(groupInfoDTO.getUserId());
					description = "추천인: "+userInfoDTO.getName();
				}
				
				getRecommendMealResponses.add(GetRecommendMealResponse.builder()
						.mealId(mealId)
						.mealCreatorName(mealCreatorTypeName)
						.title(title) // meal_recommend 구현되면 수정 예정
						.description(description) // 여기도
						.totalCalorie(totalNutrion.getTotalCalorie())
						.totalCarbohydrate(totalNutrion.getTotalCarbohydrate())
						.totalProtein(totalNutrion.getTotalProtein())
						.totalFat(totalNutrion.getTotalFat())
						.totalPoint(totalPoint)
						.benefitPoint(benefitPoint)
						.foods(foods)
						.build());
			});
		return getRecommendMealResponses;
	}

	@Override
	public List<LocalDate> getMonthRecord(Long userId, Integer year, Integer month) {
		if (year < 1900 || 2100 < year || month < 1 || 12 < month)
			throw new MealDateInputException();
		return mealMapper.
				findMealDateByUserIdAndMealYearAndMealMonth(userId, year, month);
	}

	@Override
	public GetIntakeOfDateResponse getIntakeOfDate(Long userId, LocalDate mealDate) {
		////////////////// 사용자 하루 권장 칼탄단지 계산 ////////////////
		UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
		List<ResearchLogVO> researchLogVOs = researchLogMapper
				.findTop7ByUserIdOrderByResearchDateDesc(userId);
		Double dailyCalorieTarget = getTargetCalorie(userInfoDTO, researchLogVOs);
		DailyTargetNutrionDTO dailyTargetNutrionDTO = 
				new DailyTargetNutrionDTO(dailyCalorieTarget);
		Double dailyCarbTarget = dailyTargetNutrionDTO.getDailyCarbTarget();
		Double dailyProteinTarget = dailyTargetNutrionDTO.getDailyProteinTarget();
		Double dailyFatTarget = dailyTargetNutrionDTO.getDailyFatTarget();
		
		// 권장 섭취 식이섬유 양
		Gender gender = userInfoDTO.getGender();
		Double dailyFiberTarget = gender == Gender.MALE ? 30.0 : 20.0; // 추가
		
		// 권장 섭취 칼슘 양 (mg 단위인 점 주의!)
		LocalDate birthDate = userInfoDTO.getBirthDate();
		Long age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
		Double dailyCalciumTarget = 
				age >= 50 ? 1100.0 : 
				age >= 19 ? (gender == Gender.MALE ? 750.0 : 660.0) :
				age >= 9 ? 1300.0 : 1000.0; // 추가
		///////////////////////////////////////////////////////////
		
		////////////////////// 실제 섭취량 계산 //////////////////////
		List<MealVO> mealVOs = mealMapper.findAllByUserIdAndMealDate(userId, mealDate);
		List<FoodAmountDTO> foodAmountDTOs = new ArrayList<>();
		mealVOs.stream()
			.filter(mealVO -> !mealVO.getIsRecommendation())
			.forEach(mealVO -> {
				List<MealItemVO> mealItemVOs = mealItemMapper.findAllByMealId(mealVO.getMealId());
				mealItemVOs.forEach(mealItemVO -> 
					foodAmountDTOs.add(FoodAmountDTO.builder()
							.foodId(mealItemVO.getFoodId())
							.amount(mealItemVO.getAmount())
							.build()));
			});
		TotalNutrionDTO totalNutrion = getTotalNutrion(foodAmountDTOs);
		Double intakeCalorie = totalNutrion.getTotalCalorie();
		Double intakeCarbohydrate = totalNutrion.getTotalCarbohydrate();
		Double intakeProtein = totalNutrion.getTotalProtein();
		Double intakeFat = totalNutrion.getTotalFat();
		Double intakeFiber = totalNutrion.getTotalFiber(); // 추가
		Double intakeCalcium = totalNutrion.getTotalCalcium(); // 추가
		///////////////////////////////////////////////////////////
		
		IntakeValue calorie = IntakeValue.builder()
				.intake(intakeCalorie)
				.target(dailyCalorieTarget)
				.rate(intakeCalorie/dailyCalorieTarget)
				.unitName("kcal")
				.build();
		IntakeValue carbohydrate = IntakeValue.builder()
				.intake(intakeCarbohydrate)
				.target(dailyCarbTarget)
				.rate(intakeCarbohydrate/dailyCarbTarget)
				.unitName("g")
				.build();
		IntakeValue protein = IntakeValue.builder()
				.intake(intakeProtein)
				.target(dailyProteinTarget)
				.rate(intakeProtein/dailyProteinTarget)
				.unitName("g")
				.build();
		IntakeValue fat = IntakeValue.builder()
				.intake(intakeFat)
				.target(dailyFatTarget)
				.rate(intakeFat/dailyFatTarget)
				.unitName("g")
				.build();
		IntakeValue fiber = IntakeValue.builder()
				.intake(intakeFiber)
				.target(dailyFiberTarget)
				.rate(intakeFiber/dailyFiberTarget)
				.unitName("g")
				.build();
		IntakeValue calcium = IntakeValue.builder()
				.intake(intakeCalcium)
				.target(dailyCalciumTarget)
				.rate(intakeCalcium/dailyCalciumTarget)
				.unitName("mg")
				.build();
		
		return GetIntakeOfDateResponse.builder()
				.calorie(calorie)
				.carbohydrate(carbohydrate)
				.protein(protein)
				.fat(fat)
				.fiber(fiber)
				.calcium(calcium)
				.build();
	}

	@Override
	public MealInfoDTO getMealInfo(Long mealId) {
		MealVO mealVO = mealMapper.findById(mealId).orElseThrow(NoMealDataException::new);

		return MealInfoDTO.builder()
				.mealId(mealVO.getMealId())
				.mealDate(mealVO.getMealDate())
				.totalKcal(mealVO.getTotalKcal())
				.createdAt(mealVO.getCreatedAt())
				.updatedAt(mealVO.getUpdatedAt())
				.point(mealVO.getPoint())
				.isRecommendation(mealVO.getIsRecommendation())
				.userId(mealVO.getUserId())
				.mealTypeId(mealVO.getMealTypeId())
				.mealCreatorTypeId(mealVO.getMealCreatorTypeId())
				.build();
	}

	@Override
	public GetWeeklyDailyNutrionIntakeRateResponse getWeeklyDailyNutrionIntakeRate(Long userId) {
		// 주간 섭취량
		List<MealVO> mealVOsWeekly = getSameWeekRecords(userId);
		mealVOsWeekly = mealVOsWeekly.stream()
				.filter(meal -> !meal.getIsRecommendation())
				.toList();
		PeriodNutritionDTO weekly = getPeriodNutritionByMealVOs(mealVOsWeekly);
		
		// 하루 섭취량
		List<MealVO> mealVOsDaily = mealMapper.findAllByUserIdAndMealDate(userId, LocalDate.now());
		mealVOsDaily = mealVOsDaily.stream()
				.filter(meal -> !meal.getIsRecommendation())
				.toList();
		PeriodNutritionDTO daily = getPeriodNutritionByMealVOs(mealVOsDaily);
		return GetWeeklyDailyNutrionIntakeRateResponse.builder()
				.weekly(weekly).daily(daily).build();
	}

	private PeriodNutritionDTO getPeriodNutritionByMealVOs(List<MealVO> mealVOs) {
		// 해당 끼니 목록에서 섭취 탄단지 총량 및 전체 칼로리 대비 비율 반환
		// 총 섭취량 계산
		TotalNutrionDTO totalNutrionDTO = new TotalNutrionDTO();
		mealVOs.forEach(mealVO -> { // 각 끼니마다 섭취 음식 조회 및 총 섭취 합산 계산
			List<MealItemVO> mealItemVOs = mealItemMapper.findAllByMealId(mealVO.getMealId());
			if (mealItemVOs == null) throw new NoMealItemDataException();
			mealItemVOs.forEach(mealItemVO -> {
				FoodVO foodVO = foodMapper.findById(mealItemVO.getFoodId())
						.orElseThrow(NoFoodException::new);
				totalNutrionDTO.addTotalNutrion(foodVO, mealItemVO.getAmount());
			});
		});
		
		// 총 칼로리
		Double totalIntakeCalorie = totalNutrionDTO.getTotalCalorie();
		
		// 탄수화물 칼로리
		Double totalIntakeCarbohydrateCalorie = totalNutrionDTO.getTotalCarbohydrate() * 4; // 칼로리 단위 계산
		Double rateCarbohydrate = totalIntakeCarbohydrateCalorie / totalIntakeCalorie;
		NutrientDTO carbohydrate = NutrientDTO.builder()
				.intakeCalorie(totalIntakeCarbohydrateCalorie)
				.rate(rateCarbohydrate)
				.build();
		
		// 단백질 칼로리
		Double totalIntakeProteinCalorie = totalNutrionDTO.getTotalProtein() * 4;
		Double rateProtein = totalIntakeProteinCalorie / totalIntakeCalorie;
		NutrientDTO protein = NutrientDTO.builder()
				.intakeCalorie(totalIntakeProteinCalorie)
				.rate(rateProtein)
				.build();
		
		// 지방 칼로리
		Double totalIntakeFatCalorie = totalNutrionDTO.getTotalFat() * 9;
		Double rateFat = totalIntakeFatCalorie / totalIntakeCalorie;
		NutrientDTO fat = NutrientDTO.builder()
				.intakeCalorie(totalIntakeFatCalorie)
				.rate(rateFat)
				.build();
		
		return PeriodNutritionDTO.builder()
				.totalIntakeCalorie(totalIntakeCalorie)
				.carbohydrate(carbohydrate)
				.protein(protein)
				.fat(fat)
				.build();
	}
	
	private List<MealVO> getSameWeekRecords(Long userId) {
		LocalDate today = LocalDate.now();
		int dayOfWeek = today.get(ChronoField.DAY_OF_WEEK);
		LocalDate sunday = today.minusDays(dayOfWeek); // 그 주의 일요일
		List<MealVO> mealVOs = 
				mealMapper.findAllByUserIdAndMealDateBetweenStartDateAndEndDate(userId, sunday, today);
		if (mealVOs == null) throw new NoMealDataException();
		return mealVOs;
	}

	@Override
	public List<LocalDate> getRecordedDate(Long userId) {
		List<MealVO> mealVOs = mealMapper.findAllByUserId(userId);
		if (mealVOs == null) throw new NoMealDataException();
		List<LocalDate> recordedDates = new ArrayList<>();
		mealVOs.stream()
			.filter(mealVO -> !mealVO.getIsRecommendation())
			.map(MealVO::getMealDate)
			.filter(mealDate -> !recordedDates.contains(mealDate))
			.forEach(mealDate -> recordedDates.add(mealDate));
		return recordedDates;
	}
}
