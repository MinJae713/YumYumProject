package com.ssafy.yumyum.domain.badge.exception;

public class NoBadgeCategoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoBadgeCategoryException() {
        super("해당 뱃지 카테고리가 없습니다.");
    }
}
