package com.ssafy.yumyum.domain.dashboard.exception;

public class DashboardCustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DashboardCustomException() {
        super("Dashboard 관련 커스텀 예외처리입니다.");
    }
}
