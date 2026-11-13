import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useAdminFoodApi = defineStore('adminFoodApi', () => {
  const baseUrl = `${contextPath}/admin/food`;

  // 매칭 대기 목록 조회
  const getPendingListApi = async () => {
    const response = await axios.get(`${baseUrl}/pendingList`);
    return response.data;
  };

  // 음식 검색
  const searchFoodsApi = async (keyword, page, size) => {
    const response = await axios.get(`${baseUrl}/search`, {
      params: { keyword, page, size },
    });
    return response.data;
  };

  // AI 음식명 - DB 음식 매칭
  const matchFoodsApi = async (matchRequestList) => {
    const response = await axios.post(`${baseUrl}/match`, matchRequestList);
    return response.data;
  };

  return {
    getPendingListApi,
    searchFoodsApi,
    matchFoodsApi
  };
});
