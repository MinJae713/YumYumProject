<script setup>
import { onMounted, ref } from "vue";
import { storeToRefs } from "pinia";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useAdminFoodStore } from "@/stores/admin/AdminFoodStore";
import { RotateCcw } from "lucide-vue-next";

const store = useAdminFoodStore();
const { pendingList, searchResults, searchPagination, isLoading } = storeToRefs(store);

const keyword = ref("");
const selectedPendingIndex = ref(null);

const handlePendingClick = async (index, aiFoodName) => {
  selectedPendingIndex.value = index;
  keyword.value = aiFoodName || "";
  await store.searchFoods(keyword.value, 1, 10);
};

const handleSelectFood = (foodId) => {
  if (selectedPendingIndex.value === null) {
    alert("매칭할 항목을 먼저 선택하세요");
    return;
  }
  store.applyMatch(selectedPendingIndex.value, foodId);
};

const handleResetMatch = (item) => {
  store.resetMatch(item);
};

const handleSave = async () => {
  try {
    await store.saveMatches();
  } catch (error) {
    console.error("매칭 저장 실패:", error);
  }
};

const handleSearch = async (page = 1) => {
  await store.searchFoods(keyword.value, page, 10);
};

const handlePrevPage = () => {
  const prev = (searchPagination.value.currentPage || 1) - 1;
  if (prev < 1) return;
  handleSearch(prev);
};

const handleNextPage = () => {
  const next = (searchPagination.value.currentPage || 1) + 1;
  if (next > (searchPagination.value.totalPage || 1)) return;
  handleSearch(next);
};

onMounted(() => {
  store.fetchPendingList();
});
</script>

<template>
  <div class="space-y-6">
    <section class="bg-white rounded-lg shadow border border-slate-200">
      <div class="flex items-center justify-between px-6 py-4 border-b border-slate-200">
        <h2 class="text-lg font-semibold text-slate-900">매칭 대기 목록</h2>
        <Button :disabled="isLoading" @click="handleSave">저장</Button>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full text-sm text-left">
          <thead class="bg-slate-100 text-slate-700">
            <tr>
              <th class="px-4 py-3 w-1/3">AI 음식명</th>
              <th class="px-4 py-3 w-1/6">상태</th>
              <th class="px-4 py-3 w-1/3">Food ID</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, idx) in pendingList"
              :key="item.aiFoodId || idx"
              class="cursor-pointer transition-colors"
              :class="selectedPendingIndex === idx ? 'bg-blue-50 border-l-4 border-l-blue-500' : 'hover:bg-slate-50'"
              @click="handlePendingClick(idx, item.foodName)"
            >
              <td class="px-4 py-3 border-t border-slate-100 font-medium">
                {{ item.foodName }}
              </td>
              <td class="px-4 py-3 border-t border-slate-100">
                <template v-if="item.status === 'MATCHED'">
                  <span
                    class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
                  >
                    MATCHED
                  </span>
                  <Button
                    size="icon"
                    variant="ghost"
                    class="ml-2 h-8 w-8"
                    @click.stop="handleResetMatch(item)"
                    title="매칭 취소"
                  >
                    <RotateCcw class="w-4 h-4 text-slate-500" />
                  </Button>
                </template>
                <span
                  v-else
                  class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800"
                >
                  {{ item.status || 'PENDING' }}
                </span>
              </td>
              <td class="px-4 py-3 border-t border-slate-100">
                <Input 
                  v-model="item.foodId" 
                  placeholder="ID 입력 or 아래 검색 선택" 
                  class="h-8 bg-white"
                  @click.stop
                  @input="(value) => store.applyMatch(item, value?.target ? value.target.value : value)"
                />
              </td>
            </tr>
            <tr v-if="!pendingList.length">
              <td colspan="3" class="px-4 py-6 text-center text-slate-500">
                매칭 대기 항목이 없습니다.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section class="bg-white rounded-lg shadow border border-slate-200">
      <div class="flex flex-col gap-4 px-6 py-4 border-b border-slate-200 md:flex-row md:items-center md:justify-between">
        <h2 class="text-lg font-semibold text-slate-900">음식 DB 검색</h2>
        <div class="flex items-center gap-2 w-full md:w-auto">
          <Input
            v-model="keyword"
            placeholder="음식명을 입력하세요"
            class="w-full md:w-64"
            @keyup.enter="handleSearch(1)"
          />
          <Button :disabled="isLoading" @click="handleSearch(1)">검색</Button>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full text-sm text-left">
          <thead class="bg-slate-100 text-slate-700">
            <tr>
              <th class="px-4 py-3 w-24">ID</th>
              <th class="px-4 py-3">이름</th>
              <th class="px-4 py-3 w-32">칼로리</th>
              <th class="px-4 py-3 w-40">1회 제공량</th>
              <th class="px-4 py-3 w-24 text-right">선택</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="item in searchResults"
              :key="item.foodId"
              class="hover:bg-slate-50"
            >
              <td class="px-4 py-3 border-t border-slate-100 text-slate-500">{{ item.foodId }}</td>
              <td class="px-4 py-3 border-t border-slate-100 font-medium">{{ item.foodName }}</td>
              <td class="px-4 py-3 border-t border-slate-100">{{ item.energy ?? item.calorie ?? '-' }} kcal</td>
              <td class="px-4 py-3 border-t border-slate-100">
                <span class="text-slate-700">{{ item.servingSize ?? item.amount ?? '-' }}</span>
                <span class="text-slate-500 ml-1">{{ item.unitName ?? item.servingUnit ?? '' }}</span>
              </td>
              <td class="px-4 py-3 border-t border-slate-100 text-right">
                <Button size="sm" variant="secondary" @click="handleSelectFood(item.foodId)">
                  선택
                </Button>
              </td>
            </tr>
            <tr v-if="!searchResults.length">
              <td colspan="5" class="px-4 py-12 text-center text-slate-500">
                검색 결과가 없습니다.
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="flex items-center justify-between px-6 py-4 border-t border-slate-200 text-sm text-slate-700">
        <div>
          <span class="text-slate-500">Total</span>
          <span class="font-medium ml-1">{{ searchPagination.totalCount || 0 }}</span>
        </div>
        <div class="flex items-center gap-2">
          <Button 
            variant="outline" 
            size="sm" 
            :disabled="(searchPagination.currentPage || 1) <= 1" 
            @click="handlePrevPage"
          >
            이전
          </Button>
          <span class="px-2 text-slate-500">
            {{ searchPagination.currentPage || 1 }} / {{ searchPagination.totalPage || 1 }}
          </span>
          <Button
            variant="outline"
            size="sm"
            :disabled="(searchPagination.currentPage || 1) >= (searchPagination.totalPage || 1)"
            @click="handleNextPage"
          >
            다음
          </Button>
        </div>
      </div>
    </section>
  </div>
</template>
