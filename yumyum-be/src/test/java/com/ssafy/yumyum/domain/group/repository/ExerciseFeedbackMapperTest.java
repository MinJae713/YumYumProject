package com.ssafy.yumyum.domain.group.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.group.repository.exerciseFeedback.ExerciseFeedbackMapper;
import com.ssafy.yumyum.domain.group.repository.exerciseFeedback.ExerciseFeedbackVO;

@SpringBootTest
public class ExerciseFeedbackMapperTest {

    @Autowired
    private ExerciseFeedbackMapper exerciseFeedbackMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = exerciseFeedbackMapper.count();
        System.out.println("총 개수: " + count);

        List<ExerciseFeedbackVO> list = exerciseFeedbackMapper.findAll();
        for (ExerciseFeedbackVO feedback : list) {
            System.out.println(feedback);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<ExerciseFeedbackVO> feedback = exerciseFeedbackMapper.findById(1L);
        System.out.println(feedback.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        ExerciseFeedbackVO feedback = ExerciseFeedbackVO.builder()
                .content("운동 자세가 좋아졌습니다!")
                .exerciseRecordId(1L)
                .groupMemberId(1L)
                .build();

        exerciseFeedbackMapper.insert(feedback);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = exerciseFeedbackMapper.maxId();

        ExerciseFeedbackVO feedback = ExerciseFeedbackVO.builder()
                .exerciseFeedbackId(maxId)
                .content("오늘 운동 점검 완료. 코어 더 신경 쓰세요.")
                .exerciseRecordId(2L)
                .groupMemberId(2L)
                .build();

        exerciseFeedbackMapper.updateById(feedback);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = exerciseFeedbackMapper.maxId();
        exerciseFeedbackMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
