package com.ssafy.yumyum.domain.exercise.repository.exerciseRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExerciseRecordMapper {
    List<ExerciseRecordVO> findAll();
    Optional<ExerciseRecordVO> findById(Long exerciseRecordId);
    int insert(ExerciseRecordVO exerciseRecordVO);
    int updateById(ExerciseRecordVO exerciseRecordVO);
    int deleteById(Long exerciseRecordId);
    int count();
	long maxId();
	List<ExerciseRecordVO> findAllByUserIdAndExerciseDate(Long userId, LocalDate exerciseDate);
	List<LocalDate> findExerciseDateByUserIdAndExerciseYearAndExerciseMonth(Long userId, Integer year, Integer month);
	List<ExerciseRecordVO> findAllByUserIdAndExerciseDateBetweenStartDateAndEndDateOrderByExerciseDate(Long userId, LocalDate startDate, LocalDate endDate);
	List<ExerciseRecordVO> findAllByUserId(Long userId);
}
