<script setup>
import { BarChart3 } from "lucide-vue-next";
import BadgeSection from "@/components/dashboard/BadgeSection.vue";
import WeightChart from "@/components/dashboard/WeightChart.vue";
import NutritionDonutChart from "@/components/dashboard/NutritionDonutChart.vue";
import NutritionRadarChart from "@/components/dashboard/NutritionRadarChart.vue";
import { computed, onMounted } from "vue";
import useDashboard from "@/stores/dashboard/DashboardView";
import { storeToRefs } from "pinia";
import { safeNumber } from "@/utils/numberUtils";

const store = useDashboard();
const {
  spendedCalorie,
  totalDuration,
  streakRecords
} = storeToRefs(store);

const displaySpendedAverageCalorie = computed(() =>
  safeNumber(spendedCalorie.value?.spendedAverageCalorie, null),
);
const displaySpendedPercent = computed(() =>
  safeNumber(spendedCalorie.value?.percentFromLastWeek, null),
);

const displayExerciseTime = computed(() =>
  safeNumber(totalDuration.value?.exerciseTimeSameWeek, null),
);
const displayExercisePercent = computed(() =>
  safeNumber(totalDuration.value?.percentFromLastWeek, null),
);

const displayStreakRecords = computed(() =>
  (streakRecords.value || []).map((record) => ({
    ...record,
    longestStreak: safeNumber(record?.longestStreak, null),
  })),
);

onMounted(() => {
  // 이 시점에 axios 호출
  // 내부 컴포넌트 모두 마운트 이후 뷰가 마운트 됨
  store.setDashboardData();
})

</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-6 space-y-6">
    <div class="flex items-center gap-3">
      <div class="w-12 h-12 bg-emerald-50 rounded-2xl flex items-center justify-center">
        <BarChart3 class="w-6 h-6 text-emerald-600" />
      </div>
      <div>
        <h1 class="text-2xl font-semibold text-gray-900">데이터 대시보드</h1>
        <p class="text-sm text-gray-500">
          건강 데이터를 한눈에 확인하고 분석하세요
        </p>
      </div>
    </div>

    <BadgeSection />

    <div class="grid lg:grid-cols-2 gap-6">
      <div class="lg:col-span-2">
        <WeightChart />
      </div>

      <NutritionDonutChart />

      <NutritionRadarChart />
    </div>
    <div class="grid md:grid-cols-4 gap-4">
      <div v-if="displaySpendedAverageCalorie !== null" class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6">
        <div class="text-xs text-gray-500 font-medium mb-2">주간 하루 평균 소비 칼로리</div>
        <div class="text-2xl font-bold text-gray-900 mb-1">
          {{ displaySpendedAverageCalorie }}
          <span class="text-base font-semibold">kcal</span>
        </div>
        <div class="text-xs font-medium text-emerald-600">
          <span v-if="displaySpendedPercent !== null">목표 대비 {{ displaySpendedPercent }}%</span>
          <span v-else>데이터 없음</span>
        </div>
      </div>
      <div v-if="displayExerciseTime !== null" class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6">
        <div class="text-xs text-gray-500 font-medium mb-2">주간 운동 시간</div>
        <div class="text-2xl font-bold text-gray-900 mb-1">
          {{ displayExerciseTime }}
          <span class="text-base font-semibold">분</span>
        </div>
        <div class="text-xs font-medium text-blue-600">
          <span v-if="displayExercisePercent !== null">전주 대비 {{ displayExercisePercent }}%</span>
          <span v-else>데이터 없음</span>
        </div>
      </div>
      <div
        v-for="record in displayStreakRecords"
        :key="record.name"
        class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6"
      >
        <div class="text-xs text-gray-500 font-medium mb-2">{{ record.name }} 연속 기록 일수</div>
        <div class="text-2xl font-bold text-gray-900 mb-1">
          <template v-if="record.longestStreak !== null">
            {{ record.longestStreak }}<span class="text-base font-semibold">일</span>
          </template>
          <template v-else>-</template>
        </div>
        <div class="text-xs font-medium text-orange-600">최고 기록 갱신(뺄 수도 있음)</div>
      </div>
    </div>
  </div>
</template>
