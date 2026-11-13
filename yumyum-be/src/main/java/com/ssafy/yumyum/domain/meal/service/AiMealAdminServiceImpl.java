package com.ssafy.yumyum.domain.meal.service;

import com.ssafy.yumyum.domain.meal.dto.response.AdminAiMealResponse;
import com.ssafy.yumyum.domain.meal.repository.food.FoodMapper;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.meal.MealMapper;
import com.ssafy.yumyum.domain.meal.repository.meal.MealVO;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemMapper;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemVO;
import com.ssafy.yumyum.domain.user.repository.user.UserMapper;
import com.ssafy.yumyum.domain.user.repository.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiMealAdminServiceImpl implements AiMealAdminService {

    private final MealMapper mealMapper;
    private final MealItemMapper mealItemMapper;
    private final FoodMapper foodMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Map<String, Object> getAllAiMealList(int page, int size) {
        int offset = (page - 1) * size;

        // 1. AI 식단(type=2) 목록 조회
        List<MealVO> meals = mealMapper.findAllAiMealsForAdmin(size, offset);
        int totalCount = mealMapper.countAllAiMealsForAdmin();

        List<AdminAiMealResponse> dtos = new ArrayList<>();

        // 2. 각 식단별 상세 정보 조립
        for (MealVO meal : meals) {
            // 2-1. 유저 정보 조회 (이름)
            // UserMapper가 없다면 추가 필요, UserVO에 userName이 있다고 가정
            String userName = "알 수 없음";
            UserVO user = userMapper.findById(meal.getUserId()).orElse(null);
            if (user != null) {
                userName = user.getName();
            }

            // 2-2. 식단에 포함된 음식들 조회
            List<MealItemVO> items = mealItemMapper.findAllByMealId(meal.getMealId());

            List<String> foodNames = new ArrayList<>();
            double totalCarbo = 0.0;
            double totalProtein = 0.0;
            double totalFat = 0.0;

            // 2-3. 영양소 합산 및 음식 이름 수집
            for (MealItemVO item : items) {
                FoodVO food = foodMapper.findById(item.getFoodId()).orElse(null);
                if (food != null) {
                    foodNames.add(food.getFoodName());

                    // 영양소 계산 (단순 합산 예시)
                    // 실제로는 (food.getCarbohydrate() / food.getAmount()) * item.getAmount() 처럼 비율 계산이 필요할 수 있음
                    // 여기서는 DB에 저장된 값을 그대로 더하는 것으로 구현 (필요시 수정)
                    if (food.getCarbohydrate() != null) totalCarbo += food.getCarbohydrate();
                    if (food.getProtein() != null) totalProtein += food.getProtein();
                    if (food.getFat() != null) totalFat += food.getFat();
                }
            }

            // 2-4. DTO 생성
            dtos.add(AdminAiMealResponse.builder()
                    .mealId(meal.getMealId())
                    .userId(meal.getUserId())
                    .userName(userName)
                    .mealDate(meal.getMealDate())
                    .totalKcal(meal.getTotalKcal())
                    // 소수점 1자리 반올림
                    .totalCarbo(Math.round(totalCarbo * 10) / 10.0)
                    .totalProtein(Math.round(totalProtein * 10) / 10.0)
                    .totalFat(Math.round(totalFat * 10) / 10.0)
                    .foodNames(foodNames)
                    .build());
        }

        // 3. 결과 맵핑 (프론트엔드 Pagination 처리를 위함)
        Map<String, Object> result = new HashMap<>();
        result.put("list", dtos);
        result.put("totalCount", totalCount);
        result.put("totalPage", (int) Math.ceil((double) totalCount / size));
        result.put("currentPage", page);

        return result;
    }
}
