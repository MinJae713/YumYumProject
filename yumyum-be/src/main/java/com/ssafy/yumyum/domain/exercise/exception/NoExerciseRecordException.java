package com.ssafy.yumyum.domain.exercise.exception;

public class NoExerciseRecordException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoExerciseRecordException() {
		super("해당 운동 기록이 없습니다.");
	}
}
