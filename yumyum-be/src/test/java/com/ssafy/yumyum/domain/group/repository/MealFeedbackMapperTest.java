package com.ssafy.yumyum.domain.group.repository;

import com.ssafy.yumyum.domain.group.repository.mealFeedback.MealFeedbackMapper;
import com.ssafy.yumyum.domain.group.repository.mealFeedback.MealFeedbackVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MealFeedbackMapperTest {

    @Autowired
    private MealFeedbackMapper mealFeedbackMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = mealFeedbackMapper.count();
        System.out.println("총 개수: " + count);

        List<MealFeedbackVO> list = mealFeedbackMapper.findAll();
        for (MealFeedbackVO feedback : list) {
            System.out.println(feedback);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<MealFeedbackVO> feedback = mealFeedbackMapper.findById(1L);
        System.out.println(feedback.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        MealFeedbackVO feedback = MealFeedbackVO.builder()
                .content("맛있어요")
                .mealId(1L)
                .groupMemberId(1L)
                .build();

        mealFeedbackMapper.insert(feedback);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = mealFeedbackMapper.maxId();

        MealFeedbackVO feedback = MealFeedbackVO.builder()
                .mealFeedbackId(maxId)
                .content("조금 짰어요")
                .mealId(2L)
                .groupMemberId(2L)
                .build();

        mealFeedbackMapper.updateById(feedback);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = mealFeedbackMapper.maxId();
        mealFeedbackMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
