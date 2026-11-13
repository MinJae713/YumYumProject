package com.ssafy.yumyum.domain.meal.exception;

public class NoMealAIDetailException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoMealAIDetailException() {
		super("해당 식단에 대한 AI 세부 정보가 없습니다.");
	}
}
