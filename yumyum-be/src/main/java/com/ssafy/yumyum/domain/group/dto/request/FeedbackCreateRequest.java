package com.ssafy.yumyum.domain.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackCreateRequest {
    @NotNull(message = "피드백을 남길 대상 ID는 필수입니다.")
    private Long targetLogId;
    @NotBlank(message = "피드백 내용은 필수입니다.")
    private String content;
}
