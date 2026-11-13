<script setup>
import { onMounted } from "vue";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import RecommendedMealsCarousel from "@/components/main/RecommendedMealsCarousel.vue";
import TodayMeals from "@/components/main/TodayMeals.vue";
import TodayExercise from "@/components/main/TodayExercise.vue";
import DailyResearchModal from "@/components/main/DailyResearchModal.vue";
import DailyTargetAchieved from "@/components/main/DailyTargetAchieved.vue";
import {
  Calendar,
  ClipboardCheck,
  Dumbbell,
  Utensils,
} from "lucide-vue-next";
import useMain from "@/stores/main/mainView";
import useDailyResearch from "@/stores/main/DailyResearch";
import { storeToRefs } from "pinia";

// ✅ Tooltip
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

const store = useMain();
const {
  streakRecord,
  isResearchComplete,
  dailyGoals,
  recommendedMeals,
} = storeToRefs(store);

const dailyResearchStore = useDailyResearch();

const handleOpenResearch = () => {
  if (isResearchComplete.value) return;
  dailyResearchStore.openModal();
};

onMounted(() => {
  store.setMainData();
});
</script>

<template>
  <div class="max-w-7xl mx-auto space-y-6 pb-24">
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
      <!-- 🔹 추천 식단 카드 영역 -->
      <div
        class="lg:col-span-8 bg-white rounded-3xl p-6 border border-gray-200 shadow-sm
               relative overflow-hidden min-h-[400px] h-full flex flex-col"
      >
        <div class="flex-1 min-h-0">
          <RecommendedMealsCarousel
            v-if="recommendedMeals && recommendedMeals.length > 0"
            class="h-full"
          />
          <p v-else class="text-sm text-gray-500">
            추천 식단을 준비 중입니다.
          </p>
        </div>
      </div>

      <!-- 🔹 오른쪽 사이드 영역 -->
      <div class="lg:col-span-4 flex flex-col gap-6">
        <!-- Streak -->
        <div class="bg-white rounded-3xl p-6 border border-gray-200 shadow-sm flex-1">
          <div class="flex items-center gap-2 mb-4">
            <Calendar class="w-5 h-5 text-emerald-500" />
            <h3 class="text-lg font-semibold text-gray-900">Streak</h3>
          </div>

          <div
            v-if="streakRecord && (streakRecord.EXERCISE_RECORD || streakRecord.MEAL_RECORD)"
            class="space-y-3"
          >
            <div
              v-if="streakRecord.EXERCISE_RECORD"
              class="flex items-center justify-between bg-emerald-50 border border-emerald-100 rounded-2xl px-4 py-3"
            >
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-emerald-100 flex items-center justify-center">
                  <Dumbbell class="w-5 h-5 text-emerald-600" />
                </div>
                <div>
                  <p class="text-sm font-semibold">
                    {{ streakRecord.EXERCISE_RECORD.name }}
                  </p>
                  <p class="text-xs text-gray-600">연속 운동</p>
                </div>
              </div>
              <Badge variant="secondary">
                {{ streakRecord.EXERCISE_RECORD.longestStreak }}일
              </Badge>
            </div>

            <div
              v-if="streakRecord.MEAL_RECORD"
              class="flex items-center justify-between bg-orange-50 border border-orange-100 rounded-2xl px-4 py-3"
            >
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-orange-100 flex items-center justify-center">
                  <Utensils class="w-5 h-5 text-orange-600" />
                </div>
                <div>
                  <p class="text-sm font-semibold">
                    {{ streakRecord.MEAL_RECORD.name }}
                  </p>
                  <p class="text-xs text-gray-600">연속 식단</p>
                </div>
              </div>
              <Badge variant="secondary">
                {{ streakRecord.MEAL_RECORD.longestStreak }}일
              </Badge>
            </div>
          </div>

          <div
            v-else
            class="bg-gray-50 border border-dashed border-gray-200 rounded-2xl px-4 py-6 text-center text-gray-500"
          >
            기록을 시작해보세요
          </div>
        </div>

        <!-- 목표 달성 -->
        <div class="bg-white rounded-3xl p-6 border border-gray-200 shadow-sm flex-1">
          <DailyTargetAchieved v-if="dailyGoals" />
        </div>
      </div>
    </div>

    <!-- 하단 영역 -->
    <div class="space-y-6">
      <div class="bg-white rounded-3xl p-6 border border-gray-200 shadow-sm">
        <TodayMeals />
      </div>

      <div class="bg-white rounded-3xl p-6 border border-gray-200 shadow-sm">
        <TodayExercise />
      </div>
    </div>

    <!-- ✅ 플로팅 버튼 + Tooltip -->
    <TooltipProvider>
      <Tooltip :disabled="!isResearchComplete">
        <TooltipTrigger as-child>
          <!-- disabled button을 위해 wrapper div -->
          <div class="fixed bottom-8 right-8 z-50">
            <Button
              size="lg"
              :aria-disabled="isResearchComplete"
              :disabled="isResearchComplete"
              class="w-16 h-16 rounded-full shadow-lg
                    flex items-center justify-center transition-transform"
              :class="[
                isResearchComplete
                  ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                  : 'bg-emerald-500 text-white hover:scale-105'
              ]"
              @click="handleOpenResearch"
            >
              <ClipboardCheck class="w-6 h-6" />
            </Button>
          </div>
        </TooltipTrigger>

        <TooltipContent side="left" align="center">
          <p class="text-xs">오늘 리서치는 이미 완료되었습니다</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>

    <DailyResearchModal />
  </div>
</template>
