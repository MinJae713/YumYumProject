package com.ssafy.yumyum.domain.user.repository.weightLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WeightLogMapper {
    List<WeightLogVO> findAll();
    Optional<WeightLogVO> findById(Long weightLogId);
    int insert(WeightLogVO weightLogVO);
    int updateById(WeightLogVO weightLogVO);
    int deleteById(Long weightLogId);
    int count();
	long maxId();
	List<WeightLogVO> findAllByUserIdOrderByLogDate(Long userId);

    Optional<WeightLogVO> findByDateAndUserId(@Param("logDate") LocalDate logDate, @Param("userId") Long userId);
    List<WeightLogVO> findAllByUserIdOrderByDate(Long userId);
    Double findLatestWeightByUserId(Long userId);
}
