package com.ssafy.yumyum.domain.notification.repository.notificationType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationTypeVO {
    private Long notificationTypeId;
    private String notificationName;
}
