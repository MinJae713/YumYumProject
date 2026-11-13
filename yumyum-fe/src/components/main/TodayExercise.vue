<script setup>
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import ExerciseRegistrationModal from "@/components/exercise/ExerciseRegistrationModal.vue";
import { Clock, Dumbbell, Flame, Plus } from "lucide-vue-next";
import useExerciseView from "@/stores/exercise/ExerciseView";
import { storeToRefs } from "pinia";
import { getExerciseColor, getExerciseIcon } from "@/utils/exerciseUtils";

const store = useExerciseView();
const {
  isGroupMember,
  exercises,
  totalDuration,
  totalCalories
} = storeToRefs(store);
const openModal = store.openModal;
</script>

<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-2">
        <Dumbbell class="w-5 h-5 text-emerald-500" />
        <h3 class="text-lg font-semibold text-gray-900">오늘의 운동</h3>
      </div>
      <Button size="sm" variant="outline" @click="openModal(null)">
        <Plus class="w-4 h-4 mr-1" />
        운동 추가
      </Button>
    </div>

    <div class="flex gap-4 items-stretch">
      <div class="flex-1 bg-gray-50 rounded-xl p-4 flex items-center gap-3 border border-gray-100">
        <div class="w-10 h-10 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center">
          <Clock class="w-5 h-5" />
        </div>
        <div>
          <div class="text-xs text-gray-500">총 운동 시간</div>
          <div class="text-lg font-semibold">{{ totalDuration }}분</div>
        </div>
      </div>
      <div class="flex-1 bg-gray-50 rounded-xl p-4 flex items-center gap-3 border border-gray-100">
        <div class="w-10 h-10 rounded-full bg-orange-100 text-orange-600 flex items-center justify-center">
          <Flame class="w-5 h-5" />
        </div>
        <div>
          <div class="text-xs text-gray-500">소모 칼로리</div>
          <div class="text-lg font-semibold">{{ totalCalories }} kcal</div>
        </div>
      </div>
    </div>

    <div v-if="exercises.length > 0" class="flex flex-col gap-3">
      <button
        v-for="(exercise, index) in exercises"
        :key="index"
        class="w-full flex items-center justify-between p-3 border border-gray-100 rounded-xl hover:bg-gray-50 transition-all text-left"
        @click="openModal(exercise)"
      >
        <div
          class="w-12 h-12 mr-4 rounded-lg bg-card border border-border flex items-center justify-center flex-shrink-0"
          :class="getExerciseColor(exercise.type)"
        >
          <component :is="getExerciseIcon(exercise.type)" class="w-6 h-6" />
        </div>
        <div class="flex-1">
          <h4 class="text-sm mb-1 text-gray-900 font-medium">{{ exercise.name }}</h4>
          <div class="flex items-center gap-3 text-xs text-gray-500">
            <span class="flex items-center gap-1" v-if="exercise.duration">
              <Clock class="w-3 h-3" />
              {{ exercise.duration }}분
            </span>
            <span v-if="exercise.setNo && exercise.reps">
              {{ exercise.setNo }}세트 × {{ exercise.reps }}회
            </span>
          </div>
        </div>
        <div class="flex items-center gap-2">
          <Badge variant="secondary" class="text-xs bg-gray-100 text-gray-600">
            {{ exercise.calories }} kcal
          </Badge>
          <Badge 
            v-if="isGroupMember" 
            variant="outline" 
            class="text-xs border-emerald-100 text-emerald-700 bg-emerald-50"
          >
            {{ exercise.points }}P
          </Badge>
        </div>
      </button>
    </div>
    
    <div v-else class="flex flex-col items-center justify-center py-10 border border-dashed border-gray-200 rounded-xl bg-gray-50/50">
      <div class="w-14 h-14 rounded-full bg-gray-100 flex items-center justify-center mb-3">
        <Dumbbell class="w-7 h-7 text-gray-400" />
      </div>
      <p class="text-sm text-gray-500 mb-3">
        오늘 운동을 시작해보세요
      </p>
      <Button variant="outline" @click="openModal(null)">
        <Plus class="w-4 h-4 mr-1" />
        첫 운동 추가하기
      </Button>
    </div>
  </div>

  <ExerciseRegistrationModal />
</template>