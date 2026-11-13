package com.ssafy.yumyum.domain.group.repository.exerciseFeedback;

import java.util.List;
import java.util.Optional;

import com.ssafy.yumyum.domain.group.dto.response.FeedbackResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExerciseFeedbackMapper {
    List<ExerciseFeedbackVO> findAll();
    Optional<ExerciseFeedbackVO> findById(Long exerciseFeedbackId);
    int insert(ExerciseFeedbackVO exerciseFeedbackVO);
    int updateById(ExerciseFeedbackVO exerciseFeedbackVO);
    int deleteById(Long exerciseFeedbackId);
    int count();
	long maxId();
    List<FeedbackResponse> findAllByGroupIdAndExerciseRecordId(Long groupId, Long exerciseRecordId);
    boolean existsByExerciseRecordId(Long exerciseRecordId);
    int deleteByGroupId(Long groupId);
}
