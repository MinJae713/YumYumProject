package com.ssafy.yumyum.domain.badge.repository.userBadge;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBadgeMapper {
    List<UserBadgeVO> findAll();
    Optional<UserBadgeVO> findById(Long userBadgeId);
    int insert(UserBadgeVO userBadgeVO);
    int updateById(UserBadgeVO userBadgeVO);
    int deleteById(Long userBadgeId);
    int count();
	long maxId();
	List<UserBadgeVO> findAllByUserId(Long userId);
	Optional<UserBadgeVO> findByUserIdAndBadgeId(Long userId, Long badgeId);
	List<UserBadgeVO> findTop5ByUserIdOrderByAchievedAtDesc(Long userId);
}
