package com.ssafy.yumyum.domain.user.repository.provider;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProviderMapper {
    List<ProviderVO> findAll();
    Optional<ProviderVO> findById(Long providerId);
    int insert(ProviderVO providerVO);
    int updateById(ProviderVO providerVO);
    int deleteById(Long providerId);
    int count();
	long maxId();
}
