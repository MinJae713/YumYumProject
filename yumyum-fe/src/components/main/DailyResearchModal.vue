<script setup>
import { storeToRefs } from "pinia";
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import useDailyResearch from "@/stores/main/DailyResearch";
import ResearchSlider from "@/components/main/ResearchSlider.vue";

const dailyResearchStore = useDailyResearch();
const {
  isModalOpen,
  dietPerformance,
  exerciseIntensity,
  digestionState,
  tomorrowActivity,
  freeMeal,
} = storeToRefs(dailyResearchStore);
const {
  submitResearch,
  closeModal,
  dietPerformanceOptions,
  exerciseIntensityOptions,
  digestionStateOptions,
  tomorrowActivityOptions,
  freeMealOptions,
} = dailyResearchStore;
</script>

<template>
  <Dialog v-model:open="isModalOpen">
    <DialogContent class="sm:max-w-[520px] bg-white border border-gray-200 rounded-3xl shadow-lg">
      <DialogHeader class="space-y-1">
        <p class="text-xs uppercase tracking-[0.2em] text-muted-foreground">
          Daily research
        </p>
        <DialogTitle class="text-xl text-gray-900">오늘의 상태를 알려주세요</DialogTitle>
        <p class="text-sm text-gray-600">
          식단 추천과 분석에 사용돼요. 각 문항을 빠르게 선택해 주세요.
        </p>
      </DialogHeader>

      <div class="space-y-8 py-2">
        <div class="space-y-3">
          <h4 class="text-base font-bold text-gray-800">오늘 식단 수행률은 어땠나요?</h4>
          <ResearchSlider v-model="dietPerformance" :options="dietPerformanceOptions" />
        </div>

        <div class="space-y-3">
          <h4 class="text-base font-bold text-gray-800">오늘 운동 강도는 어느 정도였나요?</h4>
          <ResearchSlider v-model="exerciseIntensity" :options="exerciseIntensityOptions" />
        </div>

        <div class="space-y-3">
          <h4 class="text-base font-bold text-gray-800">현재 소화 상태를 선택해주세요.</h4>
          <ResearchSlider v-model="digestionState" :options="digestionStateOptions" />
        </div>

        <div class="space-y-3">
          <h4 class="text-base font-bold text-gray-800">내일 활동량은 어떨까요?</h4>
          <ResearchSlider v-model="tomorrowActivity" :options="tomorrowActivityOptions" />
        </div>

        <div class="space-y-3">
          <h4 class="text-base font-bold text-gray-800">내일 외식 계획이 있나요?</h4>
          <div class="grid grid-cols-2 gap-2 sm:grid-cols-4">
            <button
              v-for="option in freeMealOptions"
              :key="option.value"
              class="p-3 rounded-lg border text-sm transition-all"
              :class="freeMeal === option.value
                ? 'bg-emerald-50 border-emerald-500 text-emerald-700 font-bold shadow-sm'
                : 'bg-white border-gray-200 text-gray-500 hover:bg-gray-50'"
              @click="freeMeal = option.value"
            >
              {{ option.label }}
            </button>
          </div>
        </div>
      </div>

      <DialogFooter class="gap-2">
        <Button variant="outline" class="w-full sm:w-auto" @click="closeModal">
          나중에
        </Button>
        <Button class="w-full sm:w-auto bg-emerald-500 hover:bg-emerald-600 text-white" @click="submitResearch">
          제출하기
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
