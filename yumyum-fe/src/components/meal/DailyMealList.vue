<script setup>
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import MealRegistrationModal from "@/components/meal/MealRegistrationModal.vue";
import { Plus } from "lucide-vue-next";
import useMealView from "@/stores/meal/MealView";
import { storeToRefs } from "pinia";
import MealDetailModal from "@/components/meal/MealDetailModal.vue";
import { ref } from "vue";

const store = useMealView();
const {
  isGroupMember,
  selectedMealsData,
  isRegistrationOpen,
  mealSlots,
  selectedDate
} = storeToRefs(store);
const openModal = store.openModal;

const isDetailOpen = ref(false);
const currentDetailMeal = ref(null);
const currentSlotKey = ref(null);

const openDetail = (slotKey, mealData) => {
  currentSlotKey.value = slotKey;
  currentDetailMeal.value = mealData;
  isDetailOpen.value = true;
};

const closeDetail = () => {
  isDetailOpen.value = false;
  currentDetailMeal.value = null;
  currentSlotKey.value = null;
};

const handleEditFromDetail = () => {
  if (currentSlotKey.value && currentDetailMeal.value) {
    openModal(currentSlotKey.value, currentDetailMeal.value);
  }
  closeDetail();
};
</script>

<template>
  <div class="h-full flex flex-col">
    <div class="flex items-center justify-between mb-4">
      <div>
        <h3 class="text-lg font-semibold text-gray-900">
          {{ new Date(selectedDate).toLocaleDateString("ko-KR", { month: "long", day: "numeric" }) }} 식단
        </h3>
        <p class="text-xs text-gray-500">
          {{ new Date(selectedDate).toLocaleDateString("ko-KR", { weekday: "long" }) }}
        </p>
      </div>
      <div class="flex gap-4">
        <div class="text-right">
          <div class="text-xs text-gray-500 mb-1">총 칼로리</div>
          <div class="text-xl text-emerald-600">{{ selectedMealsData.mealTotalCalorie }}</div>
          <div class="text-xs text-gray-500">kcal</div>
        </div>
        <div v-if="selectedMealsData.mealTotalPoint != null" class="text-right">
          <div class="text-xs text-gray-500 mb-1">총 획득 점수</div>
          <div class="text-xl text-emerald-700">{{ selectedMealsData.mealTotalPoint }}</div>
          <div class="text-xs text-gray-500">points</div>
        </div>
        <div v-if="selectedMealsData.mealTotalBenefitPoint != null" class="text-right">
          <div class="text-xs text-gray-500 mb-1">총 베네핏 점수</div>
          <div class="text-xl text-emerald-700">{{ selectedMealsData.mealTotalBenefitPoint }}</div>
          <div class="text-xs text-gray-500">points</div>
        </div>
      </div>
    </div>

    <div class="flex-1 overflow-y-auto space-y-6 pr-1">
      <div
        v-for="(slot, index) in mealSlots"
        :key="slot.key"
        class="flex gap-4 relative"
      >
        <div class="flex flex-col items-center">
          <div :class="`w-11 h-11 rounded-full border ${slot.bg} bg-white flex items-center justify-center shadow-sm`">
            <component :is="slot.icon" :class="`w-5 h-5 ${slot.color}`" />
          </div>
          <div
            class="w-px flex-1 bg-gray-200 mt-2"
            :class="index === mealSlots.length - 1 ? 'opacity-0' : ''"
          ></div>
        </div>

        <div class="flex-1">
          <div class="bg-gray-50 border border-gray-100 rounded-2xl p-4 shadow-sm">
            <div class="flex items-start justify-between mb-3">
              <div>
                <h4 class="text-base font-semibold text-gray-900">{{ slot.label }}</h4>
                <p class="text-xs text-gray-500">{{ slot.time }}</p>
              </div>
              <div v-if="selectedMealsData[slot.label]" class="flex gap-2">
                <Badge variant="outline" class="border-emerald-200 text-emerald-700 bg-emerald-50">
                  {{ selectedMealsData[slot.label].totalCalories }} kcal
                </Badge>
                
                <Badge 
                  v-if="selectedMealsData.mealTotalPoint != null" 
                  variant="outline" 
                  class="border-gray-200 text-gray-600"
                >
                  {{ selectedMealsData[slot.label].points || 0 }}P
                </Badge>
              </div>
            </div>

            <div
              v-if="selectedMealsData[slot.label]"
              class="space-y-3 cursor-pointer hover:border-emerald-200 hover:shadow-md transition-all"
              @click="openDetail(slot.key, selectedMealsData[slot.label])"
            >
              <ul class="text-sm space-y-2 text-gray-700">
                <li
                  v-for="(food, idx) in selectedMealsData[slot.label].foods"
                  :key="idx"
                  class="flex items-center gap-2"
                >
                  <span class="w-1.5 h-1.5 rounded-full bg-emerald-500"></span>
                  <span>{{ food.name }} {{ food.amount }}{{ food.unit }}</span>
                </li>
              </ul>

              <div
                class="flex flex-wrap gap-3 text-xs bg-white border border-gray-100 p-3 rounded-xl cursor-pointer hover:border-emerald-200 hover:shadow-md transition-all"
                @click="openDetail(slot.key, selectedMealsData[slot.label])"
              >
                <span class="text-emerald-700 font-semibold">단백질 {{ selectedMealsData[slot.label].protein }}g</span>
                <span class="text-orange-600 font-semibold">탄수화물 {{ selectedMealsData[slot.label].carbs }}g</span>
                <span class="text-gray-600 font-semibold">지방 {{ selectedMealsData[slot.label].fat }}g</span>
              </div>
            </div>

            <div
              v-else
              class="mt-2 border-2 border-dashed border-gray-200 rounded-xl p-4 flex items-center justify-between"
            >
              <div>
                <p class="text-sm font-medium text-gray-700">{{ slot.label }} 식단이 없습니다</p>
                <p class="text-xs text-gray-500">{{ slot.time }}</p>
              </div>
              <Button size="sm" variant="outline" class="text-emerald-700 border-emerald-200 hover:bg-emerald-50" @click="openModal(slot.key)">
                <Plus class="w-4 h-4 mr-1" /> 식단 추가
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <MealRegistrationModal v-if="isRegistrationOpen" />
    <MealDetailModal
      v-if="currentDetailMeal"
      :isOpen="isDetailOpen"
      :mealData="currentDetailMeal"
      :date="selectedDate"
      @close="closeDetail"
      @edit="handleEditFromDetail"
    />
  </div>
</template>