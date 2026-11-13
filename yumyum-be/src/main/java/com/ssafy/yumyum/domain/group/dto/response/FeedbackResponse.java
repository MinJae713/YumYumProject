package com.ssafy.yumyum.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {
    private Long feedbackId;
    private String content;
    private LocalDateTime createdAt;
    private Long writerUserId;
    private String writerName;
    private String writerRole;
}
