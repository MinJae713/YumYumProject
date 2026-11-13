import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useResearchApi = defineStore('researchApi', () => {
  const domain = 'research';
  // 데이터 접근은 아래 함수 반환한거 .data로 ㄱㄴ

  // 데일리 리서치 제출
  const submitResearchApi = async (submitRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/submit`, submitRequest);
    return response.data;
  };

  return {
    submitResearchApi
  };
});
