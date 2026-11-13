package com.ssafy.yumyum.domain.meal.exception;

public class FoodMatchFailedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public FoodMatchFailedException() {
		super("음식 매칭에 실패하였습니다.");
	}
}
