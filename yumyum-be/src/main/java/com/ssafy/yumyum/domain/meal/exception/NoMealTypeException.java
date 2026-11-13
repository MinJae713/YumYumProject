package com.ssafy.yumyum.domain.meal.exception;

public class NoMealTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoMealTypeException() {
		super("해당 식단 유형을 못찾았습니다.");
	}
}
