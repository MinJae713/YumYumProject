package com.ssafy.yumyum.domain.notification.repository.notificationType;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationTypeMapper {
    List<NotificationTypeVO> findAll();
    Optional<NotificationTypeVO> findById(Long notificationTypeId);
    int insert(NotificationTypeVO notificationTypeVO);
    int updateById(NotificationTypeVO notificationTypeVO);
    int deleteById(Long notificationTypeId);
    int count();
	long maxId();
}
