package com.ssafy.yumyum.domain.meal.repository;

import com.ssafy.yumyum.domain.meal.repository.food.FoodMapper;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class FoodMapperTest {

    @Autowired
    private FoodMapper foodMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = foodMapper.count();
        System.out.println("총 개수: " + count);

        List<FoodVO> list = foodMapper.findAll();
        for (FoodVO food : list) {
            System.out.println(food);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<FoodVO> food = foodMapper.findById(10000L);
        System.out.println(food.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        FoodVO food = FoodVO.builder()
                .foodName("Test Food")
                .energy(100.0)
                .servingSize("100g")
                .carbohydrate(20.0)
                .protein(10.0)
                .fat(5.0)
                .sugar(3.0)
                .fiber(2.0)
                .calcium(30.0)
                .iron(1.2)
                .potassium(200.0)
                .sodium(150.0)
                .vitaminA(50.0)
                .vitaminB1(0.1)
                .vitaminC(12.0)
                .vitaminD(1.5)
                .cholesterol(3.0)
                .saturatedFattyAcid(2.5)
                .unsaturatedFattyAcid(1.5)
                .caffeine(0.7)
                .magnesium(15.0)
                .aminoAcid(0.0)
                .amount(650.0)
                .foodUnitId(1L)
                .build();

        foodMapper.insert(food);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = foodMapper.maxId();

        FoodVO food = FoodVO.builder()
                .foodId(maxId)
                .foodName("Test")
                .energy(50.0)
                .servingSize("1g")
                .carbohydrate(20.0)
                .protein(10.0)
                .fat(5.0)
                .sugar(null)
                .fiber(2.0)
                .calcium(null)
                .iron(1.2)
                .potassium(null)
                .sodium(150.0)
                .vitaminA(50.0)
                .vitaminB1(0.1)
                .vitaminC(null)
                .vitaminD(1.5)
                .cholesterol(null)
                .saturatedFattyAcid(2.5)
                .unsaturatedFattyAcid(1.5)
                .caffeine(null)
                .magnesium(null)
                .aminoAcid(0.0)
                .amount(250.0)
                .foodUnitId(1L)
                .build();

        foodMapper.updateById(food);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = foodMapper.maxId();
        foodMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
