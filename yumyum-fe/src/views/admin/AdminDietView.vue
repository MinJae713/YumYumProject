<script setup>
import { onMounted } from "vue";
import { storeToRefs } from "pinia";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge"; // Badge 추가
import { useAdminDietStore } from "@/stores/admin/AdminDietStore";

const store = useAdminDietStore();
const { dietList, pagination, isLoading } = storeToRefs(store);

const pageSize = 10;

const formatDateTime = (value) => {
  if (!value) return "-";
  const date = new Date(value);
  // 날짜만 보여줄지, 시간까지 보여줄지 결정 (여기선 날짜만)
  return Number.isNaN(date.getTime()) ? value : date.toLocaleDateString();
};

const displayIndex = (idx) => {
  const page = pagination.value.currentPage || 1;
  return (page - 1) * pageSize + idx + 1;
};

const handleFetch = async (page = 1) => {
  await store.fetchDietList(page, pageSize);
};

const handlePrevPage = () => {
  const prev = (pagination.value.currentPage || 1) - 1;
  if (prev < 1) return;
  handleFetch(prev);
};

const handleNextPage = () => {
  const next = (pagination.value.currentPage || 1) + 1;
  if (next > (pagination.value.totalPage || 1)) return;
  handleFetch(next);
};

onMounted(() => {
  handleFetch(1);
});
</script>

<template>
  <div class="space-y-6">
    <section class="bg-white rounded-lg shadow border border-slate-200">
      <div class="flex items-center justify-between px-6 py-4 border-b border-slate-200">
        <h2 class="text-lg font-semibold text-slate-900">AI 추천 식단 조회</h2>
        <span class="text-sm text-slate-500">총 {{ pagination.totalCount || 0 }}건</span>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full text-sm text-left">
          <thead class="bg-slate-100 text-slate-700">
            <tr>
              <th class="px-4 py-3 w-16">No</th>
              <th class="px-4 py-3 w-48">유저 정보</th>
              <th class="px-4 py-3 w-32">추천 날짜</th>
              <th class="px-4 py-3">메뉴 구성</th>
              <th class="px-4 py-3 w-64">영양소 정보</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, idx) in dietList"
              :key="item.mealId || idx"
              class="hover:bg-slate-50 border-t border-slate-100"
            >
              <td class="px-4 py-3 text-slate-500">{{ displayIndex(idx) }}</td>
              
              <td class="px-4 py-3">
                <div class="flex items-center gap-3">
                  <Avatar class="h-9 w-9">
                    <AvatarImage src="" /> 
                    <AvatarFallback>{{ item.userName?.[0] || "?" }}</AvatarFallback>
                  </Avatar>
                  <div class="flex flex-col">
                    <span class="font-medium text-slate-900">{{ item.userName || "알 수 없음" }}</span>
                    <span class="text-xs text-slate-400">ID: {{ item.userId }}</span>
                  </div>
                </div>
              </td>

              <td class="px-4 py-3 text-slate-700 font-medium">
                {{ formatDateTime(item.mealDate) }}
              </td>

              <td class="px-4 py-3">
                <div class="flex flex-wrap gap-1">
                  <Badge 
                    v-for="(food, fIdx) in item.foodNames" 
                    :key="fIdx" 
                    variant="secondary"
                    class="font-normal text-slate-700 bg-slate-100 hover:bg-slate-200"
                  >
                    {{ food }}
                  </Badge>
                  <span v-if="!item.foodNames?.length" class="text-slate-400">-</span>
                </div>
              </td>

              <td class="px-4 py-3">
                <div class="space-y-1 text-xs">
                  <div class="flex items-center gap-2">
                    <span class="text-orange-500 font-bold">🔥 {{ item.totalKcal ?? 0 }} kcal</span>
                  </div>
                  <div class="flex items-center gap-2 text-slate-500">
                    <span>🍚 {{ item.totalCarbo ?? 0 }}g</span>
                    <span class="text-slate-300">|</span>
                    <span>🥩 {{ item.totalProtein ?? 0 }}g</span>
                    <span class="text-slate-300">|</span>
                    <span>🧀 {{ item.totalFat ?? 0 }}g</span>
                  </div>
                </div>
              </td>
            </tr>
            <tr v-if="!dietList.length">
              <td colspan="5" class="px-4 py-10 text-center text-slate-500">
                추천 식단 데이터가 없습니다.
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="flex items-center justify-between px-6 py-4 border-t border-slate-200 text-sm text-slate-700">
        <div>
          <span class="text-slate-500">Page</span>
          <span class="font-medium ml-1">{{ pagination.currentPage || 1 }} / {{ pagination.totalPage || 1 }}</span>
        </div>
        <div class="flex items-center gap-2">
          <Button
            variant="outline"
            size="sm"
            :disabled="isLoading || (pagination.currentPage || 1) <= 1"
            @click="handlePrevPage"
          >
            이전
          </Button>
          <Button
            variant="outline"
            size="sm"
            :disabled="isLoading || (pagination.currentPage || 1) >= (pagination.totalPage || 1)"
            @click="handleNextPage"
          >
            다음
          </Button>
        </div>
      </div>
    </section>
  </div>
</template>