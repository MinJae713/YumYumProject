package com.ssafy.yumyum.domain.meal.repository;

import com.ssafy.yumyum.domain.meal.repository.mealType.MealTypeMapper;
import com.ssafy.yumyum.domain.meal.repository.mealType.MealTypeVO;
import com.ssafy.yumyum.domain.meal.types.MealType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MealTypeMapperTest {

    @Autowired
    private MealTypeMapper mealTypeMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = mealTypeMapper.count();
        System.out.println("총 개수: " + count);

        List<MealTypeVO> list = mealTypeMapper.findAll();
        for (MealTypeVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<MealTypeVO> vo = mealTypeMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
    	// enum이라 실행 불가
        MealTypeVO vo = MealTypeVO.builder()
                .mealTypeName(MealType.BREAKFAST) // 예시: 새 타입
                .build();

        mealTypeMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
    	// enum이라 실행 불가
        Long maxId = mealTypeMapper.maxId();

        MealTypeVO vo = MealTypeVO.builder()
                .mealTypeId(maxId)
                .mealTypeName(MealType.SNACK) // 예시: 업데이트
                .build();

        mealTypeMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = mealTypeMapper.maxId();
        mealTypeMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
