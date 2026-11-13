package com.ssafy.yumyum.domain.user.exception;

public class WeightLogNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public WeightLogNotFoundException() {
		super("해당 유저의 몸무게 기록이 아직 입력되지 않았습니다.");
	}
}
