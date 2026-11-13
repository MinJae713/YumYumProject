import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useUserApi = defineStore('userApi', () => {
  const domain = 'user';

  // 회원가입
  const joinApi = async (joinRequest) => {
    const response = await axios.post(
      `${contextPath}/${domain}/join`,
      joinRequest
    );
    return response.data;
  };

  // 로그인
  const loginApi = async (loginRequest) => {
    const response = await axios.post(
      `${contextPath}/${domain}/login`,
      loginRequest
    );
    return response.data;
  };

  // AccessToken 재발급
  const reissueTokenApi = async () => {
    const response = await axios.post(
      `${contextPath}/${domain}/refresh`
    );
    return response.data;
  };

  // 로그아웃
  const logoutApi = async () => {
    const response = await axios.post(
      `${contextPath}/${domain}/logout`
    );
    return response.data;
  };

  // 유저 정보 조회
  const getUserDetailApi = async () => {
    const response = await axios.get(
      `${contextPath}/${domain}/detail`
    );
    return response.data;
  };

  // 유저 정보 수정
  const updateUserInfoApi = async (userUpdateRequest) => {
    const response = await axios.patch(
      `${contextPath}/${domain}/update`,
      userUpdateRequest
    );
    return response.data;
  };

  // 비밀번호 변경
  const changePasswordApi = async (changePasswordRequest) => {
    const response = await axios.patch(
      `${contextPath}/${domain}/update-password`,
      changePasswordRequest
    );
    return response.data;
  };

  // 회원 탈퇴 (refreshToken 쿠키도 서버에서 삭제)
  const deleteUserApi = async () => {
    const response = await axios.delete(
      `${contextPath}/${domain}`
    );
    return response.data;
  };

  // 초기 리서치 정보 저장
  const updateUserResearchApi = async (userResearchRequest) => {
    const response = await axios.patch(
      `${contextPath}/${domain}/research`,
      userResearchRequest
    );
    return response.data;
  };

  // 이메일로 유저 검색
  const searchUserByEmailApi = async (email) => {
    const response = await axios.get(
      `${contextPath}/${domain}/search`,
      {
        params: { email }
      }
    );
    return response.data;
  };

  // 체중 기록 추가 / 수정
  const updateUserWeightApi = async (updateUserWeightRequest) => {
    const response = await axios.post(
      `${contextPath}/${domain}/weight`,
      updateUserWeightRequest
    );
    return response.data;
  };

  // 유저 역할 변경
  const changeUserRoleApi = async (changeUserRoleRequest) => {
    const response = await axios.patch(
      `${contextPath}/${domain}/change-role`,
      changeUserRoleRequest
    );
    return response.data;
  };

  return {
    joinApi,
    loginApi,
    reissueTokenApi,
    logoutApi,
    getUserDetailApi,
    updateUserInfoApi,
    changePasswordApi,
    deleteUserApi,
    updateUserResearchApi,
    searchUserByEmailApi,
    updateUserWeightApi,
    changeUserRoleApi
  };
});
