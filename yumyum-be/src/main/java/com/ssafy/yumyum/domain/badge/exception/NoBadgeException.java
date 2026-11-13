package com.ssafy.yumyum.domain.badge.exception;

public class NoBadgeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoBadgeException() {
		super("해당 뱃지가 없습니다.");
	}
}
