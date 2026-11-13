<script setup>
import { Doughnut } from "vue-chartjs";
import {
  ArcElement,
  Chart as ChartJS,
  Legend,
  Tooltip,
} from "chart.js";
import { Card } from "@/components/ui/card";
import { Apple } from "lucide-vue-next";
import useNutritionDonutChart from "@/stores/dashboard/NutritionDonutChart";
import { storeToRefs } from "pinia";
const store = useNutritionDonutChart();

ChartJS.register(ArcElement, Tooltip, Legend);

const {
  isEmptyRecord,
  nutritionData,
  totalCalories,
  chartData,
  chartOptions
} = storeToRefs(store);

</script>

<template>
  <Card class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6">
    <div class="flex items-center gap-3 mb-6">
      <div class="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
        <Apple class="w-5 h-5 text-emerald-600" />
      </div>
      <div>
        <h3 class="text-lg font-bold text-gray-900">영양소 섭취 비율</h3>
        <p class="text-xs text-gray-500">탄수화물 · 단백질 · 지방</p>
      </div>
    </div>
    <div v-if="isEmptyRecord" class="flex flex-col items-center">
      <div class="inset-0 flex flex-col items-center justify-center pointer-events-none">
        <span class="text-xs text-gray-400">기록이 비어있습니다!</span>
      </div>
    </div>
    <div v-else class="flex flex-col items-center">
      <div class="relative h-[280px] w-full flex items-center justify-center">
        <Doughnut :data="chartData" :options="chartOptions" />
        <div class="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
          <span class="text-xs text-gray-500">Total Kcal</span>
          <span class="text-xl font-bold text-gray-900">{{ totalCalories }} kcal</span>
        </div>
      </div>

      <div class="w-full space-y-3 mt-4">
        <div
          v-for="item in nutritionData"
          :key="item.name"
          class="flex items-center justify-between p-3 bg-gray-50 border border-gray-100 rounded-2xl"
        >
          <div class="flex items-center gap-3">
            <div
              class="w-4 h-4 rounded-full"
              :style="{ backgroundColor: item.color }"
            />
            <span class="text-sm font-medium text-gray-900">{{ item.name }}</span>
          </div>
          <div class="flex items-center gap-3">
            <span class="text-sm text-gray-500">{{ item.value }}%</span>
            <span class="text-sm text-gray-500">
              {{ item.intakeCalorie }} kcal
            </span>
          </div>
        </div>
      </div>

      <div class="w-full mt-4 p-4 bg-emerald-50 rounded-2xl text-center border border-emerald-100">
        <div class="text-xs text-gray-500 mb-1">총 섭취 칼로리</div>
        <div class="text-2xl font-bold text-emerald-600">{{ totalCalories }} kcal</div>
      </div>
    </div>
  </Card>
</template>
