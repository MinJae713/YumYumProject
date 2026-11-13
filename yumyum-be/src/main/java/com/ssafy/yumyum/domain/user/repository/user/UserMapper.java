package com.ssafy.yumyum.domain.user.repository.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
	List<UserVO> findAll();
	Optional<UserVO> findById(Long userId);
	int insert(UserVO userVO);
	int updateById(UserVO userVO);
	int deleteById(Long userId);
	int count();
	long maxId();
	int existsByEmail(String email);
	Optional<UserVO> findByEmail(String email);
	Optional<UserVO> findByEmailWithProviderAndRole(String email);
	Optional<UserVO> findByIdWithProviderAndRole(Long userId);
	int updatePasswordById(@Param("userId")Long userId, @Param("password")String newPassword);
	int softDeleteById(Long userId);
	int updateUserResearchById(UserVO userVO);
	List<UserVO> findByEmailLike(String email);
	int updatePoint(@Param("userId") Long userId, @Param("point") Integer point);
	List<UserVO> findUsersWithoutResearch(@Param("date") LocalDate date);
	int updateCurrentWeightByUserId(@Param("latestWeight") Double latestWeight, @Param("userId") Long userId);
	int updateRoleByUserId(@Param("userRoleId") Long userRoleId, @Param("userId") Long userId);
}
