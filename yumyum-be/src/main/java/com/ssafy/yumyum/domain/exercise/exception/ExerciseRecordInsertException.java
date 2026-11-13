package com.ssafy.yumyum.domain.exercise.exception;

public class ExerciseRecordInsertException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ExerciseRecordInsertException() {
		super("운동 기록 추가에 실패했습니다.");
	}
}
