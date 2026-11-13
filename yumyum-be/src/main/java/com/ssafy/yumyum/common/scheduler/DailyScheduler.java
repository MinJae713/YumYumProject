package com.ssafy.yumyum.common.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssafy.yumyum.domain.ai.service.AiService;
import com.ssafy.yumyum.domain.research.service.dto.ResearchLogDTO;
import com.ssafy.yumyum.domain.research.types.DietPerformance;
import com.ssafy.yumyum.domain.research.types.DigestionState;
import com.ssafy.yumyum.domain.research.types.ExerciseIntensity;
import com.ssafy.yumyum.domain.research.types.FreeMeal;
import com.ssafy.yumyum.domain.research.types.TomorrowActivity;
import com.ssafy.yumyum.domain.user.service.UserService;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyScheduler {

    private final UserService userService;
    private final AiService aiService;

    @Scheduled(cron = "0 0 22 * * *")   // 초 분 시 월 요일
//    @Scheduled(cron = "0/10 * * * * *")
    public void runAutoAiRecommendation(){
        log.info("[Scheduler] 데일리 리서치 미작성자 자동 추천 시작");

        // 1. 오늘 리서치 안 쓴 유저 조회
        List<UserInfoDTO> targetUsers = userService.findUsersWhoDidNotSubmitResearch();
        log.info("대상 유저 수: {}명", targetUsers.size());

        for (UserInfoDTO user : targetUsers) {
            try {
                // 2. "기본값(Default)" 리서치 로그 생성
                // 유저가 입력을 안 했으니, 가장 표준적인 상태라고 가정하고 만듭니다.
                ResearchLogDTO defaultLog = createDefaultResearchLog();

                // 3. AI 추천 생성 요청 (비동기라 금방 넘어감)
                aiService.createAiMealRecommendations(user.getUserId(), defaultLog);

                log.info("User ID: {} - 자동 추천 요청 완료", user.getUserId());

            } catch (Exception e) {
                // 한 명 실패해도 나머지는 계속 돌아야 함
                log.error("User ID: {} - 자동 추천 실패", user.getUserId(), e);
            }
        }

        log.info("[Scheduler] 작업 완료");
    }

    // ★ 기본값 DTO 생성 메서드
    private ResearchLogDTO createDefaultResearchLog() {
        return ResearchLogDTO.builder()
                .researchDate(LocalDate.now())

                // [가정] 식단 잘 지켰음 (보통)
                .dietPerformance(DietPerformance.NORMAL)

                // [가정] 운동은 안 함 (안 적었으니 모름 -> 안전하게 안 함으로 처리)
                .exerciseIntensity(ExerciseIntensity.NONE)

                // [가정] 소화 상태 편안함
                .digestionState(DigestionState.COMFORTABLE)

                // [가정] 내일 활동량 보통
                .tomorrowActivity(TomorrowActivity.NORMAL)

                // [가정] 치팅밀 없음
                .freeMeal(FreeMeal.NONE)

                .build();
    }

    // [추가] 서버가 정상적으로 켜지면(Ready 상태) 바로 이 메서드를 실행합니다.
//    @EventListener(ApplicationReadyEvent.class)
//    public void testRunOnStartup() {
//        log.info("[Test] 앱 시작 직후 스케줄러 테스트 실행");
//        runAutoAiRecommendation();
//    }
}
