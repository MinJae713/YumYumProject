package com.ssafy.yumyum.domain.meal.service;

import java.util.Map;

public interface AiMealAdminService {
    Map<String, Object> getAllAiMealList(int page, int size);
}
