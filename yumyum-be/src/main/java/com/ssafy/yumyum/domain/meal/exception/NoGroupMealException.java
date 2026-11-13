package com.ssafy.yumyum.domain.meal.exception;

public class NoGroupMealException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoGroupMealException() {
		super("해당 그룹장 추천 식단이 없습니다.");
	}
}
