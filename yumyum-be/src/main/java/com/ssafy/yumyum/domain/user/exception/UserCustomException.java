package com.ssafy.yumyum.domain.user.exception;

public class UserCustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserCustomException() {
        super("User 관련 커스텀 예외처리입니다.");
    }
}
