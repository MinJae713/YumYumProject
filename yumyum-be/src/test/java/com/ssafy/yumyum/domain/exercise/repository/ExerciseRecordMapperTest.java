package com.ssafy.yumyum.domain.exercise.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.exercise.repository.exerciseRecord.ExerciseRecordMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseRecord.ExerciseRecordVO;

@SpringBootTest
public class ExerciseRecordMapperTest {

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = exerciseRecordMapper.count();
        System.out.println("총 개수: " + count);

        List<ExerciseRecordVO> list = exerciseRecordMapper.findAll();
        for (ExerciseRecordVO record : list) {
            System.out.println(record);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<ExerciseRecordVO> record = exerciseRecordMapper.findById(1L);
        System.out.println(record.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        ExerciseRecordVO record = ExerciseRecordVO.builder()
                .exerciseDate(LocalDate.now())
                .durationMin(30)
                .caloriesKcal(200.5)
                .points(15)
                .setNo(3)
                .reps(12)
                .weightKg(40)
                .exerciseId(1L)
                .userId(1L)
                .build();

        exerciseRecordMapper.insert(record);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = exerciseRecordMapper.maxId();

        ExerciseRecordVO record = ExerciseRecordVO.builder()
                .exerciseRecordId(maxId)
                .exerciseDate(LocalDate.now())
                .durationMin(45)
                .caloriesKcal(280.0)
                .points(20)
                .setNo(4)
                .reps(10)
                .weightKg(45)
                .exerciseId(2L)
                .userId(2L)
                .build();

        exerciseRecordMapper.updateById(record);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = exerciseRecordMapper.maxId();
        exerciseRecordMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
