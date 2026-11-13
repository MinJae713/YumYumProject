package com.ssafy.yumyum.domain.meal.exception;

public class MealDateInputException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public MealDateInputException() {
		super("년도는 1900~2100 사이, 월은 1~12 사이여야 합니다.");
	}
}
