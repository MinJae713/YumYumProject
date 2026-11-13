package com.ssafy.yumyum.domain.group.exception;

public class FeedbackNoFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public FeedbackNoFoundException() {
        super("존재하지 않는 피드백 입니다.");
    }
}