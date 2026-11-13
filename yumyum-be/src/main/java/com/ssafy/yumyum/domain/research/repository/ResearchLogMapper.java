package com.ssafy.yumyum.domain.research.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ResearchLogMapper {
    List<ResearchLogVO> findAll();
    Optional<ResearchLogVO> findById(@Param("researchId") Long researchId);
    int insert(ResearchLogVO researchLogVO);
    int updateById(ResearchLogVO researchLogVO);
    int deleteById(@Param("researchId") Long researchId);
    int count();
    Long maxId();
    List<ResearchLogVO> findAllByUserId(@Param("userId") Long userId);
    List<ResearchLogVO> findTop7ByUserIdOrderByResearchDateDesc(@Param("userId") Long userId);
    Optional<ResearchLogVO> findByUserIdAndResearchDate(
            @Param("userId") Long userId,
            @Param("researchDate") LocalDate researchDate
    );
}
