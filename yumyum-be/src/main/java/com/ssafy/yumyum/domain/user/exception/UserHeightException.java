package com.ssafy.yumyum.domain.user.exception;

public class UserHeightException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public UserHeightException() {
		super("해당 유저의 키 값이 아직 입력되지 않았습니다.");
	}
}
