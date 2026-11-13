<script setup>
import { computed } from "vue";
import { Radar } from "vue-chartjs";
import {
  Chart as ChartJS,
  Legend,
  LineElement,
  PointElement,
  RadialLinearScale,
  Filler,
  Tooltip,
} from "chart.js";
import { Card } from "@/components/ui/card";
import { Activity } from "lucide-vue-next";
import useNutritionRadarChart from "@/stores/dashboard/NutritionRadarChart";
import { storeToRefs } from "pinia";
import { safeNumber } from "@/utils/numberUtils";

const store = useNutritionRadarChart();

ChartJS.register(RadialLinearScale, PointElement, LineElement, Filler, Tooltip, Legend);

const {
  isEmptyRecord,
  nutritionComparisonData,
  averageAchievement,
  chartData,
  chartOptions,
} = storeToRefs(store);

const normalizedComparisonData = computed(() =>
  (nutritionComparisonData.value || []).map((item) => ({
    ...item,
    actual: safeNumber(item?.actual, null),
    recommended: safeNumber(item?.recommended, null),
  })),
);

const hasValidData = computed(() =>
  normalizedComparisonData.value.some(
    (item) => typeof item.actual === "number" && item.actual !== null && item.actual > 0,
  ),
);

const safeAverageAchievement = computed(() => safeNumber(averageAchievement.value, null));

const normalizedChartData = computed(() => ({
  labels: normalizedComparisonData.value.map((item) => item.subject),
  datasets: [
    {
      ...chartData.value.datasets[0],
      data: normalizedComparisonData.value.map((item) => safeNumber(item.recommended, 0)),
    },
    {
      ...chartData.value.datasets[1],
      data: normalizedComparisonData.value.map((item) => safeNumber(item.actual, 0)),
    },
  ],
}));

const showEmptyState = computed(() => !hasValidData.value);

</script>

<template>
  <Card class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6">
    <div class="flex items-center justify-between mb-6">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
          <Activity class="w-5 h-5 text-emerald-600" />
        </div>
        <div>
          <h3 class="text-lg font-bold text-gray-900">권장량 대비 섭취량</h3>
          <p class="text-xs text-gray-500">영양소별 목표 달성률</p>
        </div>
      </div>
      <div class="text-right">
        <div class="text-2xl font-bold text-emerald-600">
          <template v-if="safeAverageAchievement !== null">
            {{ safeAverageAchievement }}%
          </template>
          <template v-else>
            -
          </template>
        </div>
        <div class="text-xs text-gray-500">평균 달성률</div>
      </div>
    </div>

    <div v-if="showEmptyState" class="inset-0 flex flex-col items-center justify-center text-sm text-gray-500">
      <div class="inset-0 flex flex-col items-center justify-center pointer-events-none">
        <span class="text-xs text-gray-400">기록이 비어있습니다!</span>
      </div>
    </div>
    <div v-else>
      <div class="h-[300px]">
        <Radar :data="normalizedChartData" :options="chartOptions" />
      </div>

      <div class="grid grid-cols-4 gap-3 mt-6">
        <div
          v-for="item in normalizedComparisonData.slice(0, 4)"
          :key="item.subject"
          class="bg-gray-50 border border-gray-100 rounded-2xl p-3 text-center"
        >
          <div class="text-xs text-gray-500 mb-1">
            {{ item.subject }}
          </div>
          <div
            class="text-lg font-bold"
            :class="item.actual >= 90 ? 'text-emerald-600' : item.actual >= 70 ? 'text-blue-600' : 'text-orange-500'"
          >
            <template v-if="item.actual !== null">
              {{ item.actual }}%
            </template>
            <template v-else>-</template>
          </div>
        </div>
      </div>
    </div>
  </Card>
</template>
