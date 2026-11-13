package com.ssafy.yumyum.domain.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.common.exception.commonExceptions.DeleteFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.exercise.service.ExerciseService;
import com.ssafy.yumyum.domain.exercise.service.dto.ExerciseRecordInfoDTO;
import com.ssafy.yumyum.domain.group.dto.request.FeedbackCreateRequest;
import com.ssafy.yumyum.domain.group.dto.request.FeedbackUpdateRequest;
import com.ssafy.yumyum.domain.group.dto.response.FeedbackResponse;
import com.ssafy.yumyum.domain.group.exception.FeedbackNoFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupMemberNotFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupPermissionDeniedException;
import com.ssafy.yumyum.domain.group.repository.exerciseFeedback.ExerciseFeedbackMapper;
import com.ssafy.yumyum.domain.group.repository.exerciseFeedback.ExerciseFeedbackVO;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberMapper;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberVO;
import com.ssafy.yumyum.domain.group.repository.mealFeedback.MealFeedbackMapper;
import com.ssafy.yumyum.domain.group.repository.mealFeedback.MealFeedbackVO;
import com.ssafy.yumyum.domain.group.types.GroupRole;
import com.ssafy.yumyum.domain.meal.service.MealService;
import com.ssafy.yumyum.domain.meal.service.dto.MealInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupFeedbackServiceImpl implements GroupFeedbackService {

    private final MealFeedbackMapper mealFeedbackMapper;
    private final GroupMemberMapper groupMemberMapper;

    private final MealService mealService;
    private final ExerciseService exerciseService;
    private final ExerciseFeedbackMapper exerciseFeedbackMapper;

    @Override
    @Transactional
    public FeedbackResponse createMealFeedback(Long groupId, FeedbackCreateRequest feedbackCreateRequest, Long userId) {
        // 현재 유저가 해당 그룹의 멤버인지 확인
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // targetLogId로 피드백을 달을 식단 정보 조회 -> 존재하는 식단인지
        MealInfoDTO mealInfoDTO = mealService.getMealInfo(feedbackCreateRequest.getTargetLogId());

        // 권한 체크 (본인이던지, Leader 던지)
        boolean isMyMeal = mealInfoDTO.getUserId().equals(userId);
        boolean isLeader = GroupRole.LEADER.name().equals(groupMemberVO.getRole());

        // 내 글도 아니고, 리더도 아니면 -> 권한 없음
        if (!isMyMeal && !isLeader) {
            throw new GroupPermissionDeniedException();
        }

        // 피드백 VO 작성
        MealFeedbackVO mealFeedbackVO = MealFeedbackVO.builder()
                .content(feedbackCreateRequest.getContent())
                .mealId(mealInfoDTO.getMealId())
                .groupMemberId(groupMemberVO.getGroupMemberId())
                .build();

        // mapper에 저장
        if(mealFeedbackMapper.insert(mealFeedbackVO) < 1){
            throw new InsertFailedException("식단 피드백 생성에 실패했습니다.");
        }

        String writerName = groupMemberMapper.findUserNameByGroupMemberId(groupMemberVO.getGroupMemberId());

        return FeedbackResponse.builder()
                .feedbackId(mealFeedbackVO.getMealFeedbackId())
                .content(mealFeedbackVO.getContent())
                .createdAt(mealFeedbackVO.getCreatedAt())
                .writerUserId(userId)
                .writerName(writerName)
                .writerRole(groupMemberVO.getRole())
                .build();
    }

    @Override
    public FeedbackResponse createExerciseFeedback(Long groupId, FeedbackCreateRequest feedbackCreateRequest, Long userId) {
        // 현재 유저가 해당 그룹의 멤버인지 확인
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // targetLogId로 피드백을 달을 식단 정보 조회 -> 존재하는 식단인지
        ExerciseRecordInfoDTO exerciseRecordInfoDTO = exerciseService.getExerciseRecordInfo(feedbackCreateRequest.getTargetLogId());

        // 권한 체크 (본인이던지, Leader 던지)
        boolean isMyExerciseRecord = exerciseRecordInfoDTO.getUserId().equals(userId);
        boolean isLeader = GroupRole.LEADER.name().equals(groupMemberVO.getRole());

        // 내 글도 아니고, 리더도 아니면 -> 권한 없음
        if (!isMyExerciseRecord && !isLeader) {
            throw new GroupPermissionDeniedException();
        }

        // 피드백 VO 작성
        ExerciseFeedbackVO exerciseFeedbackVO = ExerciseFeedbackVO.builder()
                .content(feedbackCreateRequest.getContent())
                .exerciseRecordId(exerciseRecordInfoDTO.getExerciseRecordId())
                .groupMemberId(groupMemberVO.getGroupMemberId())
                .build();

        // mapper에 저장
        if(exerciseFeedbackMapper.insert(exerciseFeedbackVO) < 1){
            throw new InsertFailedException("운동 피드백 생성에 실패했습니다.");
        }

        String writerName = groupMemberMapper.findUserNameByGroupMemberId(groupMemberVO.getGroupMemberId());

        return FeedbackResponse.builder()
                .feedbackId(exerciseFeedbackVO.getExerciseFeedbackId())
                .content(exerciseFeedbackVO.getContent())
                .createdAt(exerciseFeedbackVO.getCreatedAt())
                .writerUserId(userId)
                .writerName(writerName)
                .writerRole(groupMemberVO.getRole())
                .build();
    }

    @Override
    @Transactional
    public List<FeedbackResponse> getMealFeedback(Long groupId, Long mealId) {
        return mealFeedbackMapper.findAllByGroupIdAndMealId(groupId, mealId);
    }

    @Override
    public List<FeedbackResponse> getExerciseFeedback(Long groupId, Long exerciseRecordId) {
        return exerciseFeedbackMapper.findAllByGroupIdAndExerciseRecordId(groupId, exerciseRecordId);
    }

    @Override
    public FeedbackResponse updateMealFeedback(Long groupId, Long mealFeedbackId, FeedbackUpdateRequest feedbackUpdateRequest, Long userId) {
        // 로그인 된 유저정보 조회
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // 수정할 피드백 데이터 조회
        MealFeedbackVO mealFeedbackVO = mealFeedbackMapper.findById(mealFeedbackId).orElseThrow(FeedbackNoFoundException::new);

        // 권한 체크 -> 본인만
        if(!mealFeedbackVO.getGroupMemberId().equals(groupMemberVO.getGroupMemberId())){
            throw new GroupPermissionDeniedException();
        }

        // 세터로 content값 변경
        mealFeedbackVO.setContent(feedbackUpdateRequest.getContent());

        // update쿼리
        if(mealFeedbackMapper.updateById(mealFeedbackVO) < 1){
            throw new UpdateFailedException("식단 피드백 수정을 실패했습니다.");
        }

        // 변경된 내용 리턴
        String writerName = groupMemberMapper.findUserNameByGroupMemberId(groupMemberVO.getGroupMemberId());

        return FeedbackResponse.builder()
                .feedbackId(mealFeedbackVO.getMealFeedbackId())
                .content(mealFeedbackVO.getContent())
                .createdAt(mealFeedbackVO.getCreatedAt())
                .writerUserId(userId)
                .writerName(writerName)
                .writerRole(groupMemberVO.getRole())
                .build();

    }

    @Override
    public FeedbackResponse updateExerciseFeedback(Long groupId, Long exerciseFeedbackId, FeedbackUpdateRequest feedbackUpdateRequest, Long userId) {
        // 로그인 된 유저정보 조회
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // 수정할 피드백 데이터 조회
        ExerciseFeedbackVO exerciseFeedbackVO = exerciseFeedbackMapper.findById(exerciseFeedbackId).orElseThrow(FeedbackNoFoundException::new);

        // 권한 체크 -> 본인만
        if(!exerciseFeedbackVO.getGroupMemberId().equals(groupMemberVO.getGroupMemberId())){
            throw new GroupPermissionDeniedException();
        }

        // 세터로 content값 변경
        exerciseFeedbackVO.setContent(feedbackUpdateRequest.getContent());

        // update쿼리
        if(exerciseFeedbackMapper.updateById(exerciseFeedbackVO) < 1){
            throw new UpdateFailedException("운동 피드백 수정을 실패했습니다.");
        }

        // 변경된 내용 리턴
        String writerName = groupMemberMapper.findUserNameByGroupMemberId(groupMemberVO.getGroupMemberId());

        return FeedbackResponse.builder()
                .feedbackId(exerciseFeedbackVO.getExerciseFeedbackId())
                .content(exerciseFeedbackVO.getContent())
                .createdAt(exerciseFeedbackVO.getCreatedAt())
                .writerUserId(userId)
                .writerName(writerName)
                .writerRole(groupMemberVO.getRole())
                .build();
    }

    @Override
    public void deleteMealFeedback(Long groupId, Long mealFeedbackId, Long userId) {
        // 로그인 된 유저정보 조회
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // 수정할 피드백 데이터 조회
        MealFeedbackVO mealFeedbackVO = mealFeedbackMapper.findById(mealFeedbackId).orElseThrow(FeedbackNoFoundException::new);

        // 권한 체크 -> 본인만
        if(!mealFeedbackVO.getGroupMemberId().equals(groupMemberVO.getGroupMemberId())){
            throw new GroupPermissionDeniedException();
        }

        if(mealFeedbackMapper.deleteById(mealFeedbackId) < 1){
            throw new DeleteFailedException("식단 피드맥 삭제에 실패했습니다.");
        }
    }

    @Override
    public void deleteExerciseFeedback(Long groupId, Long exerciseFeedbackId, Long userId) {
        // 로그인 된 유저정보 조회
        GroupMemberVO groupMemberVO = groupMemberMapper.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);

        // 수정할 피드백 데이터 조회
        ExerciseFeedbackVO exerciseFeedbackVO = exerciseFeedbackMapper.findById(exerciseFeedbackId).orElseThrow(FeedbackNoFoundException::new);

        // 권한 체크 -> 본인만
        if(!exerciseFeedbackVO.getGroupMemberId().equals(groupMemberVO.getGroupMemberId())){
            throw new GroupPermissionDeniedException();
        }
        if(exerciseFeedbackMapper.deleteById(exerciseFeedbackId) < 1){
            throw new DeleteFailedException("운동 피드백 삭제에 실패했습니다.");
        }
    }


}
