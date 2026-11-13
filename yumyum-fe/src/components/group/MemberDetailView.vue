<script setup>
import { ref, computed, onMounted, watch, onBeforeUnmount } from "vue";
import { storeToRefs } from "pinia";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import MemberRecordDetail from "@/components/group/MemberRecordDetail.vue";
import { Calendar as CalendarIcon, Dumbbell, Target, Utensils } from "lucide-vue-next";
import { useGroupStore } from "@/stores/group/GroupStore";
import { formatDateKey, getWeightChangeIcon, formatGoalLabel } from "@/utils/groupUtils";

const props = defineProps({
  member: { type: Object, default: () => ({}) },
  groupId: { type: [Number, String], required: true },
});
const emit = defineEmits(["update:isDeepView"]);

const groupStore = useGroupStore();
const { memberDailyLog, recordedMealDates, recordedExerciseDates } = storeToRefs(groupStore);

const selectedDate = ref(new Date());
const selectedRecord = ref(null);
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

const currentRecord = computed(() => memberDailyLog.value);

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

const displayTotalIntakeCalorie = computed(() => {
  const value = currentRecord.value?.totalIntakeCalorie;
  if (typeof value !== "number") return 0;
  return Math.round(value);
});

const displayTotalBurnedCalorie = computed(() => {
  const value = currentRecord.value?.totalIntakeCalorie;
  if (typeof value !== "number") return 0;
  return Math.round(value);
});

const mealLogsWithDisplayCalorie = computed(() =>
  (currentRecord.value?.mealLogs || []).map((meal) => ({
    ...meal,
    displayTotalCalorie: Math.round(typeof meal.totalCalorie === "number" ? meal.totalCalorie : 0),
  })),
);

const exerciseLogsWithDisplayCalorie = computed(() =>
  (currentRecord.value?.exerciseLogs || []).map((exercise) => ({
    ...exercise,
    displayBurnedCalorie: Math.round(
      typeof exercise.burnedCalorie === "number" ? exercise.burnedCalorie : 0,
    ),
  })),
);

const fetchLog = async () => {
  const memberId = props.member?.id || props.member?.userId;
  if (!memberId || !props.groupId || !selectedDate.value) {
    memberDailyLog.value = null;
    return;
  }
  await groupStore.fetchMemberDailyLog(props.groupId, memberId, selectedDate.value);
};

onMounted(() => {
  fetchLog();
  emit("update:isDeepView", !!selectedRecord.value);
});

