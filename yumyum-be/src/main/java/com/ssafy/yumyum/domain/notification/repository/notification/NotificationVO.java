package com.ssafy.yumyum.domain.notification.repository.notification;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationVO {
    private Long notificationId;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private Long userId;
    private Long notificationTypeId;
}
