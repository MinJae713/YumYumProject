package com.ssafy.yumyum.domain.research.exception;

public class ResearchCustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ResearchCustomException() {
        super("Research관련 커스텀 예외처리입니다.");
    }
}
