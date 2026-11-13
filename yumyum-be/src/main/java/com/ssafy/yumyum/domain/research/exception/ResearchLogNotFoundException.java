package com.ssafy.yumyum.domain.research.exception;

public class ResearchLogNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ResearchLogNotFoundException() {
        super("존재하지 않는 리서치 로그입니다.");
    }
}
