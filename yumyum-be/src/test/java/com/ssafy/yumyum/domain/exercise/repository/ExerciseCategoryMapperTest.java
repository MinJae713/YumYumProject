package com.ssafy.yumyum.domain.exercise.repository;

import com.ssafy.yumyum.domain.exercise.repository.exerciseCategory.ExerciseCategoryMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCategory.ExerciseCategoryVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ExerciseCategoryMapperTest {

    @Autowired
    private ExerciseCategoryMapper exerciseCategoryMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = exerciseCategoryMapper.count();
        System.out.println("총 개수: " + count);

        List<ExerciseCategoryVO> list = exerciseCategoryMapper.findAll();
        for (ExerciseCategoryVO category : list) {
            System.out.println(category);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<ExerciseCategoryVO> category = exerciseCategoryMapper.findById(1L);
        System.out.println(category.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        ExerciseCategoryVO category = ExerciseCategoryVO.builder()
                .exerciseCategoryName("새로운 운동 카테고리")
                .build();

        exerciseCategoryMapper.insert(category);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = exerciseCategoryMapper.maxId();

        ExerciseCategoryVO category = ExerciseCategoryVO.builder()
                .exerciseCategoryId(maxId)
                .exerciseCategoryName("수정된 운동 카테고리")
                .build();

        exerciseCategoryMapper.updateById(category);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = exerciseCategoryMapper.maxId();
        exerciseCategoryMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
