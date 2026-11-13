package com.ssafy.yumyum.domain.group.service;

import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.domain.group.dto.request.FeedbackCreateRequest;
import com.ssafy.yumyum.domain.group.dto.request.FeedbackUpdateRequest;
import com.ssafy.yumyum.domain.group.dto.response.FeedbackResponse;
import com.ssafy.yumyum.domain.group.exception.GroupPermissionDeniedException;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberMapper;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberVO;
import com.ssafy.yumyum.domain.group.repository.mealFeedback.MealFeedbackMapper;
import com.ssafy.yumyum.domain.group.repository.mealFeedback.MealFeedbackVO;
import com.ssafy.yumyum.domain.group.types.GroupRole;
import com.ssafy.yumyum.domain.meal.service.MealService;
import com.ssafy.yumyum.domain.meal.service.dto.MealInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GroupFeedbackServiceImplTest {
    @Mock
    private MealFeedbackMapper mealFeedbackMapper;
    @Mock
    private GroupMemberMapper groupMemberMapper;
    @Mock
    private MealService mealService;
    // 운동 관련 Mock은 식단과 로직이 같으므로 생략하거나 필요 시 추가

    @InjectMocks
    private GroupFeedbackServiceImpl groupFeedbackService;

    // --- 테스트 데이터 상수 ---
    private final Long GROUP_ID = 1L;
    private final Long USER_ID = 100L;
    private final Long MEMBER_ID = 10L;
    private final Long MEAL_ID = 500L;
    private final Long FEEDBACK_ID = 999L;

    @Test
    @DisplayName("식단 피드백 생성 성공 - 작성자 본인일 때")
    void createMealFeedback_Success_Owner() {
        // Given
        FeedbackCreateRequest request = new FeedbackCreateRequest();
        ReflectionTestUtils.setField(request, "targetLogId", MEAL_ID);
        ReflectionTestUtils.setField(request, "content", "굿굿");

        GroupMemberVO memberVO = GroupMemberVO.builder()
                .groupMemberId(MEMBER_ID)
                .userId(USER_ID)
                .role(GroupRole.MEMBER.name())
                .build();

        MealInfoDTO mealInfoDTO = MealInfoDTO.builder()
                .mealId(MEAL_ID)
                .userId(USER_ID) // 식단 주인 = 나
                .build();

        // Mocking
        given(groupMemberMapper.findByGroupIdAndUserId(GROUP_ID, USER_ID)).willReturn(Optional.of(memberVO));
        given(mealService.getMealInfo(MEAL_ID)).willReturn(mealInfoDTO);
        given(mealFeedbackMapper.insert(any(MealFeedbackVO.class))).willReturn(1); // 성공 시 1 반환
        given(groupMemberMapper.findUserNameByGroupMemberId(MEMBER_ID)).willReturn("테스터");

        // When
        FeedbackResponse response = groupFeedbackService.createMealFeedback(GROUP_ID, request, USER_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("굿굿");
        assertThat(response.getWriterName()).isEqualTo("테스터");
        verify(mealFeedbackMapper, times(1)).insert(any());
    }

    @Test
    @DisplayName("식단 피드백 생성 성공 - 리더일 때 (남의 글)")
    void createMealFeedback_Success_Leader() {
        // Given
        Long otherUserId = 200L; // 식단 주인은 다른 사람
        FeedbackCreateRequest request = new FeedbackCreateRequest();
        ReflectionTestUtils.setField(request, "targetLogId", MEAL_ID);
        ReflectionTestUtils.setField(request, "content", "피드백");

        GroupMemberVO leaderVO = GroupMemberVO.builder()
                .groupMemberId(MEMBER_ID)
                .userId(USER_ID)
                .role(GroupRole.LEADER.name()) // 나는 리더
                .build();

        MealInfoDTO mealInfoDTO = MealInfoDTO.builder()
                .mealId(MEAL_ID)
                .userId(otherUserId)
                .build();

        // Mocking
        given(groupMemberMapper.findByGroupIdAndUserId(GROUP_ID, USER_ID)).willReturn(Optional.of(leaderVO));
        given(mealService.getMealInfo(MEAL_ID)).willReturn(mealInfoDTO);
        given(mealFeedbackMapper.insert(any(MealFeedbackVO.class))).willReturn(1);
        given(groupMemberMapper.findUserNameByGroupMemberId(MEMBER_ID)).willReturn("리더");

        // When
        FeedbackResponse response = groupFeedbackService.createMealFeedback(GROUP_ID, request, USER_ID);

        // Then
        assertThat(response).isNotNull();
        verify(mealFeedbackMapper).insert(any(MealFeedbackVO.class));
    }

    @Test
    @DisplayName("식단 피드백 생성 실패 - 권한 없음 (남의 글 & 일반 멤버)")
    void createMealFeedback_Fail_PermissionDenied() {
        // Given
        Long otherUserId = 200L;
        FeedbackCreateRequest request = new FeedbackCreateRequest();
        ReflectionTestUtils.setField(request, "targetLogId", MEAL_ID);

        GroupMemberVO memberVO = GroupMemberVO.builder()
                .userId(USER_ID)
                .role(GroupRole.MEMBER.name()) // 일반 멤버
                .build();

        MealInfoDTO mealInfoDTO = MealInfoDTO.builder()
                .userId(otherUserId) // 남의 글
                .build();

        // Mocking
        given(groupMemberMapper.findByGroupIdAndUserId(GROUP_ID, USER_ID)).willReturn(Optional.of(memberVO));
        given(mealService.getMealInfo(MEAL_ID)).willReturn(mealInfoDTO);

        // When & Then
        assertThatThrownBy(() -> groupFeedbackService.createMealFeedback(GROUP_ID, request, USER_ID))
                .isInstanceOf(GroupPermissionDeniedException.class);
    }

    @Test
    @DisplayName("식단 피드백 생성 실패 - DB Insert 오류")
    void createMealFeedback_Fail_InsertError() {
        // Given
        FeedbackCreateRequest request = new FeedbackCreateRequest();
        ReflectionTestUtils.setField(request, "targetLogId", MEAL_ID);

        GroupMemberVO memberVO = GroupMemberVO.builder().userId(USER_ID).role(GroupRole.MEMBER.name()).build();
        MealInfoDTO mealInfoDTO = MealInfoDTO.builder().userId(USER_ID).build();

        given(groupMemberMapper.findByGroupIdAndUserId(any(), any())).willReturn(Optional.of(memberVO));
        given(mealService.getMealInfo(any())).willReturn(mealInfoDTO);
        given(mealFeedbackMapper.insert(any())).willReturn(0); // 0 반환 (실패)

        // When & Then
        assertThatThrownBy(() -> groupFeedbackService.createMealFeedback(GROUP_ID, request, USER_ID))
                .isInstanceOf(InsertFailedException.class);
    }

    @Test
    @DisplayName("식단 피드백 수정 성공 - 작성자 본인")
    void updateMealFeedback_Success() {
        // Given
        FeedbackUpdateRequest request = new FeedbackUpdateRequest();
        ReflectionTestUtils.setField(request, "content", "수정됨");

        GroupMemberVO memberVO = GroupMemberVO.builder().groupMemberId(MEMBER_ID).userId(USER_ID).build();

        MealFeedbackVO existingFeedback = MealFeedbackVO.builder()
                .mealFeedbackId(FEEDBACK_ID)
                .groupMemberId(MEMBER_ID) // 작성자 일치
                .content("원본")
                .createdAt(LocalDateTime.now())
                .build();

        // Mocking
        given(groupMemberMapper.findByGroupIdAndUserId(GROUP_ID, USER_ID)).willReturn(Optional.of(memberVO));
        given(mealFeedbackMapper.findById(FEEDBACK_ID)).willReturn(Optional.of(existingFeedback));
        given(mealFeedbackMapper.updateById(any(MealFeedbackVO.class))).willReturn(1); // 성공
        given(groupMemberMapper.findUserNameByGroupMemberId(MEMBER_ID)).willReturn("작성자");

        // When
        FeedbackResponse response = groupFeedbackService.updateMealFeedback(GROUP_ID, FEEDBACK_ID, request, USER_ID);

        // Then
        assertThat(response.getContent()).isEqualTo("수정됨");
        verify(mealFeedbackMapper).updateById(any(MealFeedbackVO.class));
    }

    @Test
    @DisplayName("식단 피드백 수정 실패 - 작성자가 아님")
    void updateMealFeedback_Fail_NotAuthor() {
        // Given
        Long otherMemberId = 999L;
        GroupMemberVO myMemberVO = GroupMemberVO.builder().groupMemberId(MEMBER_ID).build();

        MealFeedbackVO existingFeedback = MealFeedbackVO.builder()
                .groupMemberId(otherMemberId) // 다른 사람이 씀
                .build();

        given(groupMemberMapper.findByGroupIdAndUserId(GROUP_ID, USER_ID)).willReturn(Optional.of(myMemberVO));
        given(mealFeedbackMapper.findById(FEEDBACK_ID)).willReturn(Optional.of(existingFeedback));

        // When & Then
        assertThatThrownBy(() -> groupFeedbackService.updateMealFeedback(GROUP_ID, FEEDBACK_ID, new FeedbackUpdateRequest(), USER_ID))
                .isInstanceOf(GroupPermissionDeniedException.class);
    }

    @Test
    @DisplayName("식단 피드백 삭제 성공 - 작성자 본인")
    void deleteMealFeedback_Success() {
        // Given
        GroupMemberVO memberVO = GroupMemberVO.builder().groupMemberId(MEMBER_ID).build();
        MealFeedbackVO feedbackVO = MealFeedbackVO.builder().groupMemberId(MEMBER_ID).build();

        given(groupMemberMapper.findByGroupIdAndUserId(GROUP_ID, USER_ID)).willReturn(Optional.of(memberVO));
        given(mealFeedbackMapper.findById(FEEDBACK_ID)).willReturn(Optional.of(feedbackVO));
        given(mealFeedbackMapper.deleteById(FEEDBACK_ID)).willReturn(1);

        // When
        groupFeedbackService.deleteMealFeedback(GROUP_ID, FEEDBACK_ID, USER_ID);

        // Then
        verify(mealFeedbackMapper).deleteById(FEEDBACK_ID);
    }
}