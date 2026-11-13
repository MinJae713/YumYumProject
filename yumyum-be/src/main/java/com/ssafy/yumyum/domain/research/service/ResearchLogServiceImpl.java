package com.ssafy.yumyum.domain.research.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.domain.ai.service.AiService;
import com.ssafy.yumyum.domain.research.dto.request.ResearchLogRequest;
import com.ssafy.yumyum.domain.research.exception.ResearchLogNotFoundException;
import com.ssafy.yumyum.domain.research.repository.ResearchLogMapper;
import com.ssafy.yumyum.domain.research.repository.ResearchLogVO;
import com.ssafy.yumyum.domain.research.service.dto.ResearchLogDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResearchLogServiceImpl implements ResearchLogService {

    private final ResearchLogMapper researchLogMapper;

    private final AiService aiService;

    @Override
    @Transactional
    public void submitResearchLog(ResearchLogRequest researchLogRequest, Long userId) {

        ResearchLogVO researchLogVO = ResearchLogVO.builder()
                .userId(userId)
                .researchDate(LocalDate.now())
                .dietPerformance(researchLogRequest.getDietPerformance())
                .exerciseIntensity(researchLogRequest.getExerciseIntensity())
                .digestionState(researchLogRequest.getDigestionState())
                .tomorrowActivity(researchLogRequest.getTomorrowActivity())
                .freeMeal(researchLogRequest.getFreeMeal())
                .build();
        if(researchLogMapper.insert(researchLogVO) != 1){
            throw new InsertFailedException("데일리 리서치 저장에 실패했습니다.");
        }

        // AI 식단 생성 호출 위치
        ResearchLogDTO researchLogDTO = convertResearchVoToResearchLogDTO(researchLogVO);
        aiService.createAiMealRecommendations(userId, researchLogDTO);
    }

    /**
     *  userId와 지정된 날짜로 리서리 로그 반환
     */
    @Override
    @Transactional
    public ResearchLogDTO getResearchLogByDateAndUserId(LocalDate localDate, Long userId) {
        ResearchLogVO researchLogVO = researchLogMapper.findByUserIdAndResearchDate(userId, localDate)
                .orElseThrow(ResearchLogNotFoundException::new);
        return convertResearchVoToResearchLogDTO(researchLogVO);
    }

    /**
     *  userId를 이용해 최근 7일동안 저장된 리서치 로그 반환
     */
    @Override
    @Transactional
    public List<ResearchLogDTO> getRecentResearchLogListByUserId(Long userId) {
        List<ResearchLogVO> researchLogVOList = researchLogMapper.findTop7ByUserIdOrderByResearchDateDesc(userId);

        return researchLogVOList.stream()
                .map(this::convertResearchVoToResearchLogDTO)
                .collect(Collectors.toList());
    }

    private ResearchLogDTO convertResearchVoToResearchLogDTO(ResearchLogVO researchLogVO) {
        return ResearchLogDTO.builder()
                .researchId(researchLogVO.getResearchId())
                .userId(researchLogVO.getUserId())
                .researchDate(researchLogVO.getResearchDate())
                .dietPerformance(researchLogVO.getDietPerformance())
                .exerciseIntensity(researchLogVO.getExerciseIntensity())
                .digestionState(researchLogVO.getDigestionState())
                .tomorrowActivity(researchLogVO.getTomorrowActivity())
                .freeMeal(researchLogVO.getFreeMeal())
                .createdAt(researchLogVO.getCreatedAt())
                .updatedAt(researchLogVO.getUpdatedAt())
                .build();
    }

	@Override
	public Boolean getIsResearchComplete(LocalDate localDate, Long userId) {
        return researchLogMapper.findByUserIdAndResearchDate(userId, localDate).isPresent();
	}

}
