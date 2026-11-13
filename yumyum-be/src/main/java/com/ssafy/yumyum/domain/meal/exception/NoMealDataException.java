package com.ssafy.yumyum.domain.meal.exception;

public class NoMealDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoMealDataException() {
		super("해당 식단 데이터가 없습니다.");
	}
}
