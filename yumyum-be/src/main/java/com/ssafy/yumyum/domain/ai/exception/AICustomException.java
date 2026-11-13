package com.ssafy.yumyum.domain.ai.exception;

public class AICustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AICustomException() {
        super("AI 관련 커스텀 예외처리입니다.");
    }
}
