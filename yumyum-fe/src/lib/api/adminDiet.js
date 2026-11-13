import axios, { contextPath } from "@/lib/axios";
import { defineStore } from "pinia";

export const useAdminDietApi = defineStore("adminDietApi", () => {
  // ✅ 백엔드 컨트롤러 경로와 일치시킴 (/admin/diet -> /admin/ai-meal)
  const baseUrl = `${contextPath}/admin/ai-meal`;

  const getDietListApi = async (page, size) => {
    const response = await axios.get(`${baseUrl}/list`, {
      params: { page, size },
    });
    return response.data;
  };

  return {
    getDietListApi,
  };
});