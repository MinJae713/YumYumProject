import { defineStore } from "pinia";
import { ref } from "vue";
import { useAdminFoodApi } from "@/lib/api/adminFood";

export const useAdminFoodStore = defineStore("adminFoodStore", () => {
  const adminFoodApi = useAdminFoodApi();

  const pendingList = ref([]);
  const searchResults = ref([]);
  const searchPagination = ref({ currentPage: 1, totalPage: 0, totalCount: 0 });
  const isLoading = ref(false);

  const normalizeList = (payload, fallbackKeys = []) => {
    if (Array.isArray(payload?.data)) return payload.data;
    for (const key of fallbackKeys) {
      if (Array.isArray(payload?.[key])) return payload[key];
    }
    if (Array.isArray(payload)) return payload;
    return [];
  };

  const fetchPendingList = async () => {
    isLoading.value = true;
    try {
      const response = await adminFoodApi.getPendingListApi();
      const data = response?.data ?? response ?? [];
      pendingList.value = normalizeList(data, ["pendingList", "items", "list"]);
    } catch (error) {
      console.error("매칭 대기 목록 조회 실패:", error);
      pendingList.value = [];
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const searchFoods = async (keyword, page = 1, size = 10) => {
    isLoading.value = true;
    try {
      const response = await adminFoodApi.searchFoodsApi(keyword, page, size);
      const payload = response?.data ?? response ?? {};

      searchResults.value = normalizeList(payload, ["foods", "results", "content", "items", "list"]);

      const paginationSource = payload?.page ?? payload?.pagination ?? payload ?? {};
      searchPagination.value = {
        currentPage: paginationSource.currentPage ?? paginationSource.page ?? page,
        totalPage: paginationSource.totalPage ?? paginationSource.totalPages ?? paginationSource.total ?? 0,
        totalCount: paginationSource.totalCount ?? paginationSource.totalElements ?? paginationSource.count ?? searchResults.value.length ?? 0,
      };
    } catch (error) {
      console.error("음식 검색 실패:", error);
      searchResults.value = [];
      searchPagination.value = { currentPage: 1, totalPage: 0, totalCount: 0 };
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const resolvePendingItem = (itemOrIndex) => {
    if (typeof itemOrIndex === "number") return pendingList.value[itemOrIndex];
    if (itemOrIndex && typeof itemOrIndex === "object") return itemOrIndex;
    return null;
  };

  const applyMatch = (itemOrIndex, foodId) => {
    const target = resolvePendingItem(itemOrIndex);
    if (!target) return;
    target.foodId = foodId;
    target.status = "MATCHED";
  };

  const resetMatch = (itemOrIndex) => {
    const target = resolvePendingItem(itemOrIndex);
    if (!target) return;
    target.foodId = null;
    target.status = "PENDING";
  };

  const matchFoods = async (matches) => {
    if (!Array.isArray(matches) || matches.length === 0) return;
    isLoading.value = true;
    try {
      await adminFoodApi.matchFoodsApi(matches);
      await fetchPendingList();
      if (typeof window !== "undefined" && window?.alert) {
        window.alert("매칭이 완료되었습니다.");
      } else {
        console.log("매칭이 완료되었습니다.");
      }
    } catch (error) {
      console.error("음식 매칭 실패:", error);
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const saveMatches = async () => {
    const matches = pendingList.value
      .filter((item) => item?.foodId && item?.status === "MATCHED")
      .map((item) => ({
        aiFoodName: item.foodName,
        foodId: item.foodId,
      }));

    if (!matches.length) {
      const error = new Error("저장할 매칭 항목이 없습니다.");
      if (typeof window !== "undefined" && window?.alert) {
        window.alert(error.message);
      }
      throw error;
    }

    await matchFoods(matches);
  };

  return {
    pendingList,
    searchResults,
    searchPagination,
    isLoading,
    fetchPendingList,
    searchFoods,
    applyMatch,
    resetMatch,
    matchFoods,
    saveMatches,
  };
});
