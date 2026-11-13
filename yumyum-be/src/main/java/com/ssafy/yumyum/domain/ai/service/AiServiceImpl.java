package com.ssafy.yumyum.domain.ai.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.yumyum.domain.ai.dto.request.AiRequest;
import com.ssafy.yumyum.domain.ai.dto.response.AiMealResponse;
import com.ssafy.yumyum.domain.ai.dto.response.AiResponse;
import com.ssafy.yumyum.domain.meal.service.MealAiService;
import com.ssafy.yumyum.domain.research.service.dto.ResearchLogDTO;
import com.ssafy.yumyum.domain.user.service.UserService;
import com.ssafy.yumyum.domain.user.service.dto.UserInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService{

    private final WebClient openAiClient;
    private final ObjectMapper objectMapper;

    private final UserService userService;
    private final MealAiService mealAiService;

    @Value("${openai.model}")
    private String openAiModel;

    @Override
    @Async // 비동기 실행
    @Transactional
    public void createAiMealRecommendations(Long userId, ResearchLogDTO researchLog) {
        try {
            // 현재 로그인된 유저 정보 조회
            UserInfoDTO user = userService.getUserInfo(userId);

            // 시스템 프롬프트 생성
            String systemPrompt = getSystemPrompt();
            // 유저 정보 프롬프트 생성
            String userPrompt = buildUserPromptInEnglish(user, researchLog);

            // ✨ API 파라미터 최적화 적용
            AiRequest request = AiRequest.builder()
                    .model(openAiModel)
                    .messages(List.of(
                            new AiRequest.Message("system", systemPrompt),
                            new AiRequest.Message("user", userPrompt)
                    ))
                    .responseFormat(new AiRequest.ResponseFormat("json_object"))
                    .temperature(0.7)           // 완전 결정론적
                    .topP(0.9)                  // 상위 10% 토큰만 샘플링
                    .frequencyPenalty(0.3)      // 반복 억제 비활성화
                    .presencePenalty(0.0)       // 새 표현 억제
                    .seed(null)                // 재현성 보장 (실험적)
                    .build();

            // OpenAI API 답변
            AiResponse response = openAiClient.post()
                    .uri("/chat/completions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AiResponse.class)
                    .block(); // Async 내부이므로 block 가능

            // 반환받은 JSON 값
            String jsonResponse = response.getChoices().get(0).getMessage().getContent();

            log.info("GPT Raw Response: {}", jsonResponse);
            // 반환받은 JSON값을 DTO에 파싱
            AiMealResponse responseDto = objectMapper.readValue(jsonResponse, AiMealResponse.class);

//            log.info("Parsed DTO: {}", responseDto);

            // 전달받은 DTO를 이용해 식단 정보 저장
            mealAiService.saveAiMealList(userId, responseDto.getRecommendations());

            log.info("AI 식단 생성 완료 User: {}", userId);

        } catch (WebClientResponseException e) {
            log.error("OpenAI API 호출 에러 Code: {}", e.getStatusCode());
            log.error("OpenAI API 에러 본문: {}", e.getResponseBodyAsString());
            throw new RuntimeException("AI 호출 실패: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            throw new RuntimeException("AI 내부 에러");
        }
    }

    // 시스템 프롬프트 (v2: 다양성 및 규칙 우선순위 강화)
    private String getSystemPrompt() {
        return """
            You are a 'Personal Nutritionist AI'. Analyze the user's data and daily research log based on the [Analysis Matrix] to recommend the **optimal single meal plan**.
            
            Suggest 3 options [HARD, MEDIUM, EASY]. 
            Do not separate by breakfast/lunch/dinner; instead, provide **one complete meal combination per difficulty**.
            
            ### [CRITICAL DEFINITION OF 'DIFFICULTY'] (Must Follow)
            The 'Difficulty' means **"How strictly 'Clean' the diet is"**, NOT cooking difficulty.
            
            1. **HARD (Spartan Mode)**: 
               - **Concept:** Bodybuilder / Prep style. Highest nutritional density. Low Sodium.
               - **Allowed Foods:** ONLY whole foods (Steamed/Grilled/Raw). 
               - **BANNED:** Soups(Jjigae), Stews, Stir-frys with heavy sauce, Noodles, Bread.
               - **Naming Style:** Mostly **[Method]+[Ingredient]** (e.g., 구운닭가슴살, 고구마).
            
            2. **MEDIUM (Sustainable Mode)**: 
               - **Concept:** Healthy Home-cooked meal (K-Food style). Balanced tasty diet.
               - **Allowed Foods:** Light Stews(Doenjang-guk), Bibimbap, Grilled Fish, Tofu dishes.
               - **Naming Style:** Mix of Dish Names & Ingredient Names.
            
            3. **EASY (Comfort Mode)**: 
               - **Concept:** Tasty but macro-controlled. Psychological satisfaction priority.
               - **Allowed Foods:** Pasta, Rice Bowls(Donburi), Sandwiches, Risotto, Stir-frys.
               - **Naming Style:** Mostly **Dish Names** (e.g., 투움바파스타, 제육덮밥).
            
            ---
            
            ### [⚠️ FOOD NAMING CONVENTION - HYBRID MODE ⚠️]
            **You must follow standard Korean menu names found in DBs.**
            
            **Rule 1: Complete Dishes (Use for MEDIUM / EASY)**
            - Use the **Exact Dish Name** for soups, stews, noodles, rice bowls, sandwiches.
            - **Examples:** "된장찌개", "김치볶음밥", "크림파스타", "닭가슴살샐러드", "불고기샌드위치", "육개장"
            - **Do NOT use for HARD mode** unless it is a plain salad.
            
            **Rule 2: Single Ingredients (Use for HARD / MEDIUM)**
            - If it is a simple ingredient, use **[Method]+[Ingredient]** format.
            - **Examples:** "구운닭가슴살", "현미밥", "삶은계란", "찐고구마", "데친브로콜리"
            - **Examples (Bad):** "닭가슴살구이" (X)
            
            **Rule 3: General Formatting**
            - **REMOVE ALL SPACES**. (e.g., "크림 파스타" -> "크림파스타")
            
            ### [Analysis Matrix]
            1. Diet Compliance
               - LOW: Include Recovery foods (Fruits, Veggies).
               - NORMAL: Standard balanced.
               - HIGH: Strict calorie deficit (Volume foods).
            
            2. Digestion State
               - BLOATED: **STRICT RESTRICTION.** Use Porridge, Tofu, Steamed Egg. (Override Difficulty rules if necessary).
            
            3. Activity Level (Tomorrow)
               - HIGH: Add complex carbs (Rice, Bread, Pasta) to MEDIUM/EASY.
            
            ---
            
            ### [Output Requirements]
            1. Response MUST be in **JSON format only**.
            2. **recommendations**: List of 3 difficulty levels.
            3. **items**: List of food items.
            4. **foodName**: Use Korean names. No spaces.
            5. **amount**: Integer (g).
            
            ### [JSON Structure Example]
            {
              "aiComment": "식단 관리 강도에 따른 3가지 옵션입니다.",
              "recommendations": [
                {
                  "difficulty": "HARD",
                  "reason": "양념과 나트륨을 최소화한 클린 식단 (체지방 감량 최적화)",
                  "items": [
                     { "foodName": "현미밥", "amount": 150 },
                     { "foodName": "구운닭가슴살", "amount": 100 },
                     { "foodName": "데친브로콜리", "amount": 50 }
                  ]
                },
                {
                  "difficulty": "MEDIUM",
                  "reason": "한식 위주의 든든하고 건강한 식사",
                  "items": [
                     { "foodName": "된장찌개", "amount": 250 },
                     { "foodName": "고등어구이", "amount": 100 },
                     { "foodName": "잡곡밥", "amount": 150 }
                  ]
                },
                {
                  "difficulty": "EASY",
                  "reason": "맛있게 즐기는 다이어트 특식",
                  "items": [
                     { "foodName": "토마토오븐스파게티", "amount": 300 },
                     { "foodName": "오이피클", "amount": 30 }
                  ]
                }
              ]
            }
        """;
    }

    // 유저 정보를 이용한 프롬프트 ( 유저 DB, 데일리 리서치 DB)
    private String buildUserPromptInEnglish(UserInfoDTO user, ResearchLogDTO research) {
        // 나이 계산
        int age = 0;
        if (user.getBirthDate() != null) {
            age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        }

        return String.format("""
            ### [User Profile]
            - Gender: %s
            - Age: %d
            - Height/Weight: %.1fcm / %.1fkg
            - Goal: %s
            
            ### [Daily Research]
            1. Diet Compliance: %s
            2. Workout Intensity: %s
            3. Digestion State: %s
            4. Tomorrow Activity: %s
            5. Tomorrow Cheat Meal: %s
            
            ⚠️ REMINDER:\s
            1. Use [Method]+[Ingredient] for simple foods (e.g., '구운닭가슴살').
            2. Use Dish Names for complete meals (e.g., '된장찌개').\s
            3. NO SPACES.
            4. **BE CREATIVE! Do not output the same menu as the example.**                                   
            """,
                user.getGender(),
                age,
                user.getHeight(), user.getCurrentWeight(),
                user.getGoalType(), // Enum String

                research.getDietPerformance(),
                research.getExerciseIntensity(),
                research.getDigestionState(),
                research.getTomorrowActivity(),
                research.getFreeMeal()
        );
    }
}
