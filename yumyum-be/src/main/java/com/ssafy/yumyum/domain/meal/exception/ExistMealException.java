package com.ssafy.yumyum.domain.meal.exception;

public class ExistMealException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ExistMealException() {
		super("해당 끼니에는 이미 식단이 존재합니다.");
	}
}
