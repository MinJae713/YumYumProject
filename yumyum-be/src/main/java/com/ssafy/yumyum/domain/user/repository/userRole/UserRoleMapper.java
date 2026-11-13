package com.ssafy.yumyum.domain.user.repository.userRole;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    List<UserRoleVO> findAll();
    Optional<UserRoleVO> findById(Long userRoleId);
    int insert(UserRoleVO userRoleVO);
    int updateById(UserRoleVO userRoleVO);
    int deleteById(Long userRoleId);
    int count();
	long maxId();
    Optional<UserRoleVO> findByUserRoleName(String userRoleName);
}
