package com.ssafy.yumyum.domain.exercise.exception;

public class ExerciseRecordUpdateException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ExerciseRecordUpdateException() {
		super("운동 기록 수정에 실패했습니다.");
	}
}
