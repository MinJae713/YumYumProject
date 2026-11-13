package com.ssafy.yumyum.domain.meal.repository.foodCache;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodCacheMapper {

    Optional<FoodCacheVO> findByFoodName(String foodName);

    int insert(FoodCacheVO foodCache);

    int updateById(FoodCacheVO foodCache);

    List<FoodCacheVO> findByStatus(String status);

    Optional<FoodCacheVO> findByFoodNameIgnoreSpace(@Param("cleanName") String cleanName);
}
