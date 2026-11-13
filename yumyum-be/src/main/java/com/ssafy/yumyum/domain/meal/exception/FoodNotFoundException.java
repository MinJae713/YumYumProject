package com.ssafy.yumyum.domain.meal.exception;

public class FoodNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public FoodNotFoundException() {
		super("존재하지 않는 음식입니다.");
	}
}
