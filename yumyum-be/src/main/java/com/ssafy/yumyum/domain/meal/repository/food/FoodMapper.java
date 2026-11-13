package com.ssafy.yumyum.domain.meal.repository.food;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodMapper {
    List<FoodVO> findAll();
    Optional<FoodVO> findById(Long foodId);
    int insert(FoodVO foodVO);
    int updateById(FoodVO foodVO);
    int deleteById(Long foodId);
    int count();
	long maxId();
	int countByFoodName(String foodName);
	List<FoodVO> findAllByFoodName(String foodName);
    Optional<FoodVO> findByFoodName(String foodName);
    int countByKeyword(String keyword);
    List<FoodVO> selectByKeywordWithPaging(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);
    Optional<FoodVO> findByFoodNameIgnoreSpace(@Param("cleanName") String cleanName);
    Optional<FoodVO> findByFoodNameContained(@Param("cleanName") String cleanName);
}
