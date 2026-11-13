<script setup>
import { computed, ref } from "vue";
import { ChevronLeft, ChevronRight } from "lucide-vue-next";
import useMealView from "@/stores/meal/MealView";
import { storeToRefs } from "pinia";

const store = useMealView();
const {
  selectedDate,
  datesWithMeals
} = storeToRefs(store);

const handleSelectDate = store.handleSelectDate;
const weekdays = ["일", "월", "화", "수", "목", "금", "토"];

const selectedDateObj = computed(() => new Date(selectedDate.value));
const currentYear = ref(selectedDateObj.value.getFullYear());
const currentMonth = ref(selectedDateObj.value.getMonth());

const formatYMD = (date) => {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, "0");
  const d = String(date.getDate()).padStart(2, "0");
  return `${y}-${m}-${d}`;
};

const mealsSet = computed(() =>
  new Set((datesWithMeals.value || []).map((d) => formatYMD(new Date(d))))
);

const daysInMonth = computed(
  () => new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
);

const firstDayOfMonth = computed(
  () => new Date(currentYear.value, currentMonth.value, 1).getDay()
);

const calendarGrid = computed(() => {
  const grid = Array.from({ length: firstDayOfMonth.value }, () => null);
  for (let day = 1; day <= daysInMonth.value; day++) {
    grid.push(day);
  }
  return grid;
});

const goToMonth = (delta) => {
  const next = new Date(currentYear.value, currentMonth.value + delta, 1);
  currentYear.value = next.getFullYear();
  currentMonth.value = next.getMonth();
};

const prevMonth = () => goToMonth(-1);
const nextMonth = () => goToMonth(1);

const todayYMD = formatYMD(new Date());

const isSelected = (day) => {
  if (!day) return false;
  const date = new Date(currentYear.value, currentMonth.value, day);
  return formatYMD(date) === formatYMD(selectedDateObj.value);
};

const isToday = (day) => {
  if (!day) return false;
  const date = new Date(currentYear.value, currentMonth.value, day);
  return formatYMD(date) === todayYMD;
};

const hasMeal = (day) => {
  if (!day) return false;
  const date = new Date(currentYear.value, currentMonth.value, day);
  return mealsSet.value.has(formatYMD(date));
};

const selectDay = (day) => {
  if (!day) return;
  const date = new Date(currentYear.value, currentMonth.value, day);
  handleSelectDate(date);
};
</script>

<template>
  <div class="flex flex-col h-full w-full max-w-3xl mx-auto bg-white border border-transparent rounded-2xl p-3">
    <div class="flex items-center justify-between mb-3">
      <div>
        <h3 class="text-lg font-semibold text-gray-900">
          {{ currentYear }}년 {{ currentMonth + 1 }}월
        </h3>
        <p class="text-sm text-gray-500">기록을 한눈에 확인하세요</p>
      </div>
      <div class="flex items-center gap-2">
        <button
          class="w-9 h-9 rounded-full border border-gray-200 flex items-center justify-center hover:bg-gray-50"
          @click="prevMonth"
        >
          <ChevronLeft class="w-4 h-4" />
        </button>
        <button
          class="w-9 h-9 rounded-full border border-gray-200 flex items-center justify-center hover:bg-gray-50"
          @click="nextMonth"
        >
          <ChevronRight class="w-4 h-4" />
        </button>
      </div>
    </div>

    <div class="flex items-center gap-3 text-xs text-gray-500 mb-2">
      <div class="flex items-center gap-1">
        <span class="w-2 h-2 rounded-full bg-emerald-500"></span>
        <span>기록 완료</span>
      </div>
      <div class="flex items-center gap-1">
        <span class="w-2 h-2 rounded-full bg-gray-300"></span>
        <span>기록 없음</span>
      </div>
    </div>

    <div class="grid grid-cols-7 text-center py-2 text-gray-500 font-medium">
      <div v-for="day in weekdays" :key="day">{{ day }}</div>
    </div>

    <div class="grid grid-cols-7 border-t border-gray-100 rounded-xl overflow-hidden">
      <template v-for="(day, index) in calendarGrid" :key="index">
        <div
          v-if="day"
          class="border border-gray-100 bg-gray-50 min-h-[100px] flex flex-col items-start p-3 relative hover:bg-gray-50 cursor-pointer transition-colors rounded-xl m-1"
          :class="{
            'bg-emerald-50 border-emerald-500 ring-1 ring-emerald-500': isSelected(day),
          }"
          @click="selectDay(day)"
        >
          <div class="flex items-center gap-2">
            <span
              v-if="isToday(day)"
              class="w-7 h-7 rounded-full bg-gray-900 text-white flex items-center justify-center text-sm"
            >
              {{ day }}
            </span>
            <span v-else class="text-sm text-gray-800">{{ day }}</span>
          </div>
          <div class="flex-1 flex items-end w-full">
            <div
              v-if="hasMeal(day)"
              class="w-2 h-2 rounded-full bg-emerald-500 mb-1"
            ></div>
          </div>
        </div>
        <div
          v-else
          class="border border-gray-50 min-h-[100px] bg-gray-50 m-1 rounded-xl"
        ></div>
      </template>
    </div>
    <div class="flex-1 bg-gray-50 rounded-b-xl mt-2"></div>
  </div>
</template>
