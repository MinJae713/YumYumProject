package com.ssafy.yumyum.domain.user.exception;

public class UserWeightException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public UserWeightException() {
		super("해당 유저의 무게 값이 아직 입력되지 않았습니다.");
	}
}
