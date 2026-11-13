package com.ssafy.yumyum.domain.badge.repository.badgeCategory;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BadgeCategoryMapper {
    List<BadgeCategoryVO> findAll();
    Optional<BadgeCategoryVO> findById(Long badgeCategoryId);
    int insert(BadgeCategoryVO badgeCategoryVO);
    int updateById(BadgeCategoryVO badgeCategoryVO);
    int deleteById(Long badgeCategoryId);
    int count();
	long maxId();
}
