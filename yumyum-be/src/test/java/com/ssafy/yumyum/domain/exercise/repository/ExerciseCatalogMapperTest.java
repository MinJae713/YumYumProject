package com.ssafy.yumyum.domain.exercise.repository;

import com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog.ExerciseCatalogMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog.ExerciseCatalogVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ExerciseCatalogMapperTest {

    @Autowired
    private ExerciseCatalogMapper exerciseCatalogMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = exerciseCatalogMapper.count();
        System.out.println("총 개수: " + count);

        List<ExerciseCatalogVO> list = exerciseCatalogMapper.findAll();
        for (ExerciseCatalogVO exercise : list) {
            System.out.println(exercise);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<ExerciseCatalogVO> exercise = exerciseCatalogMapper.findById(1L);
        System.out.println(exercise.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        ExerciseCatalogVO exercise = ExerciseCatalogVO.builder()
                .name("테스트 운동")
                .metValue(5.5)
                .exerciseCategoryId(1L)
                .build();

        exerciseCatalogMapper.insert(exercise);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = exerciseCatalogMapper.maxId();

        ExerciseCatalogVO exercise = ExerciseCatalogVO.builder()
                .exerciseId(maxId)
                .name("수정된 운동")
                .metValue(7.2)
                .exerciseCategoryId(2L)
                .build();

        exerciseCatalogMapper.updateById(exercise);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = exerciseCatalogMapper.maxId();
        exerciseCatalogMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
