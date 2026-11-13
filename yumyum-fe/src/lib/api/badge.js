import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useBadgeApi = defineStore('badgeApi', () => {
  const domain = 'badge';
  // 데이터 접근은 아래 함수 반환한거 .data로 ㄱㄴ
  const getBadgeListApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}`, {});
    return response.data;
  }
  return {
    getBadgeListApi
  }
});