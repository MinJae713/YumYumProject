package com.ssafy.yumyum.domain.mainpage.exception;

public class MainpageCustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public MainpageCustomException() {
        super("Mainpage 관련 커스텀 예외처리입니다.");
    }
}
