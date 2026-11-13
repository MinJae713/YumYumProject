package com.ssafy.yumyum.domain.research.service;

import com.ssafy.yumyum.domain.research.exception.ResearchLogNotFoundException;
import com.ssafy.yumyum.domain.research.repository.ResearchLogMapper;
import com.ssafy.yumyum.domain.research.repository.ResearchLogVO;
import com.ssafy.yumyum.domain.research.service.dto.ResearchLogDTO;
import com.ssafy.yumyum.domain.research.types.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Mockito 사용 설정
class ResearchLogServiceImplTest {
    @Mock
    private ResearchLogMapper researchLogMapper;

    @InjectMocks
    private ResearchLogServiceImpl researchLogService;

    @Test
    @DisplayName("날짜와 유저ID로 리서치 로그 조회 - 성공")
    void getResearchLogByDateAndUserId_Success() {
        // given
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        ResearchLogVO mockVO = ResearchLogVO.builder()
                .researchId(100L)
                .userId(userId)
                .researchDate(date)
                .dietPerformance(DietPerformance.HIGH)       // Enum 사용
                .exerciseIntensity(ExerciseIntensity.NORMAL) // Enum 사용
                .digestionState(DigestionState.COMFORTABLE)  // Enum 사용
                .tomorrowActivity(TomorrowActivity.NORMAL)   // Enum 사용
                .freeMeal(FreeMeal.NONE)                     // Enum 사용
                .build();

        given(researchLogMapper.findByUserIdAndResearchDate(userId, date))
                .willReturn(Optional.of(mockVO));

        // when
        ResearchLogDTO result = researchLogService.getResearchLogByDateAndUserId(date, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getResearchDate()).isEqualTo(date);

        // [중요] DTO가 Enum을 반환한다면 여기서도 Enum과 비교해야 합니다.
        // 만약 DTO 필드가 String이라면 "HIGH"로, Enum이라면 DietPerformance.HIGH로 비교하세요.
        // 여기서는 안전하게 Enum으로 가정하고 작성했습니다.
        assertThat(result.getDietPerformance()).isEqualTo(DietPerformance.HIGH);
    }

    @Test
    @DisplayName("날짜와 유저ID로 리서치 로그 조회 - 실패(데이터 없음 예외)")
    void getResearchLogByDateAndUserId_NotFound() {
        // given
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        given(researchLogMapper.findByUserIdAndResearchDate(userId, date))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(ResearchLogNotFoundException.class, () -> {
            researchLogService.getResearchLogByDateAndUserId(date, userId);
        });
    }

    @Test
    @DisplayName("최근 7일 리서치 로그 조회 - 성공 (리스트 변환 확인)")
    void getRecentResearchLogListByUserId() {
        // given
        Long userId = 1L;

        // [수정 포인트] 빌더에 문자열 대신 Enum 상수(DietPerformance.LOW)를 넣어야 합니다.
        ResearchLogVO vo1 = ResearchLogVO.builder()
                .researchId(1L)
                .userId(userId)
                .dietPerformance(DietPerformance.LOW) // "LOW" (X) -> Enum (O)
                .build();

        ResearchLogVO vo2 = ResearchLogVO.builder()
                .researchId(2L)
                .userId(userId)
                .dietPerformance(DietPerformance.HIGH) // "HIGH" (X) -> Enum (O)
                .build();

        List<ResearchLogVO> mockList = List.of(vo1, vo2);

        given(researchLogMapper.findTop7ByUserIdOrderByResearchDateDesc(userId))
                .willReturn(mockList);

        // when
        List<ResearchLogDTO> resultList = researchLogService.getRecentResearchLogListByUserId(userId);

        // then
        assertThat(resultList).hasSize(2);

        // [수정 포인트] 결과값 검증도 Enum 타입으로 비교
        assertThat(resultList.get(0).getDietPerformance()).isEqualTo(DietPerformance.LOW);
        assertThat(resultList.get(1).getDietPerformance()).isEqualTo(DietPerformance.HIGH);
    }
}