package com.ssafy.yumyum.domain.group.exception;

public class GroupInviteAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupInviteAlreadyExistsException() {
		super("이미 대기 중인 초대가 존재합니다.");
	}
}
