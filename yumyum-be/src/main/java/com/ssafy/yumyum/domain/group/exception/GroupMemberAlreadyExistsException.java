package com.ssafy.yumyum.domain.group.exception;

public class GroupMemberAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupMemberAlreadyExistsException() {
		super("이미 해당 그룹에 가입된 유저입니다.");
	}
}
