package com.ssafy.yumyum.domain.group.exception;

public class GroupMemberNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupMemberNotFoundException() {
		super("존재하지 않는 그룹원입니다.");
	}
}
