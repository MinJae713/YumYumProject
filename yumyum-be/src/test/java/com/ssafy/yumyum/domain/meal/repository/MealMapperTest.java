package com.ssafy.yumyum.domain.meal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.meal.repository.meal.MealMapper;
import com.ssafy.yumyum.domain.meal.repository.meal.MealVO;

@SpringBootTest
public class MealMapperTest {

    @Autowired
    private MealMapper mealMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = mealMapper.count();
        System.out.println("총 개수: " + count);

        List<MealVO> list = mealMapper.findAll();
        for (MealVO meal : list) {
            System.out.println(meal);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<MealVO> meal = mealMapper.findById(1L);
        System.out.println(meal.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        MealVO meal = MealVO.builder()
                .mealDate(LocalDate.now())
                .totalKcal(550.0)
                .point(10)
                .isRecommendation(false)
                .userId(1L)               // FK: users 테이블
                .mealTypeId(1L)           // FK: meal_type 테이블
                .mealCreatorTypeId(1L)    // FK: meal_creator_type 테이블
                .build();

        mealMapper.insert(meal);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = mealMapper.maxId();

        MealVO meal = MealVO.builder()
                .mealId(maxId)
                .mealDate(LocalDate.now())
                .totalKcal(600.0)
                .point(15)
                .isRecommendation(true)
                .userId(2L)               
                .mealTypeId(2L)           
                .mealCreatorTypeId(2L)    
                .build();

        mealMapper.updateById(meal);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = mealMapper.maxId();
        mealMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
