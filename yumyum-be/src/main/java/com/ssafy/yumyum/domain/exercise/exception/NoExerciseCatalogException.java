package com.ssafy.yumyum.domain.exercise.exception;

public class NoExerciseCatalogException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoExerciseCatalogException() {
		super("해당 운동이 없습니다.");
	}
}
