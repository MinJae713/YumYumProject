<script setup>
import MealCalendar from "@/components/meal/MealCalendar.vue";
import DailyMealList from "@/components/meal/DailyMealList.vue";
import useMealView from "@/stores/meal/MealView";
import { storeToRefs } from "pinia";
import { onMounted } from "vue";

const store = useMealView();
const { selectedDate } = storeToRefs(store);
onMounted(() => {
  store.setMealData();
})
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-6 space-y-6">
    <div class="flex items-end justify-between">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">식단 관리</h1>
        <p class="text-sm text-gray-500 mt-1">날짜별 식단을 기록하고 영양 밸런스를 맞춰보세요.</p>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-12 gap-6 h-[900px] min-h-[700px]">
      <div class="lg:col-span-7 bg-white rounded-3xl p-8 border border-gray-200 shadow-sm flex flex-col justify-start items-center">
        <MealCalendar class="w-full h-full" />
      </div>

      <div class="lg:col-span-5 bg-white rounded-3xl p-6 border border-gray-200 shadow-sm flex flex-col h-full overflow-hidden">
        <DailyMealList />
      </div>
    </div>
  </div>
</template>
