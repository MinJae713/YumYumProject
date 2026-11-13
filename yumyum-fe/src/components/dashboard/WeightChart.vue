<script setup>
import { computed } from "vue";
import { Line } from "vue-chartjs";
import {
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  Tooltip,
} from "chart.js";
import { Card } from "@/components/ui/card";
import { Plus, TrendingDown } from "lucide-vue-next";
import useWeightChart from "@/stores/dashboard/WeightChart";
import { storeToRefs } from "pinia";
import { Button } from "@/components/ui/button";
import WeightEditModal from "./WeightEditModal.vue";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Tooltip, Legend);

const weightChartStore = useWeightChart();

const {
  isEmptyRecord,
  currentWeight,
  startWeight,
  weightLoss,
  goalWeight,
  weightData,
  chartData,
  chartOptions,
} = storeToRefs(weightChartStore);
const openModal = weightChartStore.openModal;

const hasWeightData = computed(() => (weightData.value?.length || 0) > 0);
</script>

<template>
  <Card class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6">
    <div class="flex items-center justify-between mb-6">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
          <TrendingDown class="w-5 h-5 text-emerald-600" />
        </div>
        <div class="flex items-center gap-3">
          <h3 class="text-lg font-bold text-gray-900">몸무게 변화</h3>
          <Button
            v-if="hasWeightData"
            size="sm"
            class="bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl"
            @click="openModal"
          >
            <Plus class="w-4 h-4 mr-1" /> 몸무게 수정
          </Button>
        </div>
      </div>
      <div v-if="!isEmptyRecord" class="text-right">
        <div class="text-2xl font-bold text-gray-900">{{ currentWeight }} kg</div>
        <div class="text-xs text-gray-500">
          목표까지 {{ Math.round((currentWeight - goalWeight)*10)/10 }}kg
        </div>
      </div>
    </div>

    <div v-if="!isEmptyRecord" class="grid grid-cols-3 gap-4 mb-6">
      <div class="bg-gray-50 rounded-2xl p-4 border border-gray-100 text-center">
        <div class="text-xs text-gray-500 mb-1">시작 체중</div>
        <div class="text-lg font-bold text-gray-900">{{ startWeight }} kg</div>
      </div>
      <div class="bg-gray-50 rounded-2xl p-4 border border-gray-100 text-center">
        <div class="text-xs text-gray-500 mb-1">현재 체중</div>
        <div class="text-lg font-bold text-emerald-600">{{ currentWeight }} kg</div>
      </div>
      <div class="bg-gray-50 rounded-2xl p-4 border border-gray-100 text-center">
        <div class="text-xs text-gray-500 mb-1">감량</div>
        <div class="text-lg font-bold text-blue-600">-{{ weightLoss }} kg</div>
      </div>
    </div>

    <div class="relative h-[300px]">
      <div :class="[{ 'blur-sm pointer-events-none': !hasWeightData }, 'h-full']">
        <Line :data="chartData" :options="chartOptions" />
      </div>
      <div
        v-if="!hasWeightData"
        class="absolute inset-0 flex items-center justify-center"
      >
        <div class="flex flex-col items-center gap-3">
          <p class="text-sm text-gray-600">몸무게를 입력하면 추적이 시작됩니다</p>
          <Button class="bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl" @click="openModal">
            <Plus class="w-4 h-4 mr-2" />
            몸무게 추가
          </Button>
        </div>
      </div>
    </div>
  </Card>
  <WeightEditModal />
</template>
