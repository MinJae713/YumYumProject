<script setup>
import { ref } from "vue";
import { Progress } from "@/components/ui/progress"; // 또는 커스텀 progress 사용 가능
import useMain from "@/stores/main/MainView";
import { storeToRefs } from "pinia";
import { ChevronLeft, ChevronRight, TrendingUp } from "lucide-vue-next";

const store = useMain();
const { dailyGoals } = storeToRefs(store);
const getRate = store.getRate;

// 스크롤 컨테이너 참조
const scrollContainer = ref(null);

// 좌우 스크롤 함수
const scroll = (direction) => {
  if (!scrollContainer.value) return;
  // 컨테이너 너비만큼 스크롤 (한 페이지씩 이동)
  const scrollAmount = scrollContainer.value.clientWidth;
  scrollContainer.value.scrollBy({
    left: direction === "left" ? -scrollAmount : scrollAmount,
    behavior: "smooth",
  });
};
</script>

<template>
  <div class="h-full flex flex-col">
    <!-- 헤더 -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center gap-2">
        <TrendingUp class="w-5 h-5 text-emerald-500" />
        <h3 class="text-lg font-semibold text-gray-900">오늘의 달성률</h3>
      </div>
      <!-- 페이지 인디케이터 대신 좌우 버튼 배치 -->
      <div class="flex gap-1">
        <button
          class="w-7 h-7 rounded-full border border-gray-200 bg-white hover:bg-gray-50 flex items-center justify-center text-gray-400 hover:text-emerald-600 transition-all active:scale-95"
          @click="scroll('left')"
        >
          <ChevronLeft class="w-4 h-4" />
        </button>
        <button
          class="w-7 h-7 rounded-full border border-gray-200 bg-white hover:bg-gray-50 flex items-center justify-center text-gray-400 hover:text-emerald-600 transition-all active:scale-95"
          @click="scroll('right')"
        >
          <ChevronRight class="w-4 h-4" />
        </button>
      </div>
    </div>

    <!-- 스크롤 컨테이너 (CSS Scroll Snap) -->
    <!-- overflow-x-auto, snap-x mandatory 적용 -->
    <div
      ref="scrollContainer"
      class="flex-1 flex overflow-x-auto snap-x snap-mandatory scrollbar-hide"
    >
      <!-- 3개씩 묶어서 페이지 단위로 렌더링하려면 데이터를 청크로 나눠야 하지만,
           여기서는 간단하게 각 항목을 100% 너비의 페이지에 3개씩 배치하는 구조 대신,
           그냥 리스트 전체를 스크롤하게 하거나,
           CSS Grid로 3개씩 딱딱 끊어지게 배치하는 것이 좋습니다.
           
           아래 방식: 항목들을 3개씩 묶은 '페이지(Page)' 단위로 렌더링 -->
      
      <!-- v-for로 3개씩 묶은 그룹을 만듭니다. (임시 로직: 데이터가 객체이므로 키 배열 변환 필요) -->
      <div
        v-for="(chunk, pageIndex) in [
          Object.keys(dailyGoals || {}).slice(0, 3),
          Object.keys(dailyGoals || {}).slice(3, 6)
        ]"
        :key="pageIndex"
        class="min-w-full h-full snap-start flex flex-col gap-4 px-1"
        v-show="chunk.length > 0" 
      >
        <div 
          v-for="key in chunk" 
          :key="key" 
          class="space-y-2 bg-white/50 rounded-xl"
        >
          <div class="flex items-center justify-between text-sm">
            <span class="font-medium text-gray-700">{{ dailyGoals[key].label }}</span>
            <span class="text-xs text-gray-400">
              {{ dailyGoals[key].current }} / {{ dailyGoals[key].target }}{{ dailyGoals[key].unitName }}
            </span>
          </div>
          
          <!-- 커스텀 프로그래스 바 (회색 배경 + 초록 바) -->
          <div class="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
            <div
              class="h-full bg-emerald-500 transition-all duration-500 rounded-full"
              :style="{ width: `${getRate(key)}%` }"
            ></div>
          </div>
          
          <div class="text-right text-xs text-emerald-600 font-bold">
            {{ dailyGoals[key].rate }}%
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.scrollbar-hide::-webkit-scrollbar {
  display: none;
}
.scrollbar-hide {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>