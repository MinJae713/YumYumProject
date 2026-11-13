import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useMainApi = defineStore('main', () => {
  const domain = 'main';
  // 데이터 접근은 아래 함수 반환한거 .data로 ㄱㄴ
  
  const getMainPageApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}`, {});
    return response.data;
  }
  const getRecommendMealApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}/recommend`, {});
    return response.data;
  }
  const getMealOfDateApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}/meal`, {});
    return response.data;
  }
  const getExercisesOfDateApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}/exercise`, {});
    return response.data;
  }
  const getIntakeOfDateApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}/meal/intake`, {});
    return response.data;
  }
  const getStreakRecordApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}/badge/streak`, {});
    return response.data;
  }
  return {
    getMainPageApi,
    getRecommendMealApi,
    getMealOfDateApi,
    getExercisesOfDateApi,
    getIntakeOfDateApi,
    getStreakRecordApi
  }
});