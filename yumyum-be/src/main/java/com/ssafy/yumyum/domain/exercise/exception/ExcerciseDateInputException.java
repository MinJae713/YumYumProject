package com.ssafy.yumyum.domain.exercise.exception;

public class ExcerciseDateInputException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ExcerciseDateInputException() {
		super("년도는 1900~2100 사이, 월은 1~12 사이여야 합니다.");
	}
}
