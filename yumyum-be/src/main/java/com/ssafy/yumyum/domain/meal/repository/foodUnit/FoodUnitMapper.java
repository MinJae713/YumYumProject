package com.ssafy.yumyum.domain.meal.repository.foodUnit;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodUnitMapper {
    List<FoodUnitVO> findAll();
    Optional<FoodUnitVO> findById(Long foodUnitId);
    int insert(FoodUnitVO foodUnitVO);
    int updateById(FoodUnitVO foodUnitVO);
    int deleteById(Long foodUnitId);
    int count();
	long maxId();
	Optional<Long> findIdByUnitName(String unitName);
}
