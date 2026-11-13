package com.ssafy.yumyum.domain.meal.service;

import com.ssafy.yumyum.common.exception.commonExceptions.DeleteFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.ai.dto.response.AiMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetAiMealListResponse;
import com.ssafy.yumyum.domain.meal.exception.*;
import com.ssafy.yumyum.domain.meal.repository.food.FoodMapper;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitMapper;
import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitVO;
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
import com.ssafy.yumyum.domain.meal.service.dto.AiMealHeaderDTO;
import com.ssafy.yumyum.domain.meal.service.dto.GetMealOfDateFoodDTO;
import com.ssafy.yumyum.domain.meal.service.dto.TotalNutrionDTO;
import com.ssafy.yumyum.domain.meal.types.MealCreatorType;
import com.ssafy.yumyum.domain.meal.types.MealType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealAiServiceImpl implements MealAiService {

    private final FoodMatchingService foodMatchingService;

    private final MealMapper mealMapper;
    private final MealAiDetailMapper mealAiDetailMapper;
    private final MealCreatorTypeMapper mealCreatorTypeMapper;
    private final MealTypeMapper mealTypeMapper;
    private final MealItemMapper mealItemMapper;
    private final FoodMapper foodMapper;
    private final FoodUnitMapper foodUnitMapper;

    @Override
    @Transactional
    public void saveAiMealList(Long userId, List<AiMealResponse.Recommendation> recommendations) {
        // 날짜를 내일로 지정
        LocalDate tomorrow = LocalDate.now();//.plusDays(1);
        // 테스트용으로 저렇게 반영해둠 (실제 사용 시엔 plusDays 적용할 것!)

        // 해당 날짜로 이미 AI식단이 있으면(스케쥴러로 인해)
        // 해당 날짜의 유저 식단 중, 생성자가 'AI'인 것만 골라서 삭제 -> 지금 생성된 식단으로 갱신
        List<MealVO> existingMeals = mealMapper.findAllByUserIdAndMealDate(userId, tomorrow);

        if (existingMeals != null) {
            for (MealVO meal : existingMeals) {
                if (Boolean.TRUE.equals(meal.getIsRecommendation())) {
                    if(meal.getMealCreatorTypeId() == 2){	//AI -> id: 2
                        deleteAiMeal(meal.getMealId());
                    }
                }
            }
        }

        MealCreatorTypeVO aiCreatorType = mealCreatorTypeMapper.findByMealCreatorTypeName("AI")
                .orElseThrow(NoMealCreatorTypeException::new);


        // TODO: 식사 타입(아침/점심/저녁)을 AI 응답에서 받아오거나 로직으로 처리 필요. 현재는 '점심'으로 고정.
        MealTypeVO defaultType = mealTypeMapper.findByMealTypeName(MealType.LUNCH)
                .orElseThrow(NoMealTypeException::new);

        // 추천 식단 저장 루프(3가지)
        for (AiMealResponse.Recommendation rec : recommendations) {
            // meal 데이터 생성
            MealVO mealVO = MealVO.builder()
                    .userId(userId)
                    .mealDate(tomorrow)
                    .isRecommendation(true)
                    .mealCreatorTypeId(aiCreatorType.getMealCreatorTypeId())
                    .mealTypeId(defaultType.getMealTypeId()) // 임시 타입
                    .totalKcal(0.0)
                    .point(0)
                    .build();

            if(mealMapper.insert(mealVO) < 1){
                throw new InsertFailedException("AI 추천 식단 생성에 실패하였습니다.");
            }
            Long mealId = mealVO.getMealId();

            // MealAiDetail(상세 이유/난이도) 생성
            MealAiDetailVO detailVO = MealAiDetailVO.builder()
                    .mealId(mealId)
                    .difficulty(rec.getDifficulty())
                    .reason(rec.getReason())
                    .build();
            if(mealAiDetailMapper.insert(detailVO) < 1){
                throw new InsertFailedException("AI 추천식단 정보를 생성하는데 실패하였습니다.");
            }

            // C. 식재료 매칭 및 저장
            Double totalKcal = 0.0;

            for (AiMealResponse.AiFoodItem item : rec.getItems()) {
                // FoodMatchingService를 통해 음식 찾기 (없으면 임시 음식 생성)
                FoodVO food = foodMatchingService.findOrRegisterTempFood(item.getFoodName());

                // 칼로리 계산: (FoodEnergy / FoodAmount) * AIAmount
                Double foodKcal = 0.0;
                if (food.getAmount() > 0) {
                    foodKcal = (food.getEnergy() / food.getAmount()) * item.getAmount();
                }
                totalKcal += foodKcal;

                // 음식 목록 저장
                MealItemVO mealItemVO = MealItemVO.builder()
                        .mealId(mealId)
                        .foodId(food.getFoodId())
                        .amount((double) item.getAmount())
                        .build();
                if(mealItemMapper.insert(mealItemVO) < 1){
                    throw new InsertFailedException("AI 추천식단 음식 목록 저장에 실패하였습니다.");
                }
            }

            // 계산된 칼로리 정보 저장
            mealVO.setTotalKcal(totalKcal);

            if(mealMapper.updateById(mealVO) < 1){
                throw new UpdateFailedException("AI 식단 정보를 수정하는데 실패하였습니다.");
            }
        }
        log.info("Saved AI meals for user: {} on date: {}", userId, tomorrow);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAiMealListResponse getAiMealList(Long userId, LocalDate date) {
        List<AiMealHeaderDTO> aiMealHeaders = mealAiDetailMapper.findAiMealListByUserIdAndDate(userId, date);

        List<GetAiMealListResponse.AiMealDetail> aiMeals = new ArrayList<>();

        for (AiMealHeaderDTO header : aiMealHeaders) {
            Long mealId = header.getMealId();

            // 음식 리스트 조회 및 영양소 계산
            List<MealItemVO> mealItems = mealItemMapper.findAllByMealId(mealId);
            List<GetMealOfDateFoodDTO> foods = new ArrayList<>();
            TotalNutrionDTO totalNutrion = new TotalNutrionDTO(); // 영양소 계산기

            for (MealItemVO item : mealItems) {
                FoodVO food = foodMapper.findById(item.getFoodId())
                        .orElseThrow(NoFoodException::new);

                // 영양소 누적
                totalNutrion.addTotalNutrion(food, item.getAmount());

                // 단위 조회
                FoodUnitVO foodUnit = foodUnitMapper.findById(food.getFoodUnitId()).orElse(null);
                String unitName = (foodUnit != null) ? foodUnit.getUnitName() : "";

                // 음식 DTO 추가
                foods.add(GetMealOfDateFoodDTO.builder()
                        .foodId(food.getFoodId())
                        .name(food.getFoodName())
                        .unitName(unitName)
                        .amount(item.getAmount())
                        .build());
            }

            // 3. 응답 DTO 조립 (★ 누락된 부분 추가됨)
            aiMeals.add(GetAiMealListResponse.AiMealDetail.builder()
                    .mealId(mealId)
                    .difficulty(header.getDifficulty())
                    .reason(header.getReason())
                    .totalCalorie(totalNutrion.getTotalCalorie())
                    .totalCarbohydrate(totalNutrion.getTotalCarbohydrate())
                    .totalProtein(totalNutrion.getTotalProtein())
                    .totalFat(totalNutrion.getTotalFat())
                    .foods(foods)
                    .build());
        }

        return GetAiMealListResponse.builder()
                .aiMeals(aiMeals)
                .build();
    }

    /**
     * 식단 삭제 내부 메서드 (Cascade 로직 처리)
     */
    private void deleteAiMeal(Long mealId) {
        /////////// 식단 정보 삭제 ///////////
        MealVO mealVO = mealMapper.findById(mealId)
                .orElseThrow(NoMealDataException::new);

        MealCreatorTypeVO mealCreatorTypeVO = mealCreatorTypeMapper
                .findById(mealVO.getMealCreatorTypeId())
                .orElseThrow(NoMealCreatorTypeException::new);

        // 식단 생성 주체 따라 다르게 처리
        if (MealCreatorType.AI.equals(mealCreatorTypeVO.getMealCreatorTypeName())) {
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

            mealAiDetailMapper.deleteById(mealId);

            if (mealMapper.deleteById(mealId) < 1)
            	throw new DeleteFailedException("식단 삭제에 실패했습니다.");
            ///////////////////////////////////
        } else {
            // 추천 식단이면 추천 여부만 변경
            mealVO.setIsRecommendation(true);
            if (mealMapper.updateById(mealVO) < 1)
            	throw new UpdateFailedException("식단 수정에 실패했습니다.");
            ///////////////////////////////////
        }
    }
}
