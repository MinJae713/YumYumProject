<script setup>
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import MealRegistrationModal from "@/components/meal/MealRegistrationModal.vue";
import { Plus, Utensils } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import useMealView from "@/stores/meal/MealView";

const store = useMealView();

const {
  mealSlots,
  selectedMealsData,
  isRegistrationOpen,
} = storeToRefs(store);

const openModal = store.openModal;
</script>

<template>
  <div class="space-y-4">
    <!-- 제목 -->
    <div class="flex items-center gap-2">
      <Utensils class="w-5 h-5 text-emerald-500" />
      <h3 class="text-lg font-semibold text-gray-900">오늘의 식단</h3>
    </div>

    <!-- 식단 카드 -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <div
        v-for="slot in mealSlots"
        :key="slot.key"
        class="relative p-4 h-[250px] transition-all hover:shadow-md cursor-pointer
               border border-gray-200 rounded-xl bg-white"
        @click="openModal(slot.label, selectedMealsData[slot.label])"
      >
        <!-- 🔹 칼로리 배지 (상단 우측 고정) -->
        <Badge
          v-if="selectedMealsData[slot.label]"
          variant="outline"
          class="absolute top-3 right-3"
        >
          {{ selectedMealsData[slot.label].totalCalories }} kcal
        </Badge>

        <!-- 슬롯 헤더 -->
        <div class="flex items-center gap-3 mb-3">
          <div class="w-10 h-10 rounded-lg bg-muted/50 flex items-center justify-center">
            <component :is="slot.icon" :class="`w-5 h-5 ${slot.color}`" />
          </div>
          <div>
            <h4 class="text-sm font-semibold">{{ slot.label }}</h4>
            <p class="text-xs text-muted-foreground">{{ slot.time }}</p>
          </div>
        </div>

        <!-- 식단 있음 -->
        <div v-if="selectedMealsData[slot.label]" class="space-y-3">
          <div class="space-y-2">
            <!-- 음식명 (칼로리 배지와 겹치지 않도록 padding-right) -->
            <h4 class="text-sm font-medium pr-14">
              {{ selectedMealsData[slot.label].foods.map(f => f.name).join(", ") }}
            </h4>

            <div class="space-y-1">
              <div
                v-for="(item, idx) in selectedMealsData[slot.label].foods.slice(0, 3)"
                :key="idx"
                class="text-xs text-muted-foreground flex items-start gap-2"
              >
                <span class="text-primary">•</span>
                <span>{{ item.name }} {{ item.amount }}{{ item.unit }}</span>
              </div>

              <div
                v-if="selectedMealsData[slot.label].foods.length > 3"
                class="text-xs text-muted-foreground flex items-start gap-2"
              >
                <span class="text-primary">•</span>
                <span>
                  외 {{ selectedMealsData[slot.label].foods.length - 3 }}개
                </span>
              </div>
            </div>
          </div>

          <!-- 수정하기 -->
          <div
            class="text-xs text-primary hover:underline"
            @click.stop="openModal(slot.key, selectedMealsData[slot.label])"
          >
            수정하기
          </div>
        </div>

        <!-- 식단 없음 -->
        <div v-else class="flex flex-col items-center justify-center py-6">
          <div class="w-14 h-14 rounded-full bg-muted/50 flex items-center justify-center mb-2">
            <Plus class="w-7 h-7 text-muted-foreground" />
          </div>
          <p class="text-sm text-muted-foreground mb-3">
            등록된 식단이 없습니다
          </p>

          <Button size="sm" @click.stop="openModal(slot.key)">
            <Plus class="w-4 h-4 mr-1" /> 식단 추가
          </Button>
        </div>
      </div>
    </div>
  </div>

  <MealRegistrationModal v-if="isRegistrationOpen" />
</template>
