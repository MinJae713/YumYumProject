package com.ssafy.yumyum.domain.exercise.exception;

public class ExerciseRecordDeleteException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ExerciseRecordDeleteException() {
		super("운동 기록 삭제에 실패했습니다.");
	}
}
