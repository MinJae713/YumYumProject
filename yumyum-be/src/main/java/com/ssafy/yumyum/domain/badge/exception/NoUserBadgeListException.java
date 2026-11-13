package com.ssafy.yumyum.domain.badge.exception;

public class NoUserBadgeListException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoUserBadgeListException() {
		super("해당 유저가 획득한 뱃지 목록이 없습니다.");
	}
}
