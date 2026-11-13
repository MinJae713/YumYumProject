package com.ssafy.yumyum.domain.group.exception;

public class GroupInviteNoFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public GroupInviteNoFoundException() {
        super("존재하지 않는 초대 목록입니다.");
    }
}