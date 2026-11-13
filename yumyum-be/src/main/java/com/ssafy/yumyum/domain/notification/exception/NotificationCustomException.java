package com.ssafy.yumyum.domain.notification.exception;

public class NotificationCustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NotificationCustomException() {
        super("Notification관련 커스텀 예외처리입니다.");
    }
}
