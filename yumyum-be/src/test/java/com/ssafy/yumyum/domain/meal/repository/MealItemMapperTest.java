package com.ssafy.yumyum.domain.meal.repository;

import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemMapper;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MealItemMapperTest {

    @Autowired
    private MealItemMapper mealItemMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = mealItemMapper.count();
        System.out.println("총 개수: " + count);

        List<MealItemVO> list = mealItemMapper.findAll();
        for (MealItemVO item : list) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<MealItemVO> item = mealItemMapper.findById(11L);
        System.out.println(item.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        MealItemVO item = MealItemVO.builder()
                .amount(75.0)
                .mealId(1L)  // FK: meal 테이블
                .foodId(1L)  // FK: food 테이블
                .build();

        mealItemMapper.insert(item);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = mealItemMapper.maxId();

        MealItemVO item = MealItemVO.builder()
                .mealItemId(maxId)
                .amount(80.0)
                .mealId(2L)  // FK: meal 테이블
                .foodId(2L)  // FK: food 테이블
                .build();

        mealItemMapper.updateById(item);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = mealItemMapper.maxId();
        mealItemMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
