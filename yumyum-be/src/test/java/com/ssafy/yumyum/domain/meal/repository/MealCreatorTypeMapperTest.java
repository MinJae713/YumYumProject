package com.ssafy.yumyum.domain.meal.repository;

import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeMapper;
import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeVO;
import com.ssafy.yumyum.domain.meal.types.MealCreatorType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MealCreatorTypeMapperTest {

    @Autowired
    private MealCreatorTypeMapper mealCreatorTypeMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = mealCreatorTypeMapper.count();
        System.out.println("총 개수: " + count);

        List<MealCreatorTypeVO> list = mealCreatorTypeMapper.findAll();
        for (MealCreatorTypeVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<MealCreatorTypeVO> vo = 
        		mealCreatorTypeMapper.findById(mealCreatorTypeMapper.maxId());
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        MealCreatorTypeVO vo = MealCreatorTypeVO.builder()
                .mealCreatorTypeName(MealCreatorType.USER) // 예시: 새 타입
                .build();

        mealCreatorTypeMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = mealCreatorTypeMapper.maxId();

        MealCreatorTypeVO vo = MealCreatorTypeVO.builder()
                .mealCreatorTypeId(maxId)
                .mealCreatorTypeName(MealCreatorType.AI) // 예시: 업데이트
                .build();

        mealCreatorTypeMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = mealCreatorTypeMapper.maxId();
        mealCreatorTypeMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
