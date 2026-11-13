package com.ssafy.yumyum.domain.badge.exception;

public class NoBadgeProgressException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoBadgeProgressException() {
		super("해당 뱃지 연속 기록을 조회하는데 실패했습니다.");
	}
}
