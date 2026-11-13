package com.ssafy.yumyum.domain.group.exception;

public class GroupInviteAlreadyResponseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupInviteAlreadyResponseException() {
		super("이미 응답을 완료한 초대입니다.");
	}
}
