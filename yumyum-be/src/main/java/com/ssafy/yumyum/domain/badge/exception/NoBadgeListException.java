package com.ssafy.yumyum.domain.badge.exception;

public class NoBadgeListException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoBadgeListException() {
		super("해당 뱃지 목록이 없습니다.");
	}
}
