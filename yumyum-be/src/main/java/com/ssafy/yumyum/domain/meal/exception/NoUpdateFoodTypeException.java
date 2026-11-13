package com.ssafy.yumyum.domain.meal.exception;

public class NoUpdateFoodTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoUpdateFoodTypeException() {
		super("음식 정보 업데이트 유형을 못찾았습니다.");
	}
}
