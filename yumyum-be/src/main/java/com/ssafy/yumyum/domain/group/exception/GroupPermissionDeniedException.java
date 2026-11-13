package com.ssafy.yumyum.domain.group.exception;

public class GroupPermissionDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupPermissionDeniedException() {
		super("그룹에 관한 권한이 없습니다.");
	}
}
