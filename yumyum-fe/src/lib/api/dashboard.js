import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useDashboardApi = defineStore('dashboardApi', () => {
  const domain = 'dashboard';

  // 대시보드 메인 페이지 데이터
  const getDashboardPageApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}`
    );
    return response.data;
  };

  // 최근 획득 배지 목록
  const getRecentBadgeListApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/badge`
    );
    return response.data;
  };

  // 체중 변화량
  const getWeightDeltaApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/weight`
    );
    return response.data;
  };

  // 주간 일일 평균 영양 섭취율
  const getWeeklyDailyNutrionIntakeRateApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/nutrition`
    );
    return response.data;
  };

  // 권장 섭취량 대비 오늘 섭취량
  const getDailyIntakeRelativeToRecommendedApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/nutrition/compare`
    );
    return response.data;
  };

  // 일일 평균 소모 칼로리
  const getDailyAverageSpendedCalorieApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/exercise/average`
    );
    return response.data;
  };

  // 주간 총 운동 시간
  const getWeeklyTotalDurationApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/exercise/time`
    );
    return response.data;
  };

  // 스트릭 기록 조회
  const getStreakRecordApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/streak`
    );
    return response.data;
  };

  return {
    getDashboardPageApi,
    getRecentBadgeListApi,
    getWeightDeltaApi,
    getWeeklyDailyNutrionIntakeRateApi,
    getDailyIntakeRelativeToRecommendedApi,
    getDailyAverageSpendedCalorieApi,
    getWeeklyTotalDurationApi,
    getStreakRecordApi
  };
});
