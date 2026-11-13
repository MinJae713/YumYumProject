<script setup>
import ExerciseCalendar from "@/components/exercise/ExerciseCalendar.vue";
import DailyExerciseList from "@/components/exercise/DailyExerciseList.vue";
import useExerciseView from "@/stores/exercise/ExerciseView";
import { onMounted } from "vue";
import { storeToRefs } from "pinia";

const store = useExerciseView();
const { datesWithExercise } = storeToRefs(store);
onMounted(() => {
  // 팀원의 로직이 Store에 반영되어 있다면 여기서 호출됩니다.
  store.setExerciseData();
});
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-6 space-y-6">
    <div class="flex items-end justify-between">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">운동 관리</h1>
        <p class="text-sm text-gray-500 mt-1">캘린더에서 날짜를 선택하고 운동을 관리하세요</p>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-12 gap-6 h-[900px] min-h-[700px]">
      <div class="lg:col-span-7 bg-white rounded-3xl p-8 border border-gray-200 shadow-sm flex flex-col justify-start items-center">
        <ExerciseCalendar
          :dates-with-exercise="datesWithExercise"
          class="w-full h-full"
        />
      </div>

      <div class="lg:col-span-5 bg-white rounded-3xl p-6 border border-gray-200 shadow-sm flex flex-col h-full overflow-hidden">
        <DailyExerciseList />
      </div>
    </div>
  </div>
</template>
