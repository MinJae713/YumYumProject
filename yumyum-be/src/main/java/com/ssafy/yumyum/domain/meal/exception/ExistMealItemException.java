package com.ssafy.yumyum.domain.meal.exception;

public class ExistMealItemException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ExistMealItemException() {
		super("식단 음식이 이미 존재합니다.");
	}
}
