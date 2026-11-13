import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useExerciseApi = defineStore('exerciseApi', () => {
  const domain = 'exercise';
  // 데이터 접근은 아래 함수 반환한거 .data로 ㄱㄴ

  // 날짜별 운동 기록 조회
  const getExercisesOfDateApi = async (exerciseDate) => {
    const response = await axios.get(`${contextPath}/${domain}`, {
      params: {
        exerciseDate
      }
    });
    return response.data;
  };

  // 운동 기록 상세 조회
  const getExerciseDetailApi = async (exerciseRecordId) => {
    const response = await axios.get(`${contextPath}/${domain}/${exerciseRecordId}`);
    return response.data;
  };

  // 운동 카테고리별 운동 목록 조회
  const getExerciseListApi = async (exerciseCategoryId) => {
    const response = await axios.get(`${contextPath}/${domain}/list/${exerciseCategoryId}`);
    return response.data;
  };

  // 운동 기록 추가
  const addExerciseApi = async (addExerciseRequest) => {
    const response = await axios.post(`${contextPath}/${domain}`,addExerciseRequest);
    return response.data;
  };

  // 운동 기록 수정
  const updateExerciseApi = async (updateExerciseRequest) => {
    const response = await axios.patch(`${contextPath}/${domain}`,updateExerciseRequest);
    return response.data;
  };

  // 운동 기록 삭제
  const deleteExerciseApi = async (exerciseRecordId) => {
    const response = await axios.delete(`${contextPath}/${domain}/${exerciseRecordId}`);
    return response.data;
  };

  // 운동 점수 계산
  const scoreExerciseApi = async (scoreExerciseRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/score`,scoreExerciseRequest);
    return response.data;
  };

  // 월별 운동 기록 조회
  const getMonthRecordApi = async (year, month) => {
    const response = await axios.get(`${contextPath}/${domain}/month-record`,{
      params: {
        year,
        month
      }
    });
    return response.data;
  };

  return {
    getExercisesOfDateApi,
    getExerciseDetailApi,
    getExerciseListApi,
    addExerciseApi,
    updateExerciseApi,
    deleteExerciseApi,
    scoreExerciseApi,
    getMonthRecordApi
  };
});