watch(
  () => selectedDate.value,
  () => {
    currentMonth.value = new Date(selectedDate.value);
    fetchLog();
  },
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

const formatDateDisplay = (dateLike, options) => {
  const d = new Date(dateLike);
  if (isNaN(d.getTime())) return "";
  return d.toLocaleDateString("ko-KR", options);
};

watch(
  () => selectedRecord.value,
  (record) => {
    emit("update:isDeepView", !!record);
  },
);

const fetchRecordedDates = async (memberIdOverride = null) => {
  const memberId = memberIdOverride ?? props.member?.id ?? props.member?.userId;
  if (!props.groupId || !memberId) {
    recordedMealDates.value = [];
    recordedExerciseDates.value = [];
    return;
  }
  await groupStore.fetchRecordedDatesForLeader(props.groupId, memberId);
};

watch(
  () => props.member && (props.member.id ?? props.member.userId),
  (memberId) => {
    fetchRecordedDates(memberId);
  },
  { immediate: true },
);

onBeforeUnmount(() => {
  recordedMealDates.value = [];
  recordedExerciseDates.value = [];
});
</script>

<template>
  <div class="space-y-6">
    <MemberRecordDetail
      v-if="selectedRecord"
      :member="member"
      :record="selectedRecord"
      :group-id="groupId"
      @back="selectedRecord = null"
    />

    <template v-else>
      <div class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6">
        <div class="flex items-start gap-6">
          <Avatar class="w-20 h-20">
            <AvatarFallback class="w-full h-full flex items-center justify-center bg-blue-200 text-gray-900 font-bold text-3xl">
              {{ member.name?.[0] }}
            </AvatarFallback>
          </Avatar>

          <div class="flex-1">
            <div class="flex items-center gap-3 mb-2">
              <h2 class="text-xl font-semibold text-gray-900">{{ member.name }}</h2>
              <Badge variant="secondary">{{ formatGoalLabel(member.goal) }}</Badge>
            </div>

            <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4">
              <div>
                <div class="text-xs text-gray-500 mb-1">시작 체중</div>
                <div class="text-lg font-semibold text-gray-900">{{ member.startWeight }}kg</div>
              </div>
              <div>
                <div class="text-xs text-gray-500 mb-1">현재 체중</div>
                <div class="flex items-center gap-2">
                  <component :is="getWeightChangeIcon(member.currentWeight, member.startWeight)" class="w-5 h-5 text-emerald-500" />
                  <span class="text-lg font-semibold text-gray-900">{{ member.currentWeight }}kg</span>
                </div>
              </div>
              <div>
                <div class="text-xs text-gray-500 mb-1">목표 체중</div>
                <div class="flex items-center gap-2">
                  <Target class="w-5 h-5 text-emerald-500" />
                  <span class="text-lg font-semibold text-gray-900">{{ member.targetWeight }}kg</span>
                </div>
              </div>
              <div>
                <div class="text-xs text-gray-500 mb-1">변화량</div>
                <div class="text-lg font-semibold text-gray-900">
                  {{ member.currentWeight - member.startWeight > 0 ? "+" : "" }}
                  {{ (member.currentWeight - member.startWeight)?.toFixed(1) }}kg
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-12 gap-6 h-[600px]">
        <div class="lg:col-span-7 bg-white rounded-3xl border border-gray-200 shadow-sm p-6 flex flex-col">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-base font-semibold text-gray-900">
                {{ member.name }}님의 활동
              </h3>
              <p class="text-xs text-gray-500">기록이 있는 날짜를 선택하세요</p>
            </div>
            <div class="flex items-center gap-2">
              <button
                type="button"
                class="w-8 h-8 rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200"
                @click="changeMonth(-1)"
              >
                ‹
              </button>
              <div class="text-sm font-semibold text-gray-900">
                {{ formatDateDisplay(currentMonth, { year: "numeric", month: "long" }) }}
              </div>
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

          <div class="mt-4 p-3 bg-gray-50 rounded-xl text-sm text-gray-700">
            {{ selectedDate.toLocaleDateString("ko-KR", { year: "numeric", month: "long", day: "numeric", weekday: "long" }) }}
          </div>
        </div>

        <div class="lg:col-span-5 bg-white rounded-3xl border border-gray-200 shadow-sm p-6 flex flex-col">
          <div class="flex items-center gap-2 mb-6">
            <CalendarIcon class="w-5 h-5 text-emerald-500" />
            <h3 class="text-lg font-semibold text-gray-900">
              {{ selectedDate.toLocaleDateString("ko-KR", { month: "long", day: "numeric", weekday: "long" }) }}
            </h3>
          </div>

          <template v-if="currentRecord">
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

                <div
                  v-for="(meal, idx) in mealLogsWithDisplayCalorie || []"
                  :key="idx"
                  class="bg-white border border-gray-100 rounded-xl p-4 hover:border-emerald-200 transition-all cursor-pointer"
                  @click="
                    selectedRecord = {
                      type: 'meal',
                      data: { mealType: meal.mealType, ...meal },
                      date: formatDateKey(selectedDate),
                    }
                  "
                >
                  <div class="flex items-center justify-between mb-2">
                    <h4 class="text-sm font-semibold text-gray-900">{{ meal.mealType }}</h4>
                    <Badge variant="outline" class="text-xs">{{ meal.displayTotalCalorie }} kcal</Badge>
                  </div>
                  <div class="space-y-1">
                    <div
                      v-for="(food, foodIdx) in meal.foodNames"
                      :key="foodIdx"
                      class="text-xs text-gray-500 flex items-center gap-2"
                    >
                      <span class="text-emerald-500">•</span>
                      <span>{{ food }}</span>
                    </div>
                  </div>
                </div>
              </TabsContent>

              <TabsContent value="exercises" class="space-y-3 mt-4">
                <div class="bg-gray-100 rounded-xl p-4 flex items-center justify-between text-sm">
                  <span class="text-gray-600">총 소모 칼로리</span>
                  <span class="text-2xl font-semibold text-emerald-600">{{ displayTotalBurnedCalorie }}</span>
                  <span class="text-gray-500">kcal</span>
                </div>

                <div
                  v-for="(exercise, idx) in exerciseLogsWithDisplayCalorie || []"
                  :key="idx"
                  class="bg-white border border-gray-100 rounded-xl p-4 hover:border-emerald-200 transition-all cursor-pointer"
                  @click="
                    selectedRecord = {
                      type: 'exercise',
                      data: exercise,
                      date: formatDateKey(selectedDate),
                    }
                  "
                >
                  <div class="flex items-center justify-between mb-2">
                    <h4 class="text-sm font-semibold text-gray-900">{{ exercise.exerciseName }}</h4>
                    <Badge variant="outline" class="text-xs">{{ exercise.displayBurnedCalorie }} kcal</Badge>
                  </div>
                  <div class="text-xs text-gray-500">{{ exercise.durationMin }}분</div>
                </div>
              </TabsContent>
            </Tabs>
          </template>
          <div v-else class="flex flex-col items-center justify-center py-12 text-center">
            <div class="w-16 h-16 rounded-full bg-gray-100 flex items-center justify-center mb-4">
              <CalendarIcon class="w-8 h-8 text-gray-400" />
            </div>
            <h4 class="mb-2 text-gray-800">기록이 없습니다</h4>
            <p class="text-sm text-gray-500">
              해당 날짜에 등록된 식단이나 운동이 없습니다.
            </p>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>
