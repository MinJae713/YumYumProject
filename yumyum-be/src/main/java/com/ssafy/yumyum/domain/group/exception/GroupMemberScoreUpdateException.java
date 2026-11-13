package com.ssafy.yumyum.domain.group.exception;

public class GroupMemberScoreUpdateException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupMemberScoreUpdateException() {
		super("그룹원 점수 수정에 실패했습니다.");
	}
}
