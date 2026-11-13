package com.ssafy.yumyum.domain.group.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.group.dto.request.FeedbackCreateRequest;
import com.ssafy.yumyum.domain.group.dto.request.FeedbackUpdateRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupCreateRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupInviteActionRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupInviteRequest;
import com.ssafy.yumyum.domain.group.dto.request.GroupUpdateRequest;
import com.ssafy.yumyum.domain.group.dto.response.FeedbackResponse;
import com.ssafy.yumyum.domain.group.dto.response.GetRecordedDateResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupDailyLogResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupExerciseDetailResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupInfoResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupInviteListResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupListResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupMealDetailResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupMemberInfoResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupMemberRankingInfoResponse;
import com.ssafy.yumyum.domain.group.dto.response.GroupUpdateResponse;
import com.ssafy.yumyum.domain.group.service.GroupFeedbackService;
import com.ssafy.yumyum.domain.group.service.GroupMemberService;
import com.ssafy.yumyum.domain.group.service.GroupService;
import com.ssafy.yumyum.security.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final GroupFeedbackService groupFeedbackService;

    // ----------------------- 그룹 ----------------------------//
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<GroupInfoResponse>> createGroup(@Valid @RequestBody GroupCreateRequest groupCreateRequest, @AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(groupService.createGroup(groupCreateRequest, user.getUserId())));

    }

    @GetMapping("/group-list")
    public ResponseEntity<ApiResponse<List<GroupListResponse>>> getGroupList(@AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(groupService.getGroupList(user.getUserId())));
    }

    @PutMapping("/{groupId}/group-update")
    public ResponseEntity<ApiResponse<GroupUpdateResponse>> updateGroupName(@Valid @RequestBody GroupUpdateRequest groupUpdateRequest, @PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(groupService.updateGroupName(groupUpdateRequest, groupId, user.getUserId())));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse<String>> deleteGroup(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserPrincipal user){
        groupService.deleteGroup(groupId, user.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok("성공적으로 그룹을 삭제했습니다."));
    }

    // ----------------------- 그룹 페이지 ----------------------------//
    @GetMapping("/{groupId}/ranking")
    public ResponseEntity<ApiResponse<List<GroupMemberRankingInfoResponse>>> getGroupMemberRanking(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserPrincipal user){

        List<GroupMemberRankingInfoResponse> list = groupService.getGroupMemberRanking(groupId, user.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(list));
    }

    @GetMapping("/{groupId}/group-members")
    public ResponseEntity<ApiResponse<List<GroupMemberInfoResponse>>> getGroupMemberList(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserPrincipal user){
        List<GroupMemberInfoResponse> list = groupService.getGroupMemberList(groupId, user.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(list));
    }

    @GetMapping("/{groupId}/dailyLog")
    public ResponseEntity<ApiResponse<GroupDailyLogResponse>> getDailyLogs(
            @PathVariable("groupId") Long groupId,
            @RequestParam(required = false) Long groupMemberId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal UserPrincipal user
            ){

        GroupDailyLogResponse groupDailyLogResponse = groupService.getDailyLogs(groupId, groupMemberId, date, user.getUserId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(groupDailyLogResponse));
    }

    @GetMapping("/{groupId}/meal/{mealId}/detail")
    public ResponseEntity<ApiResponse<GroupMealDetailResponse>> getGroupMealDetail(
            @PathVariable Long groupId,
            @PathVariable Long mealId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        GroupMealDetailResponse response = groupService.getGroupMealDetail(groupId, mealId, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{groupId}/exercise/{exerciseRecordId}/detail")
    public ResponseEntity<ApiResponse<GroupExerciseDetailResponse>> getGroupExerciseDetail(
            @PathVariable Long groupId,
            @PathVariable Long exerciseRecordId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        GroupExerciseDetailResponse response = groupService.getGroupExerciseDetail(groupId, exerciseRecordId, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
    
    @GetMapping("/{groupId}/recordedDate")
    public ResponseEntity<ApiResponse<GetRecordedDateResponse>> getRecordedDate(
    		@PathVariable Long groupId,
            @RequestParam(required = false) Long groupMemberId,
            @AuthenticationPrincipal UserPrincipal user) {
    	return ResponseEntity.ok(ApiResponse.ok(groupService.getRecordedDate(groupId, groupMemberId, user.getUserId())));
    }

    // ----------------------- 그룹 멤버 ----------------------------//
    @PostMapping("/{groupId}/invite")
    public ResponseEntity<ApiResponse<String>> inviteGroupMember(@Valid @RequestBody GroupInviteRequest groupInviteRequest, @PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserPrincipal user){
        groupMemberService.inviteGroupMember(groupInviteRequest, groupId, user.getUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("초대 요청을 보냈습니다."));
    }

    @GetMapping("/invite-list")
    public ResponseEntity<ApiResponse<List<GroupInviteListResponse>>> getInviteList(@AuthenticationPrincipal UserPrincipal user){
        List<GroupInviteListResponse> list = groupMemberService.getInviteList(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping("/invite-list")
    public ResponseEntity<ApiResponse<String>> responseToInvite(@Valid @RequestBody GroupInviteActionRequest groupInviteActionRequest, @AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity.ok(ApiResponse.ok(groupMemberService.responseToInvite(groupInviteActionRequest, user.getUserId())));
    }

    @DeleteMapping("/{groupId}/group-member")
    public ResponseEntity<ApiResponse<?>> withdrawGroupMember(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserPrincipal user){
        groupMemberService.withdrawGroupMember(groupId, user.getUserId());
        return ResponseEntity.ok().body(ApiResponse.ok("그룹 멤버 탈퇴에 성공했습니다."));
    }

    @DeleteMapping("/{groupId}/group-member/{groupMemberId}")
    public ResponseEntity<ApiResponse<?>> expelGroupMember(@PathVariable("groupId") Long groupId, @PathVariable("groupMemberId") Long groupMemberId, @AuthenticationPrincipal UserPrincipal user){
        groupMemberService.expelGroupMember(groupId, groupMemberId, user.getUserId());
        return ResponseEntity.ok().body(ApiResponse.ok("그룹 멤버 강퇴에 성공했습니다."));
    }


    // ----------------------- 피드백 ----------------------------//
    @PostMapping("/{groupId}/meal/feedback")
    public ResponseEntity<ApiResponse<FeedbackResponse>> createMealFeedback(
            @PathVariable("groupId") Long groupId,
            @Valid @RequestBody FeedbackCreateRequest feedbackCreateRequest,
            @AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(groupFeedbackService.createMealFeedback(groupId, feedbackCreateRequest, user.getUserId())));
    }

    @PostMapping("/{groupId}/exercise/feedback")
    public ResponseEntity<ApiResponse<FeedbackResponse>> createExerciseFeedback(
            @PathVariable("groupId") Long groupId,
            @Valid @RequestBody FeedbackCreateRequest feedbackCreateRequest,
            @AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(groupFeedbackService.createExerciseFeedback(groupId, feedbackCreateRequest, user.getUserId())));
    }

    @GetMapping("/{groupId}/meal/{mealId}/feedback")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getMealFeedback(@PathVariable("groupId") Long groupId, @PathVariable("mealId") Long mealId){
        return ResponseEntity.ok().body(ApiResponse.ok(groupFeedbackService.getMealFeedback(groupId, mealId)));
    }

    @GetMapping("/{groupId}/exercise/{exerciseRecordId}/feedback")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getExerciseFeedback(@PathVariable("groupId") Long groupId, @PathVariable("exerciseRecordId") Long exerciseRecordId){
        return ResponseEntity.ok().body(ApiResponse.ok(groupFeedbackService.getExerciseFeedback(groupId, exerciseRecordId)));
    }

    @PatchMapping("/{groupId}/meal/feedback/{mealFeedbackId}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> updateMealFeedback(
            @PathVariable("groupId") Long groupId,
            @PathVariable("mealFeedbackId") Long mealFeedbackId,
            @Valid @RequestBody FeedbackUpdateRequest feedbackUpdateRequest,
            @AuthenticationPrincipal UserPrincipal user){

        return ResponseEntity.ok()
                .body(ApiResponse.ok(groupFeedbackService.updateMealFeedback(groupId, mealFeedbackId, feedbackUpdateRequest, user.getUserId())));
    }

    @PatchMapping("/{groupId}/exercise/feedback/{exerciseFeedbackId}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> updateExerciseFeedback(
            @PathVariable("groupId") Long groupId,
            @PathVariable("exerciseFeedbackId") Long exerciseFeedbackId,
            @Valid @RequestBody FeedbackUpdateRequest feedbackUpdateRequest,
            @AuthenticationPrincipal UserPrincipal user){

        return ResponseEntity.ok()
                .body(ApiResponse.ok(groupFeedbackService.updateExerciseFeedback(groupId, exerciseFeedbackId, feedbackUpdateRequest, user.getUserId())));
    }

    @DeleteMapping("/{groupId}/meal/feedback/{mealFeedbackId}")
    public ResponseEntity<ApiResponse<String>> deleteMealFeedback(
            @PathVariable("groupId") Long groupId,
            @PathVariable("mealFeedbackId") Long mealFeedbackId,
            @AuthenticationPrincipal UserPrincipal user){
        groupFeedbackService.deleteMealFeedback(groupId, mealFeedbackId, user.getUserId());
        return ResponseEntity.ok()
                .body(ApiResponse.ok("식단 피드백이 삭제되었습니다."));
    }

    @DeleteMapping("/{groupId}/exercise/feedback/{exerciseFeedbackId}")
    public ResponseEntity<ApiResponse<String>> deleteExerciseFeedback(
            @PathVariable("groupId") Long groupId,
            @PathVariable("exerciseFeedbackId") Long exerciseFeedbackId,
            @AuthenticationPrincipal UserPrincipal user){
        groupFeedbackService.deleteExerciseFeedback(groupId, exerciseFeedbackId, user.getUserId());
        return ResponseEntity.ok()
                .body(ApiResponse.ok("운동 피드백이 삭제되었습니다."));
    }


}
