<script setup>
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Flame, Pizza, Sparkles, Users } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import useMain from "@/stores/main/mainView";
import useMealView from "@/stores/meal/MealView";
import MealRegistrationModal from "@/components/meal/MealRegistrationModal.vue";
import { Doughnut } from "vue-chartjs";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { computed } from "vue";

ChartJS.register(ArcElement, Tooltip, Legend);

const mainStore = useMain();
const {
  recommendedIsDetailModalOpen,
  recommendedSelectedMeal
} = storeToRefs(mainStore);
const closeModal = mainStore.closeModal;

const mealStore = useMealView();
const {
  isRegistrationOpen
} = storeToRefs(mealStore);
const openApplyModal = mealStore.openApplyModal;
const openRegistrationModal = () => {
  // 얘는 mainStore, mealStore 둘 다 사용하는 메소드라 위와 같이 뺌
  const selectedMeal = recommendedSelectedMeal.value;
  closeModal?.();
  console.log(openApplyModal);
  openApplyModal?.(selectedMeal);
}

const donutChartData = computed(() => {
  if (!recommendedSelectedMeal.value) return null;
  const carbs = recommendedSelectedMeal.value.carbs ?? 0;
  const protein = recommendedSelectedMeal.value.protein ?? 0;
  const fat = recommendedSelectedMeal.value.fat ?? 0;
  return {
    labels: ["탄수화물", "단백질", "지방"],
    datasets: [
      {
        data: [carbs, protein, fat],
        backgroundColor: ["#60A5FA", "#34D399", "#FB923C"],
        borderWidth: 0,
      },
    ],
  };
});

const donutChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  cutout: "65%",
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      callbacks: {
        label: (context) => `${context.label}: ${context.parsed}g`,
      },
    },
  },
};
</script>

<template>
  <Dialog v-if="recommendedSelectedMeal" :open="recommendedIsDetailModalOpen" @update:open="(val) => !val && closeModal?.()">
    <DialogContent class="sm:max-w-[640px] max-h-[90vh] overflow-y-auto bg-white border border-gray-200 rounded-3xl shadow-lg">
      <DialogHeader class="space-y-2">
        <div class="flex items-center gap-3">
          <div
            v-if="recommendedSelectedMeal.type === 'ai'"
            class="w-12 h-12 bg-white border border-gray-200 rounded-2xl flex items-center justify-center shadow-sm"
          >
            <Sparkles class="w-6 h-6 text-emerald-500" />
          </div>
          <div
            v-else
            class="w-12 h-12 bg-white border border-gray-200 rounded-2xl flex items-center justify-center shadow-sm"
          >
            <Users class="w-6 h-6 text-emerald-500" />
          </div>
          <div class="flex-1">
            <Badge :variant="recommendedSelectedMeal.type === 'ai' ? 'default' : 'secondary'" class="mb-1">
              {{ recommendedSelectedMeal.type === "ai" ? "AI 추천" : "그룹장 추천" }}
            </Badge>
            <DialogTitle class="text-xl text-gray-900">{{ recommendedSelectedMeal.title }}</DialogTitle>
          </div>
        </div>
        <div class="bg-gray-50 rounded-xl p-4 text-sm text-gray-600 space-y-1">
          <p v-if="recommendedSelectedMeal.trainerName" class="font-medium text-gray-800">
            추천인: {{ recommendedSelectedMeal.trainerName }}
          </p>
          <DialogDescription class="text-gray-600">
            {{ recommendedSelectedMeal.description }}
          </DialogDescription>
        </div>
      </DialogHeader>

      <div class="space-y-6 py-4">
        <div class="flex flex-wrap gap-2">
          <Badge v-for="tag in recommendedSelectedMeal.tags" :key="tag" variant="outline">
            {{ tag }}
          </Badge>
        </div>

        <div class="bg-gray-50 rounded-2xl p-5 border border-gray-100">
          <div class="flex items-center gap-3 mb-4">
            <div class="w-12 h-12 bg-white rounded-xl flex items-center justify-center border border-gray-200 shadow-sm">
              <Flame class="w-6 h-6 text-emerald-500" />
            </div>
            <div>
              <div class="text-sm text-gray-500">총 칼로리</div>
              <div class="text-2xl font-bold text-emerald-600">
                {{ recommendedSelectedMeal.calories }} kcal
              </div>
            </div>
          </div>
          <div class="grid md:grid-cols-2 gap-4">
            <div class="h-[200px] rounded-xl bg-white border border-gray-100 p-4">
              <Doughnut
                v-if="donutChartData"
                :data="donutChartData"
                :options="donutChartOptions"
              />
            </div>
            <div class="grid grid-cols-1 gap-2">
              <div class="flex items-center justify-between p-3 bg-white rounded-xl border border-gray-100">
                <div class="flex items-center gap-2">
                  <div class="w-3 h-3 rounded-full bg-blue-500"></div>
                  <span class="text-sm text-gray-800">탄수화물</span>
                </div>
                <span class="text-sm font-semibold text-gray-700">{{ recommendedSelectedMeal.carbs }}g</span>
              </div>
              <div class="flex items-center justify-between p-3 bg-white rounded-xl border border-gray-100">
                <div class="flex items-center gap-2">
                  <div class="w-3 h-3 rounded-full bg-green-500"></div>
                  <span class="text-sm text-gray-800">단백질</span>
                </div>
                <span class="text-sm font-semibold text-gray-700">{{ recommendedSelectedMeal.protein }}g</span>
              </div>
              <div class="flex items-center justify-between p-3 bg-white rounded-xl border border-gray-100">
                <div class="flex items-center gap-2">
                  <div class="w-3 h-3 rounded-full bg-orange-500"></div>
                  <span class="text-sm text-gray-800">지방</span>
                </div>
                <span class="text-sm font-semibold text-gray-700">{{ recommendedSelectedMeal.fat }}g</span>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-2xl border border-gray-100 p-5 shadow-sm">
          <div class="flex items-center gap-2 mb-3">
            <Pizza class="w-5 h-5 text-emerald-500" />
            <h4 class="text-base font-semibold text-gray-900">식단 구성</h4>
          </div>
          <div class="divide-y divide-gray-100">
            <div
              v-for="(food, index) in recommendedSelectedMeal.foods"
              :key="index"
              class="flex items-center justify-between p-3"
            >
              <div class="flex items-center gap-2">
                <div class="w-2 h-2 rounded-full bg-emerald-500"></div>
                <span class="text-sm text-gray-800">{{ food.name }}</span>
              </div>
              <span class="text-sm text-gray-600">
                {{ food.amount }}{{ food.unit }}
              </span>
            </div>
          </div>
        </div>

        <div class="flex gap-3">
          <Button variant="outline" class="flex-1 hover:bg-gray-100" @click="closeModal?.()">
            닫기
          </Button>
          <Button class="flex-1 bg-emerald-500 hover:bg-emerald-600 text-white" @click="openRegistrationModal">
            이 식단 적용하기
          </Button>
        </div>
      </div>
      <MealRegistrationModal v-if="isRegistrationOpen"/>
    </DialogContent>
  </Dialog>
</template>
