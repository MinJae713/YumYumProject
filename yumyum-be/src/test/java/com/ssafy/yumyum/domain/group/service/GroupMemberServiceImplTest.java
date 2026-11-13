package com.ssafy.yumyum.domain.group.service;

import com.ssafy.yumyum.domain.group.repository.groupInvite.GroupInviteMapper;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GroupMemberServiceImplTest {
    @InjectMocks
    private GroupMemberServiceImpl groupMemberService; // 테스트 대상

    @Mock
    private GroupMemberMapper groupMemberMapper; // 가짜 객체 (Mock)

    // GroupMemberServiceImpl이 GroupInviteMapper도 의존하고 있다면 같이 Mock 처리 필요
    // (사용하지 않더라도 주입을 위해 선언)
    @Mock
    private GroupInviteMapper groupInviteMapper;

    @Test
    @DisplayName("최고 랭킹 조회 - 성공 (그룹 활동 중일 때)")
    void getBestRanking_Success() {
        // given
        Long userId = 1L;
        Integer expectedRank = 1; // 1등이라고 가정

        // 매퍼가 1을 리턴하도록 설정
        given(groupMemberMapper.findBestRankingByUserId(userId)).willReturn(expectedRank);

        // when
        Integer result = groupMemberService.getBestRanking(userId);

        // then
        assertThat(result).isEqualTo(expectedRank); // 결과가 1이어야 함
        verify(groupMemberMapper).findBestRankingByUserId(userId); // 매퍼가 호출되었는지 검증
    }

    @Test
    @DisplayName("최고 랭킹 조회 - 결과 없음 (가입한 그룹이 없거나 비활성 상태일 때)")
    void getBestRanking_NoGroup() {
        // given
        Long userId = 99L;

        // 매퍼가 null을 리턴하도록 설정 (그룹 없음)
        given(groupMemberMapper.findBestRankingByUserId(userId)).willReturn(null);

        // when
        Integer result = groupMemberService.getBestRanking(userId);

        // then
        assertThat(result).isNull(); // 결과가 null이어야 함
        verify(groupMemberMapper).findBestRankingByUserId(userId);
    }
}