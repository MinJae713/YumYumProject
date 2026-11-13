package com.ssafy.yumyum.domain.meal.exception;

public class NoMealItemDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoMealItemDataException() {
		super("식단 해당 음식 정보가 없습니다.");
	}
}
