package com.ssafy.yumyum.domain.meal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.meal.dto.request.FoodMatchRequest;
import com.ssafy.yumyum.domain.meal.dto.response.FoodMatchResponse;
import com.ssafy.yumyum.domain.meal.dto.response.FoodSearchPageResponse;
import com.ssafy.yumyum.domain.meal.exception.FoodCacheNotFoundException;
import com.ssafy.yumyum.domain.meal.exception.FoodMatchFailedException;
import com.ssafy.yumyum.domain.meal.exception.FoodNotFoundException;
import com.ssafy.yumyum.domain.meal.repository.food.FoodMapper;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.foodCache.FoodCacheMapper;
import com.ssafy.yumyum.domain.meal.repository.foodCache.FoodCacheVO;
import com.ssafy.yumyum.domain.meal.repository.meal.MealMapper;
import com.ssafy.yumyum.domain.meal.repository.meal.MealVO;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemMapper;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodMatchingServiceImpl implements FoodMatchingService {

    private final FoodMapper foodMapper;
    private final FoodCacheMapper foodCacheMapper;
    private final MealMapper mealMapper;
    private final MealItemMapper mealItemMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    @Transactional
    public FoodVO findOrRegisterTempFood(String aiFoodName) {

        // 받아온 이름 공백 제거(정규화)
        String cleanName = aiFoodName.replaceAll("\\s+", "");

        // 캐시 테이블 조회
        FoodCacheVO cache = foodCacheMapper.findByFoodNameIgnoreSpace(cleanName).orElse(null);

        if (cache != null) {
            return foodMapper.findById(cache.getFoodId()).orElse(null);
        }

        // DB에서 일치하는 이름 찾기
        FoodVO exactMatch = foodMapper.findByFoodNameIgnoreSpace(cleanName).orElse(null);

        if (exactMatch != null) {
            // 이름이 완벽히 똑같은 게 있으면 리턴
            // 캐시에 다 저장
            saveCache(cleanName, exactMatch.getFoodId(), "MATCHED");
            log.info("[FoodMatch] Exact Match (Cleaned): {} -> {}", aiFoodName, cleanName);
            return exactMatch;

        } else {
            // 매칭되는 이름이 없다면 더미 food 데이터 추가
            FoodVO tempFood = FoodVO.builder()
                    .foodName("[미검증] " + aiFoodName)
                    .amount(1.0)
                    .foodUnitId(1L)
                    .servingSize("1")
                    // --- 주요 영양소 ---
                    .energy(0.0)
                    .carbohydrate(0.0)
                    .protein(0.0)
                    .fat(0.0)
                    // --- 기타 영양소 (NULL 방지) ---
                    .water(0.0)
                    .sugar(0.0)
                    .fiber(0.0)
                    .calcium(0.0)
                    .iron(0.0)
                    .potassium(0.0)
                    .sodium(0.0)
                    .vitaminA(0.0)
                    .vitaminB1(0.0)
                    .vitaminC(0.0)
                    .vitaminD(0.0)
                    .cholesterol(0.0)
                    .saturatedFattyAcid(0.0)
                    .unsaturatedFattyAcid(0.0)
                    .caffeine(0.0)
                    .magnesium(0.0)
                    .aminoAcid(0.0)
                    .build();

            if(foodMapper.insert(tempFood) < 1) // 임시 ID 생성
                throw new InsertFailedException("임시 음식 등록 중 오류가 발생했습니다.");

            // 캐시에 pending상태로 저장 -> 추후 직접 매칭
            saveCache(aiFoodName, tempFood.getFoodId(), "PENDING");

            log.info("[FoodMatch] Pending Created: {}", tempFood.getFoodName());
            return tempFood;
        }
    }

    @Override
    public void saveCache(String name, Long id, String status) {
        try{
            FoodCacheVO vo = FoodCacheVO.builder()
                    .foodName(name)
                    .foodId(id)
                    .status(status)
                    .build();
            foodCacheMapper.insert(vo);
        }catch(DataIntegrityViolationException e){
            log.warn("이미 캐시에 존재하는 음식입니다: {}", name);
        }
    }



    @Override
    @Transactional
    public List<FoodMatchResponse> getPendingList() {
        List<FoodCacheVO> pendingList = foodCacheMapper.findByStatus("PENDING");

        return pendingList.stream()
                .map(cache -> FoodMatchResponse.builder()
                        .foodCacheId(cache.getFoodCacheId())
                        .foodName(cache.getFoodName()) // AI가 인식했던 이름
                        .foodId(cache.getFoodId())       // 현재 연결된 임시 ID
                        .status(cache.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public FoodSearchPageResponse searchFoodList(String keyword, int page, int size) {
// OFFSET 계산 (1페이지 -> 0, 2페이지 -> 10 ...)
        int offset = (page - 1) * size;

        // 전체 개수 조회 (for Pagination)
        int totalCount = foodMapper.countByKeyword(keyword);

        // 데이터 조회
        List<FoodVO> foods = foodMapper.selectByKeywordWithPaging(keyword, size, offset);

        // DTO 변환
        List<FoodSearchPageResponse.FoodSimpleInfo> foodInfos = foods.stream()
                .map(food -> FoodSearchPageResponse.FoodSimpleInfo.builder()
                        .foodId(food.getFoodId())
                        .foodName(food.getFoodName())
                        .energy(food.getEnergy())
                        // 단위 정보가 있다면 여기서 조합 (예: food.getAmount() + "g")
                        .servingSize(food.getAmount() != null ? String.format("%.0f", food.getAmount()) : "0")
                        .build())
                .collect(Collectors.toList());

        // 전체 페이지 수 계산
        int totalPage = (int) Math.ceil((double) totalCount / size);

        return FoodSearchPageResponse.builder()
                .foods(foodInfos)
                .totalCount(totalCount)
                .totalPage(totalPage)
                .currentPage(page)
                .build();
    }

    @Override
    public int matchFoods(List<FoodMatchRequest> foodMatchRequestList) {
        int successCount = 0;

        for (FoodMatchRequest req : foodMatchRequestList) {
            try {
                // 이 블록 안에 로직이 모두 성공이아니면 롤백 처리되도록 트랜잭션 템플릿으로 묶음
                transactionTemplate.executeWithoutResult(status -> {
                    processSingleMatch(req);
                });

                successCount++;
            }catch (FoodCacheNotFoundException | FoodNotFoundException e) {
                // 404 계열: 데이터 문제이므로 로그 남기고 다음 건 진행
                log.error("매칭 스킵 (데이터 없음): {} - {}", req.getAiFoodName(), e.getMessage());
            } catch (Exception e) {
                // 500 계열: 심각한 오류지만 일단 로그 남기고 진행 (정책에 따라 throw 가능)
                log.error("매칭 실패 (시스템 오류): {} - {}", req.getAiFoodName(), e.getMessage());
            }
        }
        return successCount;
    }

    private void processSingleMatch(FoodMatchRequest req) {
        // 1. 캐시 정보 조회
        FoodCacheVO cache = foodCacheMapper.findByFoodName(req.getAiFoodName())
                .orElseThrow(FoodCacheNotFoundException::new);

        if ("MATCHED".equals(cache.getStatus())) {
            return; // 이미 처리된 건은 스킵
        }

        boolean exists = foodMapper.findById(req.getFoodId()).isPresent();
        if(!exists) {
            throw new FoodNotFoundException();
        }


        try{
            Long tempFoodId = cache.getFoodId();    // 기존 임시 ID (ex: 5000)
            Long realFoodId = req.getFoodId();      // 매칭할 진짜 ID (ex: 100)

            // 2. 재계산 대상 식단 ID 미리 확보 (ID 바뀌기 전)
            // tempFoodId를 가지고 있던 식단들은 영양소 정보가 바뀌어야 함
            List<Long> affectedMealIds = mealItemMapper.findMealIdsByFoodId(tempFoodId);

            // 3. MealItem 테이블의 food_id 일괄 교체 (Swap)
            mealItemMapper.updateFoodId(realFoodId, tempFoodId);

            // 4. FoodCache 상태 업데이트
            cache.setFoodId(realFoodId);
            cache.setStatus("MATCHED");
            foodCacheMapper.updateById(cache);

            // 5. 임시 Food 데이터 삭제
            foodMapper.deleteById(tempFoodId);

            // 6. 영향 받은 식단들의 영양소 재계산
            for (Long mealId : affectedMealIds) {
                recalculateMealNutrition(mealId);
            }
        }catch (Exception e) {
            // 여기서 예외 발생 시 transactionTemplate이 받아서 롤백 시킴
            throw new FoodMatchFailedException();
        }

    }

    private void recalculateMealNutrition(Long mealId) {
        List<MealItemVO> items = mealItemMapper.findAllByMealId(mealId);

        double totalKcal = 0.0;
        // 필요 시 탄단지 변수 추가 (totalCarbo, totalProtein...)

        for (MealItemVO item : items) {
            FoodVO food = foodMapper.findById(item.getFoodId()).orElse(null);

            if (food != null && food.getAmount() > 0) {
                // (음식칼로리 / 기준량) * 먹은량
                double itemKcal = (food.getEnergy() / food.getAmount()) * item.getAmount();
                totalKcal += itemKcal;
            }
        }

        // MealVO 업데이트
        MealVO meal = mealMapper.findById(mealId).orElse(null);
        if (meal != null) {
            meal.setTotalKcal(totalKcal);
            // meal.setTotalCarbo(...)
            if(mealMapper.updateById(meal) < 1){
                throw new UpdateFailedException("AI 추천 식단 업데이트에 실패했습니다.");
            }
        }
    }
}
