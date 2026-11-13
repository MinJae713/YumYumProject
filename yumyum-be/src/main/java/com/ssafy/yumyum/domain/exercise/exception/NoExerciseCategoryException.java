package com.ssafy.yumyum.domain.exercise.exception;

public class NoExerciseCategoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoExerciseCategoryException() {
		super("해당 운동 종류가 없습니다.");
	}
}
