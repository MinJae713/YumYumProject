<script setup>
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import ExerciseRegistrationModal from "@/components/exercise/ExerciseRegistrationModal.vue";
import { Dumbbell, Edit, Plus } from "lucide-vue-next";
import useExerciseView from "@/stores/exercise/ExerciseView";
import { storeToRefs } from "pinia";
import { getExerciseColor, getExerciseIcon, getExerciseLabel } from "@/utils/exerciseUtils";

const store = useExerciseView();
const {
  isGroupMember,
  selectedDate,
  exercises,
  totalCalories,
  totalPoints,
} = storeToRefs(store);
const openModal = store.openModal;
</script>

<template>
  <div class="h-full flex flex-col">
    <div class="flex items-center justify-between mb-6">
      <div>
        <h3 class="text-lg font-semibold text-gray-900">
          {{ new Date(selectedDate).toLocaleDateString("ko-KR", { month: "long", day: "numeric" }) }} 운동
        </h3>
        <p class="text-xs text-gray-500">
          {{ new Date(selectedDate).toLocaleDateString("ko-KR", { weekday: "long" }) }}
        </p>
      </div>
      <div class="flex gap-4">
        <div class="text-right">
          <div class="text-xs text-gray-500 mb-1">소모 칼로리</div>
          <div class="text-2xl text-emerald-600">{{ totalCalories }}</div>
          <div class="text-xs text-gray-500">kcal</div>
        </div>
        <div v-if="isGroupMember" class="text-right">
          <div class="text-xs text-gray-500 mb-1">획득 점수</div>
          <div class="text-2xl text-emerald-700">{{ totalPoints }}</div>
          <div class="text-xs text-gray-500">points</div>
        </div>
      </div>
    </div>

    <div v-if="(exercises && exercises.length)" class="flex-1 overflow-y-auto space-y-4 pr-1">
      <div
        v-for="exercise in exercises"
        :key="exercise.id"
        class="flex gap-4"
      >
        <div class="flex flex-col items-center">
          <div
            class="w-11 h-11 rounded-full bg-white border border-gray-200 flex items-center justify-center shadow-sm"
            :class="getExerciseColor(exercise.type)"
          >
            <component :is="getExerciseIcon(exercise.type)" class="w-5 h-5" />
          </div>
          <div class="w-px flex-1 bg-gray-200 mt-2"></div>
        </div>

        <div class="flex-1">
          <div class="bg-gray-50 rounded-2xl p-4 border border-gray-100 hover:border-emerald-200 hover:shadow-md transition-all">
            <div class="flex items-start justify-between mb-2">
              <div class="flex items-center gap-2">
                <h4 class="text-base font-semibold text-gray-900">{{ exercise.name }}</h4>
                <Badge variant="outline" class="text-xs">
                  {{ getExerciseLabel(exercise.type) }}
                </Badge>
              </div>
              <div class="flex items-center gap-2">
                <Badge variant="secondary" class="text-xs bg-emerald-50 text-emerald-700 border-emerald-100">
                  {{ exercise.calories }} kcal
                </Badge>
                <Badge 
                  v-if="isGroupMember" 
                  variant="outline" 
                  class="text-xs border-emerald-100 text-emerald-700"
                >
                  {{ exercise.points }}P
                </Badge>
              </div>
            </div>
            <div class="text-xs text-gray-600 mb-3">
              <span v-if="exercise.durationMin">[{{ exercise.durationMin }}분] </span>
              <span v-if="exercise.exerciseOptionDetail">{{ exercise.exerciseOptionDetail }}</span>
            </div>
            <Button size="sm" variant="outline" class="w-full" @click="openModal(exercise)">
              <Edit class="w-3 h-3 mr-1" />
              수정하기
            </Button>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="flex flex-col items-center justify-center flex-1">
      <div class="w-16 h-16 rounded-full bg-gray-100 flex items-center justify-center mb-3">
        <Dumbbell class="w-8 h-8 text-gray-400" />
      </div>
      <p class="text-sm text-gray-500 mb-4">등록된 운동이 없습니다</p>
      <Button variant="outline" @click="openModal()">
        <Plus class="w-4 h-4 mr-2" />
        첫 운동 추가
      </Button>
    </div>

    <div class="pt-4">
      <Button class="w-full bg-emerald-500 hover:bg-emerald-600 text-white" @click="openModal()">
        <Plus class="w-4 h-4 mr-2" />
        운동 추가
      </Button>
    </div>
  </div>

  <ExerciseRegistrationModal />
</template>