package com.ssafy.yumyum.domain.meal.repository;

import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitMapper;
import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class FoodUnitMapperTest {

    @Autowired
    private FoodUnitMapper foodUnitMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = foodUnitMapper.count();
        System.out.println("총 개수: " + count);

        List<FoodUnitVO> list = foodUnitMapper.findAll();
        for (FoodUnitVO unit : list) {
            System.out.println(unit);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<FoodUnitVO> unit = foodUnitMapper.findById(1L);
        System.out.println(unit.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        FoodUnitVO unit = FoodUnitVO.builder()
                .unitName("test unit")
                .build();

        foodUnitMapper.insert(unit);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = foodUnitMapper.maxId();

        FoodUnitVO unit = FoodUnitVO.builder()
                .foodUnitId(maxId)
                .unitName("updated unit")
                .build();

        foodUnitMapper.updateById(unit);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = foodUnitMapper.maxId();
        foodUnitMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
