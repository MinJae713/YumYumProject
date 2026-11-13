package com.ssafy.yumyum.domain.notification.repository.notification;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    List<NotificationVO> findAll();
    Optional<NotificationVO> findById(Long notificationId);
    int insert(NotificationVO notificationVO);
    int updateById(NotificationVO notificationVO);
    int deleteById(Long notificationId);
    int count();
	long maxId();
}
