package com.ssafy.yumyum.domain.ai.service;

import com.ssafy.yumyum.domain.research.service.dto.ResearchLogDTO;

public interface AiService {
    void createAiMealRecommendations(Long userId, ResearchLogDTO researchLogDTO);
}
