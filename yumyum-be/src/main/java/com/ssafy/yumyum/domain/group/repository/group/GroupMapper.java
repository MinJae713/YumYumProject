package com.ssafy.yumyum.domain.group.repository.group;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupMapper {
    List<GroupVO> findAll();
    Optional<GroupVO> findById(Long groupId);
    int insert(GroupVO groupVO);
    int updateById(GroupVO groupVO);
    int deleteById(Long groupId);
    int count();
	long maxId();
}
