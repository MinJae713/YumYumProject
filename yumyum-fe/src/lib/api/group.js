import axios, { contextPath } from '@/lib/axios';
import { defineStore } from 'pinia';

export const useGroupApi = defineStore('groupApi', () => {
  const domain = 'group';
  // 데이터 접근은 아래 함수 반환한거 .data로 ㄱㄴ

  // ----------------------- 그룹 ---------------------------- //

  // 그룹 생성
  const createGroupApi = async (groupCreateRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/create`, groupCreateRequest);
    return response.data;
  };

  // 그룹 목록 조회
  const getGroupListApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}/group-list`);
    return response.data;
  };

  // 그룹 이름 수정
  const updateGroupNameApi = async (groupId, groupUpdateRequest) => {
    const response = await axios.put(`${contextPath}/${domain}/${groupId}/group-update`, groupUpdateRequest);
    return response.data;
  };

  // 그룹 삭제
  const deleteGroupApi = async (groupId) => {
    const response = await axios.delete(`${contextPath}/${domain}/${groupId}`);
    return response.data;
  };

  // ----------------------- 그룹 페이지 ---------------------------- //

  // 그룹 랭킹 조회
  const getGroupMemberRankingApi = async (groupId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/ranking`);
    return response.data;
  };

  // 그룹 멤버 랭킹 조회 (별도 사용용)
  const getGroupRankingApi = async (groupId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/ranking`);
    return response.data;
  };

  // 그룹 멤버 목록 조회
  const getGroupMemberListApi = async (groupId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/group-members`);
    return response.data;
  };

  // 그룹 일별 기록 조회
  const getDailyLogsApi = async (groupId, date, groupMemberId = null) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/dailyLog`,{
      params: {
        date,
        groupMemberId
      }
    });
    return response.data;
  };

  // 그룹 식단 상세 조회
  const getGroupMealDetailApi = async (groupId, mealId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/meal/${mealId}/detail`);
    return response.data;
  };

  // 그룹 운동 상세 조회
  const getGroupExerciseDetailApi = async (groupId, exerciseRecordId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/exercise/${exerciseRecordId}/detail`);
    return response.data;
  };

  // 그룹장용 - 특정 그룹원 기록 날짜 조회
  const getRecordedDatesForLeader = async (groupId, groupMemberId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/recordedDate`, {
      params: { groupMemberId },
    });
    return response.data?.data;
  };

  // 그룹원용 - 본인 기록 날짜 조회
  const getRecordedDatesForMember = async (groupId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/recordedDate`);
    return response.data?.data;
  };

  // ----------------------- 그룹 멤버 ---------------------------- //

  // 그룹 멤버 초대
  const inviteGroupMemberApi = async (groupId, groupInviteRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/${groupId}/invite`, groupInviteRequest);
    return response.data;
  };

  // 초대 목록 조회
  const getInviteListApi = async () => {
    const response = await axios.get(`${contextPath}/${domain}/invite-list`);
    return response.data;
  };

  // 초대 수락 / 거절
  const responseToInviteApi = async (groupInviteActionRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/invite-list`, groupInviteActionRequest);
    return response.data;
  };

  // 그룹 탈퇴
  const withdrawGroupMemberApi = async (groupId) => {
    const response = await axios.delete(`${contextPath}/${domain}/${groupId}/group-member`);
    return response.data;
  };

  // 그룹 멤버 강퇴
  const expelGroupMemberApi = async (groupId, groupMemberId) => {
    const response = await axios.delete(`${contextPath}/${domain}/${groupId}/group-member/${groupMemberId}`);
    return response.data;
  };

  // ----------------------- 피드백 ---------------------------- //

  // 식단 피드백 생성
  const createMealFeedbackApi = async (groupId, feedbackCreateRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/${groupId}/meal/feedback`, feedbackCreateRequest);
    return response.data;
  };

  // 운동 피드백 생성
  const createExerciseFeedbackApi = async (groupId, feedbackCreateRequest) => {
    const response = await axios.post(`${contextPath}/${domain}/${groupId}/exercise/feedback`, feedbackCreateRequest);
    return response.data;
  };

  // 식단 피드백 조회
  const getMealFeedbackApi = async (groupId, mealId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/meal/${mealId}/feedback`);
    return response.data;
  };

  // 운동 피드백 조회
  const getExerciseFeedbackApi = async (groupId, exerciseRecordId) => {
    const response = await axios.get(`${contextPath}/${domain}/${groupId}/exercise/${exerciseRecordId}/feedback`);
    return response.data;
  };

  // 식단 피드백 수정
  const updateMealFeedbackApi = async (groupId, mealFeedbackId, feedbackUpdateRequest) => {
    const response = await axios.patch(`${contextPath}/${domain}/${groupId}/meal/feedback/${mealFeedbackId}`, feedbackUpdateRequest);
    return response.data;
  };

  // 운동 피드백 수정
  const updateExerciseFeedbackApi = async (groupId, exerciseFeedbackId, feedbackUpdateRequest) => {
    const response = await axios.patch(`${contextPath}/${domain}/${groupId}/exercise/feedback/${exerciseFeedbackId}`, feedbackUpdateRequest);
    return response.data;
  };

  // 식단 피드백 삭제
  const deleteMealFeedbackApi = async (groupId, mealFeedbackId) => {
    const response = await axios.delete(`${contextPath}/${domain}/${groupId}/meal/feedback/${mealFeedbackId}`);
    return response.data;
  };

  // 운동 피드백 삭제
  const deleteExerciseFeedbackApi = async (groupId, exerciseFeedbackId) => {
    const response = await axios.delete(`${contextPath}/${domain}/${groupId}/exercise/feedback/${exerciseFeedbackId}`);
    return response.data;
  };

  return {
    // 그룹
    createGroupApi,
    getGroupListApi,
    updateGroupNameApi,
    deleteGroupApi,

    // 그룹 페이지
    getGroupMemberRankingApi,
    getGroupMemberListApi,
    getDailyLogsApi,
    getGroupMealDetailApi,
    getGroupExerciseDetailApi,
    getRecordedDatesForLeader,
    getRecordedDatesForMember,

    // 그룹 멤버
    inviteGroupMemberApi,
    getInviteListApi,
    responseToInviteApi,
    withdrawGroupMemberApi,
    expelGroupMemberApi,

    // 피드백
    createMealFeedbackApi,
    createExerciseFeedbackApi,
    getMealFeedbackApi,
    getExerciseFeedbackApi,
    updateMealFeedbackApi,
    updateExerciseFeedbackApi,
    deleteMealFeedbackApi,
    deleteExerciseFeedbackApi,

    // 랭킹
    getGroupRankingApi
  };
});
