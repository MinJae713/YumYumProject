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
import { Coffee, Cookie, Edit, Flame, Moon, Pizza, Sun } from "lucide-vue-next";

const props = defineProps({
  isOpen: { type: Boolean, default: false },
  mealData: { type: Object, default: null },
  date: { type: [Date, String], default: () => new Date() },
});

const emit = defineEmits(["close", "edit"]);

const mealTypeConfig = {
  breakfast: { label: "아침", icon: Coffee, color: "text-[#f97316]", bgColor: "bg-[#f97316]/10" },
  lunch: { label: "점심", icon: Sun, color: "text-[#eab308]", bgColor: "bg-[#eab308]/10" },
  dinner: { label: "저녁", icon: Moon, color: "text-[#8b5cf6]", bgColor: "bg-[#8b5cf6]/10" },
  snack: { label: "간식", icon: Cookie, color: "text-[#ec4899]", bgColor: "bg-[#ec4899]/10" },
};
</script>

<template>
  <Dialog v-if="mealData" :open="isOpen" @update:open="(val) => !val && emit('close')">
    <DialogContent class="sm:max-w-[620px] max-h-[90vh] overflow-y-auto bg-white border border-gray-200 rounded-3xl shadow-lg">
      <DialogHeader>
        <div class="flex items-center gap-3 mb-2">
          <div
            class="w-12 h-12 rounded-2xl flex items-center justify-center border border-gray-100 shadow-sm"
            :class="mealTypeConfig[mealData.type]?.bgColor"
          >
            <component
              :is="mealTypeConfig[mealData.type]?.icon || Coffee"
              class="w-6 h-6"
              :class="mealTypeConfig[mealData.type]?.color"
            />
          </div>
          <div class="flex-1">
            <Badge variant="outline" class="mb-1">
              {{ mealTypeConfig[mealData.type]?.label || "식사" }}
            </Badge>
            <DialogTitle>식단 상세 정보</DialogTitle>
          </div>
        </div>
        <DialogDescription>
          {{
            new Date(date).toLocaleDateString("ko-KR", {
              month: "long",
              day: "numeric",
              weekday: "long",
            })
          }}
        </DialogDescription>
      </DialogHeader>

      <div class="space-y-6 py-4">
        <div class="bg-gray-50 rounded-2xl p-5 border border-gray-100">
          <div class="flex items-center gap-3 mb-4">
            <div class="w-12 h-12 bg-white rounded-xl flex items-center justify-center border border-gray-200 shadow-sm">
              <Flame class="w-6 h-6 text-emerald-500" />
            </div>
            <div>
              <div class="text-sm text-gray-500">총 칼로리</div>
              <div class="text-2xl font-bold text-emerald-600">{{ mealData.totalCalories }} kcal</div>
            </div>
          </div>
          <div class="space-y-2">
            <div class="flex items-center justify-between text-sm">
              <span class="text-gray-800">탄수화물</span>
              <span class="text-gray-600">{{ mealData.carbs }}g</span>
            </div>
            <div class="h-2 w-full bg-white rounded-full overflow-hidden border border-gray-100">
              <div
                class="h-full bg-blue-400 rounded-full transition-all"
                :style="{ width: (((mealData.carbs) / ((mealData.carbs + mealData.protein + mealData.fat) || 1)) * 100) + '%' }"
              ></div>
            </div>
            <div class="flex items-center justify-between text-sm">
              <span class="text-gray-800">단백질</span>
              <span class="text-gray-600">{{ mealData.protein }}g</span>
            </div>
            <div class="h-2 w-full bg-white rounded-full overflow-hidden border border-gray-100">
              <div
                class="h-full bg-green-500 rounded-full transition-all"
                :style="{ width: (((mealData.protein) / ((mealData.carbs + mealData.protein + mealData.fat) || 1)) * 100) + '%' }"
              ></div>
            </div>
            <div class="flex items-center justify-between text-sm">
              <span class="text-gray-800">지방</span>
              <span class="text-gray-600">{{ mealData.fat }}g</span>
            </div>
            <div class="h-2 w-full bg-white rounded-full overflow-hidden border border-gray-100">
              <div
                class="h-full bg-orange-400 rounded-full transition-all"
                :style="{ width: (((mealData.fat) / ((mealData.carbs + mealData.protein + mealData.fat) || 1)) * 100) + '%' }"
              ></div>
            </div>
          </div>
        </div>

        <div>
          <div class="flex items-center gap-2 mb-3">
            <Pizza class="w-5 h-5 text-primary" />
            <h4>식단 구성</h4>
          </div>
          <div class="space-y-2">
            <div
              v-for="(food, index) in mealData.foods"
              :key="index"
              class="flex items-center justify-between p-3 bg-white rounded-xl border border-gray-100"
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
          <Button variant="outline" class="flex-1 hover:bg-gray-100" @click="emit('close')">닫기</Button>
          <Button class="flex-1 bg-emerald-500 hover:bg-emerald-600 text-white" @click="emit('edit')">
            <Edit class="w-4 h-4 mr-2" />
            수정하기
          </Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>
