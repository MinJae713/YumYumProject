package com.ssafy.yumyum.domain.badge.repository.badgeProgress;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.yumyum.domain.badge.types.StreakType;

@Mapper
public interface BadgeProgressMapper {
    List<BadgeProgressVO> findAll();
    Optional<BadgeProgressVO> findById(Long badgeProgressId);
    int insert(BadgeProgressVO badgeProgressVO);
    int updateById(BadgeProgressVO badgeProgressVO);
    int deleteById(Long badgeProgressId);
    int count();
	long maxId();
	Optional<BadgeProgressVO> findByUserIdAndStreakType(Long userId, StreakType streakType);
	List<BadgeProgressVO> findAllByUserId(Long userId);
}
