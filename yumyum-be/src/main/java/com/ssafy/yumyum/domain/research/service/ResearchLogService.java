package com.ssafy.yumyum.domain.research.service;

import com.ssafy.yumyum.domain.research.dto.request.ResearchLogRequest;
import com.ssafy.yumyum.domain.research.service.dto.ResearchLogDTO;

import java.time.LocalDate;
import java.util.List;

public interface ResearchLogService {

    void submitResearchLog(ResearchLogRequest researchLogRequest, Long userId);

    ResearchLogDTO getResearchLogByDateAndUserId(LocalDate localDate, Long userId);

    List<ResearchLogDTO> getRecentResearchLogListByUserId(Long userId);

    Boolean getIsResearchComplete(LocalDate now, Long userId);
}
