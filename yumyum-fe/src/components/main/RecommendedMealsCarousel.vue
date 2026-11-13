<script setup>
import { ref } from 'vue';
import RecommendedMealDetailModal from "@/components/main/RecommendedMealDetailModal.vue";
import { Sparkles, Users, ArrowRight, ChevronLeft, ChevronRight } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import useMain from "@/stores/main/MainView";

const store = useMain();
const { recommendedMeals } = storeToRefs(store);
const handleViewDetail = store.recommendedHandleViewDetail;

// 스크롤 컨테이너 참조
const scrollContainer = ref(null);

// 좌우 스크롤 함수
const scroll = (direction) => {
  if (!scrollContainer.value) return;
  // 컨테이너 너비의 절반(카드 하나 정도)만큼 스크롤
  const scrollAmount = scrollContainer.value.clientWidth * 0.5 + 16; 
  scrollContainer.value.scrollBy({
    left: direction === 'left' ? -scrollAmount : scrollAmount,
    behavior: 'smooth'
  });
};
</script>

<template>
  <div class="h-full flex flex-col">
    <!-- 1. 헤더 및 네비게이션 (상단 고정) -->
    <div class="flex items-center justify-between mb-4 flex-shrink-0">
      <div>
        <div class="flex items-center gap-2 mb-1">
          <Sparkles class="w-5 h-5 text-emerald-500" />
          <h2 class="text-xl font-bold text-gray-900">AI 추천 식단</h2>
        </div>
        <p class="text-sm text-gray-500">목표 달성을 위한 최적의 식단</p>
      </div>
      
      <!-- 커스텀 네비게이션 버튼 -->
      <div class="flex gap-2">
        <button 
          @click="scroll('left')"
          class="w-9 h-9 rounded-full border border-gray-200 bg-white hover:bg-gray-50 text-gray-500 flex items-center justify-center transition-colors active:scale-95"
        >
          <ChevronLeft class="w-5 h-5" />
        </button>
        <button 
          @click="scroll('right')"
          class="w-9 h-9 rounded-full border border-gray-200 bg-white hover:bg-gray-50 text-gray-500 flex items-center justify-center transition-colors active:scale-95"
        >
          <ChevronRight class="w-5 h-5" />
        </button>
      </div>
    </div>

    <!-- 2. 카드 리스트 영역 (CSS Scroll Snap 사용) -->
    <!-- overflow-x-auto: 가로 스크롤 허용 -->
    <!-- snap-x snap-mandatory: 스크롤 시 카드가 딱딱 걸리게 함 -->
    <div 
      ref="scrollContainer"
      class="flex-1 flex gap-4 overflow-x-auto snap-x snap-mandatory scrollbar-hide min-h-0"
    >
      <div 
        v-for="meal in recommendedMeals" 
        :key="meal.id"
        class="min-w-[calc(100%-4px)] md:min-w-[calc(50%-8px)] h-full snap-start flex-shrink-0"
      >
        <!-- 내부 카드 디자인 (높이 100% 적용됨) -->
        <div 
          class="h-full flex flex-col bg-gray-50 rounded-2xl p-5 border border-gray-100 hover:border-emerald-200 transition-all cursor-pointer group relative" 
          @click="handleViewDetail(meal)"
        >
          <!-- 카드 헤더 -->
          <div class="flex justify-between items-start mb-4 flex-shrink-0">
            <div class="flex items-center gap-3 overflow-hidden">
              <div class="w-10 h-10 rounded-xl flex items-center justify-center shadow-sm flex-shrink-0 bg-white">
                <Sparkles v-if="meal.type === 'ai'" class="w-5 h-5 text-emerald-500" />
                <Users v-else class="w-5 h-5 text-blue-500" />
              </div>
              <div class="min-w-0">
                <span 
                  class="text-[10px] px-1.5 py-0.5 border rounded font-bold mb-0.5 inline-block"
                  :class="meal.type === 'ai' ? 'bg-white border-gray-200 text-emerald-600' : 'bg-white border-gray-200 text-blue-600'"
                >
                  {{ meal.type === 'ai' ? 'AI 추천' : '그룹장 추천' }}
                </span>
                <h3 class="text-base font-bold text-gray-900 leading-tight truncate">{{ meal.title }}</h3>
              </div>
            </div>
          </div>

          <!-- 영양소 그리드 -->
          <div class="grid grid-cols-4 gap-2 mb-4 flex-shrink-0">
            <div class="bg-white rounded-lg p-1.5 text-center border border-gray-100 shadow-sm">
              <div class="text-[10px] text-gray-400">Kcal</div>
              <div class="text-sm font-bold">{{ meal.calories }}</div>
            </div>
            <div class="bg-white rounded-lg p-1.5 text-center border border-gray-100 shadow-sm">
              <div class="text-[10px] text-gray-400">Carb</div>
              <div class="text-sm font-bold">{{ meal.carbs }}</div>
            </div>
            <div class="bg-white rounded-lg p-1.5 text-center border border-gray-100 shadow-sm">
              <div class="text-[10px] text-gray-400">Prot</div>
              <div class="text-sm font-bold">{{ meal.protein }}</div>
            </div>
            <div class="bg-white rounded-lg p-1.5 text-center border border-gray-100 shadow-sm">
              <div class="text-[10px] text-gray-400">Fat</div>
              <div class="text-sm font-bold">{{ meal.fat }}</div>
            </div>
          </div>

          <!-- 음식 목록 (스크롤 가능) -->
          <div class="flex-1 overflow-y-auto pr-1 mb-2 custom-scroll min-h-0">
            <h5 class="text-xs font-bold text-gray-500 mb-2 sticky top-0 bg-gray-50 pb-1">식단 구성</h5>
            <div class="space-y-2">
              <div 
                v-for="(food, index) in meal.foods" 
                :key="index"
                class="flex items-center justify-between text-sm bg-white p-2.5 rounded-lg border border-gray-100 shadow-sm group-hover:border-emerald-100 transition-colors"
              >
                <div class="flex items-center gap-2 overflow-hidden">
                  <div class="w-1.5 h-1.5 rounded-full bg-emerald-500 flex-shrink-0"></div>
                  <span class="font-medium text-gray-700 truncate">{{ food.name }}</span>
                </div>
                <span class="text-xs text-gray-500 font-medium flex-shrink-0">{{ food.amount }}{{ food.unit }}</span>
              </div>
            </div>
          </div>

          <!-- 상세보기 버튼 -->
          <div class="pt-2 border-t border-gray-200 border-dashed flex justify-end flex-shrink-0">
             <button 
                class="text-xs font-bold text-gray-400 group-hover:text-emerald-600 flex items-center gap-1 transition-colors"
              >
                상세보기 <ArrowRight class="w-3 h-3" />
              </button>
          </div>
        </div>
      </div>
    </div>

    <RecommendedMealDetailModal />
  </div>
</template>

<style scoped>
/* 스크롤바 숨기기 (깔끔한 UI) */
.scrollbar-hide::-webkit-scrollbar {
    display: none;
}
.scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
}

/* 내부 음식 목록 스크롤바 커스텀 */
.custom-scroll::-webkit-scrollbar {
  width: 3px;
}
.custom-scroll::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scroll::-webkit-scrollbar-thumb {
  background: #d1d5db; 
  border-radius: 10px;
}
.custom-scroll::-webkit-scrollbar-thumb:hover {
  background: #9ca3af; 
}
</style>