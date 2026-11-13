package com.ssafy.yumyum.domain.group.exception;

public class GroupNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupNotFoundException() {
		super("존재하지 않는 그룹입니다.");
	}
}
