<script setup>
import { ref, computed, watch, onMounted, onBeforeUnmount } from "vue";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Calendar as CalendarIcon, Dumbbell, MessageSquare, Utensils } from "lucide-vue-next";
import { useGroupStore } from "@/stores/group/GroupStore";
import { storeToRefs } from "pinia";

const props = defineProps({
  dailyLog: { type: Object, default: null },
  groupId: { type: [Number, String], default: null },
  selectedRecord: { type: Object, default: null },
});

const emit = defineEmits(["record-click", "date-change"]);

const groupStore = useGroupStore();
const { recordedMealDates, recordedExerciseDates } = storeToRefs(groupStore);

const selectedDate = ref(new Date());
const currentMonth = ref(new Date());

const startOfMonth = computed(
  () => new Date(currentMonth.value.getFullYear(), currentMonth.value.getMonth(), 1),
);
const daysInMonth = computed(
  () => new Date(currentMonth.value.getFullYear(), currentMonth.value.getMonth() + 1, 0).getDate(),
);
const calendarGrid = computed(() => {
  const startDay = startOfMonth.value.getDay();
  const totalSlots = Math.ceil((startDay + daysInMonth.value) / 7) * 7;
  return Array.from({ length: totalSlots }, (_, idx) => {
    const dayNum = idx - startDay + 1;
    const inMonth = dayNum >= 1 && dayNum <= daysInMonth.value;
    const date = new Date(currentMonth.value.getFullYear(), currentMonth.value.getMonth(), dayNum);
    return { date, inMonth };
  });
});

const formatDateDisplay = (dateLike, options) => {
  const d = new Date(dateLike);
  if (isNaN(d.getTime())) return "";
  return d.toLocaleDateString("ko-KR", options);
};

watch(
  () => selectedDate.value,
  (newDate) => {
    emit("date-change", newDate);
    currentMonth.value = new Date(newDate);
  },
);

const feedbackCount = computed(() => {
  if (!props.dailyLog) return 0;
  const meals = props.dailyLog.mealLogs || [];
  const exercises = props.dailyLog.exerciseLogs || [];
  const mealFeedbacks = meals.filter((m) => m.hasFeedback).length;
  const exerciseFeedbacks = exercises.filter((e) => e.hasFeedback).length;
  return mealFeedbacks + exerciseFeedbacks;
});

const displayTotalIntakeCalorie = computed(() => {
  const value = props.dailyLog?.totalIntakeCalorie;
  if (typeof value !== "number") return 0;
  return Math.round(value);
});

const displayTotalBurnedCalorie = computed(() => {
  const value = props.dailyLog?.totalBurnedCalorie;
  if (typeof value !== "number") return 0;
  return Math.round(value);
});

const mealLogsWithDisplayCalorie = computed(() =>
  (props.dailyLog?.mealLogs || []).map((meal) => ({
    ...meal,
    displayTotalCalorie: Math.round(typeof meal.totalCalorie === "number" ? meal.totalCalorie : 0),
  })),
);

const exerciseLogsWithDisplayCalorie = computed(() =>
  (props.dailyLog?.exerciseLogs || []).map((exercise) => ({
    ...exercise,
    displayBurnedCalorie: Math.round(
      typeof exercise.burnedCalorie === "number" ? exercise.burnedCalorie : 0,
    ),
  })),
);

const handleRecordClick = (payload) => emit("record-click", payload);

const hasMealRecordForDate = (date) =>
  recordedMealDates.value.some(
    (d) =>
      d.getFullYear() === date.getFullYear() &&
      d.getMonth() === date.getMonth() &&
      d.getDate() === date.getDate(),
  );

const hasExerciseRecordForDate = (date) =>
  recordedExerciseDates.value.some(
    (d) =>
      d.getFullYear() === date.getFullYear() &&
      d.getMonth() === date.getMonth() &&
      d.getDate() === date.getDate(),
  );

const handleDaySelect = (day) => {
  if (!day.inMonth) return;
  selectedDate.value = day.date;
};

const changeMonth = (delta) => {
  const next = new Date(currentMonth.value);
  next.setMonth(currentMonth.value.getMonth() + delta);
  currentMonth.value = next;
  selectedDate.value = new Date(next.getFullYear(), next.getMonth(), 1);
};

