package com.ssafy.yumyum.domain.meal.exception;

public class MealInsertException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public MealInsertException() {
		super("식단 삽입에 실패했습니다.");
	}
}
