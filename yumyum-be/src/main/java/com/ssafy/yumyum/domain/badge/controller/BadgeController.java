package com.ssafy.yumyum.domain.badge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetBadgeListResponse;
import com.ssafy.yumyum.domain.badge.service.BadgeService;
import com.ssafy.yumyum.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/badge")
@RequiredArgsConstructor
public class BadgeController {
	private final BadgeService badgeService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<GetBadgeListResponse>> getBadgeList(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(badgeService.getBadgeList(user.getUserId())));
	}
}
