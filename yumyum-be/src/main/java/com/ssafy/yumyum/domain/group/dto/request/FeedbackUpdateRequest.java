package com.ssafy.yumyum.domain.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackUpdateRequest {
    @NotBlank(message = "수정할 내용을 입력해주세요.")
    private String content;
}
