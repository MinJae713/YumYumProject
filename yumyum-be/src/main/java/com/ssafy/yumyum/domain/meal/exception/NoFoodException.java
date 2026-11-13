package com.ssafy.yumyum.domain.meal.exception;

public class NoFoodException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoFoodException() {
		super("아이디 해당 음식이 없습니다.");
	}
}
