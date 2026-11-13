import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useMealApi = defineStore('mealApi', () => {
  const domain = 'meal';
  // 데이터 접근은 아래 함수 반환한거 .data로 ㄱㄴ
  
  const getMealOfDateApi = async (mealDate) => {
    const response = await axios.get(`${contextPath}/${domain}`, {
      params: {
        mealDate
      }
    });
    return response.data;
  }
  const addMealApi = async (addMealRequest) => {
    const response = await axios.post(`${contextPath}/${domain}`, addMealRequest);
    return response.data;
  }
  const getMealDetailApi = async (mealId) => {
    const response = await axios.get(`${contextPath}/${domain}/${mealId}`, {});
    return response.data;
  }
  const updateMealApi = async (updateMealRequest) => {
    const response = await axios.patch(`${contextPath}/${domain}`, updateMealRequest);
    return response.data;
  }
  const deleteMealApi = async (mealId) => {
    const response = await axios.delete(`${contextPath}/${domain}/${mealId}`, {});
    return response.data;
  }
  const applyRecommendMealApi = async (applyRecommendMealRequest) => {
    const response = await axios.patch(`${contextPath}/${domain}/apply-recommend`, applyRecommendMealRequest);
    return response.data;
  }
  const searchFoodApi = async (foodName) => {
    const response = await axios.get(`${contextPath}/${domain}/search`, {
      params: {
        foodName
      }
    });
    return response.data;
  }
  const totalFoodApi = async (totalFoodRequests) => {
    const response = await axios.post(`${contextPath}/${domain}/total`, totalFoodRequests);
    return response.data;
  }
  const getMonthRecordApi = async (year, month) => {
    const response = await axios.get(`${contextPath}/${domain}/month-record`, {
      params: {
        year, month
      }
    });
    return response.data;
  }
  const addRecommendMealApi = async (userId, addMealRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/recommend/${userId}`, addMealRequest);
    return response.data;
  }
  return {
    getMealOfDateApi,
    addMealApi,
    getMealDetailApi,
    updateMealApi,
    deleteMealApi,
    applyRecommendMealApi,
    searchFoodApi,
    totalFoodApi,
    getMonthRecordApi,
    addRecommendMealApi
  }
});