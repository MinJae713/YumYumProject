package com.ssafy.yumyum.domain.meal.exception;

public class FoodCacheNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public FoodCacheNotFoundException() {
		super("캐시 테이블에 존재하지 않는 음식입니다.");
	}
}
