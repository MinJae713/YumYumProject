package com.ssafy.yumyum.domain.group.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupInfoDTO {
	private String groupName;
	private Long userId;
}
