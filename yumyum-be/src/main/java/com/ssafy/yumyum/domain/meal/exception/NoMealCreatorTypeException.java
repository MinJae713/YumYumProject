package com.ssafy.yumyum.domain.meal.exception;

public class NoMealCreatorTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoMealCreatorTypeException() {
		super("해당 식단 작성자를 못찾았습니다.");
	}
}