onMounted(() => {
  if (props.groupId) {
    groupStore.fetchRecordedDatesForMember(props.groupId);
  }
});

watch(
  () => props.selectedRecord,
  (newVal, oldVal) => {
    if (oldVal && !newVal && props.groupId) {
      groupStore.fetchRecordedDatesForMember(props.groupId);
    }
  },
);

</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-12 gap-6 h-[600px]">
    <div class="lg:col-span-7 bg-white rounded-3xl border border-gray-200 shadow-sm p-6 flex flex-col">
      <div class="flex items-center justify-between mb-4">
        <div>
          <h3 class="text-base font-semibold text-gray-900">
            {{ formatDateDisplay(currentMonth, { year: "numeric", month: "long" }) }}
          </h3>
          <div class="flex items-center gap-2 text-xs text-gray-500 mt-1">
            <div class="flex items-center gap-1">
              <div class="w-2 h-2 rounded-full bg-emerald-500" />
              <span>기록 있음</span>
            </div>
            <div class="flex items-center gap-1">
              <MessageSquare class="w-3 h-3 text-emerald-500" />
              <span>피드백 있음</span>
            </div>
          </div>
        </div>
        <div class="flex items-center gap-2">
          <button
            type="button"
            class="w-8 h-8 rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200"
            @click="changeMonth(-1)"
          >
            ‹
          </button>
          <button
            type="button"
            class="w-8 h-8 rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200"
            @click="changeMonth(1)"
          >
            ›
          </button>
        </div>
      </div>

      <div class="grid grid-cols-7 text-center text-xs text-gray-500 mb-2">
        <span v-for="day in ['일','월','화','수','목','금','토']" :key="day" class="py-1">{{ day }}</span>
      </div>

      <div class="flex-1 grid grid-cols-7 grid-rows-6">
        <button
          v-for="(day, idx) in calendarGrid"
          :key="idx"
          type="button"
          class="bg-gray-50 rounded-xl m-1 p-2 flex flex-col text-sm transition-all"
          :class="[
            day.inMonth ? 'text-gray-800' : 'text-gray-300',
            formatDateDisplay(day.date, { year: 'numeric', month: '2-digit', day: '2-digit' }) === formatDateDisplay(selectedDate, { year: 'numeric', month: '2-digit', day: '2-digit' })
              ? 'bg-emerald-50 ring-1 ring-emerald-500'
              : '',
          ]"
          @click="handleDaySelect(day)"
        >
          <span>{{ day.inMonth ? day.date.getDate() : "" }}</span>
          <div
            v-if="day.inMonth && (hasMealRecordForDate(day.date) || hasExerciseRecordForDate(day.date))"
            class="mt-auto flex justify-center gap-1"
          >
            <span
              v-if="hasMealRecordForDate(day.date)"
              class="w-1.5 h-1.5 rounded-full bg-orange-400"
            />
            <span
              v-if="hasExerciseRecordForDate(day.date)"
              class="w-1.5 h-1.5 rounded-full bg-blue-400"
            />
          </div>
        </button>
      </div>
    </div>

    <div class="lg:col-span-5 bg-white rounded-3xl border border-gray-200 shadow-sm p-6 flex flex-col">
      <div class="flex items-center gap-2 mb-6">
        <CalendarIcon class="w-5 h-5 text-emerald-500" />
        <h3 class="text-lg font-semibold text-gray-900">
          {{ formatDateDisplay(selectedDate, { month: "long", day: "numeric", weekday: "long" }) }}
        </h3>
        <Badge v-if="feedbackCount > 0" variant="default" class="ml-2">
          <MessageSquare class="w-3 h-3 mr-1" />
          피드백 {{ feedbackCount }}
        </Badge>
      </div>

      <template v-if="props.dailyLog">
        <Tabs default-value="meals" class="w-full flex-1 flex flex-col">
          <TabsList class="w-full bg-gray-100 p-1 rounded-xl grid grid-cols-2">
            <TabsTrigger value="meals" class="rounded-lg data-[state=active]:bg-white data-[state=active]:shadow-sm">
              <Utensils class="w-4 h-4 mr-2" />
              식단
            </TabsTrigger>
            <TabsTrigger value="exercises" class="rounded-lg data-[state=active]:bg-white data-[state=active]:shadow-sm">
              <Dumbbell class="w-4 h-4 mr-2" />
              운동
            </TabsTrigger>
          </TabsList>

          <TabsContent value="meals" class="space-y-3 mt-4">
            <div class="bg-emerald-50 rounded-xl p-4 flex items-center justify-between text-sm">
              <span class="text-gray-600">총 섭취 칼로리</span>
              <span class="text-2xl font-semibold text-emerald-600">{{ displayTotalIntakeCalorie }}</span>
              <span class="text-gray-500">kcal</span>
            </div>

            <template v-if="(mealLogsWithDisplayCalorie || []).length">
              <div
                v-for="(meal, idx) in mealLogsWithDisplayCalorie"
                :key="idx"
                class="bg-white border border-gray-100 rounded-xl p-4 mb-2 hover:border-emerald-200 cursor-pointer transition-all"
                @click="handleRecordClick({ type: 'meal', data: meal, date: selectedDate })"
              >
                <div class="flex items-center justify-between mb-2">
                  <h4 class="text-sm font-semibold text-gray-900">
                    {{ meal.mealType }}
                  </h4>
                  <div class="flex items-center gap-2">
                    <Badge variant="outline" class="text-xs">{{ meal.displayTotalCalorie }} kcal</Badge>
                    <MessageSquare v-if="meal.hasFeedback" class="w-4 h-4 text-emerald-500" />
                  </div>
                </div>
                <div class="space-y-1">
                  <div
                    v-for="(food, foodIdx) in meal.foodNames || []"
                    :key="foodIdx"
                    class="text-xs text-gray-500 flex items-center gap-2"
                  >
                    <span class="text-emerald-500">•</span>
                    <span>{{ food }}</span>
                  </div>
                </div>
              </div>
            </template>
            <div v-else class="bg-gray-50 border border-dashed border-gray-200 rounded-xl p-4 text-center text-sm text-gray-500">
              식단 기록이 없습니다.
            </div>
          </TabsContent>

          <TabsContent value="exercises" class="space-y-3 mt-4">
            <div class="bg-gray-100 rounded-xl p-4 flex items-center justify-between text-sm">
              <span class="text-gray-600">총 소모 칼로리</span>
              <span class="text-2xl font-semibold text-gray-900">{{ displayTotalBurnedCalorie }}</span>
              <span class="text-gray-500">kcal</span>
            </div>

            <template v-if="(exerciseLogsWithDisplayCalorie || []).length">
              <div
                v-for="(exercise, idx) in exerciseLogsWithDisplayCalorie"
                :key="idx"
                class="bg-white border border-gray-100 rounded-xl p-4 mb-2 hover:border-emerald-200 cursor-pointer transition-all"
                @click="handleRecordClick({ type: 'exercise', data: exercise, date: selectedDate })"
              >
                <div class="flex items-center justify-between mb-2">
                  <h4 class="text-sm font-semibold text-gray-900">{{ exercise.exerciseName }}</h4>
                  <div class="flex items-center gap-2">
                    <Badge variant="outline" class="text-xs">{{ exercise.displayBurnedCalorie }} kcal</Badge>
                    <MessageSquare v-if="exercise.hasFeedback" class="w-4 h-4 text-emerald-500" />
                  </div>
                </div>
                <div class="text-xs text-gray-500">{{ exercise.durationMin }}분</div>
              </div>
            </template>
            <div v-else class="bg-gray-50 border border-dashed border-gray-200 rounded-xl p-4 text-center text-sm text-gray-500">
              운동 기록이 없습니다.
            </div>
          </TabsContent>
        </Tabs>
      </template>
      <div v-else class="flex flex-col items-center justify-center py-12 text-center">
        <div class="w-16 h-16 rounded-full bg-gray-100 flex items-center justify-center mb-4">
          <CalendarIcon class="w-8 h-8 text-gray-400" />
        </div>
        <h4 class="mb-2 text-gray-800">기록이 없습니다</h4>
        <p class="text-sm text-gray-500">해당 날짜에 등록된 식단이나 운동이 없습니다.</p>
      </div>
    </div>
  </div>
</template>
