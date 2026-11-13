package com.ssafy.yumyum.domain.badge.repository.badge;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.yumyum.domain.badge.types.BadgeType;

@Mapper
public interface BadgeMapper {
	List<BadgeVO> findAll();
    Optional<BadgeVO> findById(Long badgeId);
    int insert(BadgeVO badgeVO);
    int updateById(BadgeVO badgeVO);
    int deleteById(Long badgeId);
    int count();
	long maxId();
	Optional<BadgeVO> findByBadgeType(BadgeType badgeType);
}
