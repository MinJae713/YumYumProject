package com.ssafy.yumyum.domain.meal.exception;

public class NoFoodUnitException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoFoodUnitException() {
		super("해당 음식 단위를 못찾았습니다");
	}
}
