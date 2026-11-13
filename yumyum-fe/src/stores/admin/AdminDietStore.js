import { defineStore } from "pinia";
import { ref } from "vue";
import { useAdminDietApi } from "@/lib/api/adminDiet";

export const useAdminDietStore = defineStore("adminDietStore", () => {
  const adminDietApi = useAdminDietApi();

  const dietList = ref([]);
  const pagination = ref({ currentPage: 1, totalPage: 0, totalCount: 0 });
  const isLoading = ref(false);

  const fetchDietList = async (page = 1, size = 10) => {
    isLoading.value = true;
    try {
      const response = await adminDietApi.getDietListApi(page, size);
      // ApiResponse 구조: { status: "OK", data: { list: [], totalCount: ... } }
      // 실제 데이터 객체 추출
      const data = response?.data ?? response ?? {};

      // ✅ 백엔드 DTO의 "list" 키와 매핑
      dietList.value = Array.isArray(data.list) ? data.list : [];

      // ✅ 백엔드 DTO의 페이지네이션 정보 매핑
      pagination.value = {
        currentPage: data.currentPage ?? page,
        totalPage: data.totalPage ?? 0,
        totalCount: data.totalCount ?? 0,
      };
    } catch (error) {
      console.error("식단 목록 조회 실패:", error);
      dietList.value = [];
      pagination.value = { currentPage: 1, totalPage: 0, totalCount: 0 };
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  return {
    dietList,
    pagination,
    isLoading,
    fetchDietList,
  };
});