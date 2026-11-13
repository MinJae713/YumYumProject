import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { useGroupApi } from "@/lib/api/group";
import { useAuthStore } from "@/stores/auth";
import { useUserApi } from "@/lib/api/user";
import { formatDateKey } from "@/utils/groupUtils";

export const useGroupStore = defineStore("groupStore", () => {
  const groupApi = useGroupApi();
  const userApi = useUserApi();
  const authStore = useAuthStore();
  const groups = ref([]);
  const isLoading = ref(false);
  const currentGroupMembers = ref([]);
  const groupRankings = ref([]);
  const invites = ref([]);
  const memberDailyLog = ref(null);
  const recordDetail = ref(null);
  const recordFeedbacks = ref([]);
  const searchResults = ref([]);
  const recordedMealDates = ref([]);
  const recordedExerciseDates = ref([]);

  const normalizeDateStringsToDates = (dateStrings = []) =>
    (dateStrings || [])
      .map((dateStr) => {
        if (!dateStr) return null;
        const [year, month, day] = dateStr.split("-").map(Number);
        if ([year, month, day].some((n) => Number.isNaN(n))) return null;
        return new Date(year, month - 1, day);
      })
      .filter(Boolean);

  const currentUserRole = computed(() => {
    const role = authStore.user?.role;
    return role === "BUSINESS" ? "leader" : "member";
  });

  const fetchGroups = async () => {
    isLoading.value = true;
    try {
      const response = await groupApi.getGroupListApi();
      const data = response?.data ?? response ?? [];
      const list = Array.isArray(data?.data) ? data.data : Array.isArray(data) ? data : [];

      groups.value = list.map((item) => ({
        id: item.groupId,
        name: item.groupName,
        isLeader: item.roleName === "LEADER",
        memberCount: item.memberCount ?? 0,
      }));
    } catch (error) {
      console.error("그룹 목록 불러오기 실패:", error);
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const createGroup = async (groupName) => {
    try {
      await groupApi.createGroupApi({ groupName });
      await fetchGroups();
    } catch (error) {
      console.error("그룹 생성 실패:", error);
      throw error;
    }
  };

  const fetchGroupMembers = async (groupId) => {
    if (!groupId) {
      currentGroupMembers.value = [];
      return;
    }
    try {
      const response = await groupApi.getGroupMemberListApi(groupId);
      const data = response?.data ?? response ?? [];
      const list = Array.isArray(data?.data) ? data.data : Array.isArray(data) ? data : [];

      currentGroupMembers.value = list.map((item) => ({
        id: item.groupMemberId,
        userId: item.userId,
        name: item.name,
        avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${item.userId}`,
        goal: item.goalType,
        startWeight: item.startWeight ?? item.currentWeight ?? 0,
        currentWeight: item.currentWeight ?? 0,
        targetWeight: item.targetWeight ?? 0,
        consecutiveDays: Math.max(item.mealStreak ?? 0, item.exerciseStreak ?? 0),
        lastActivityDate: item.lastLogUpdateAt ?? null,
        totalCalories: 0,
        exerciseCount: 0,
      }));
    } catch (error) {
      console.error("그룹원 목록 불러오기 실패:", error);
      throw error;
    }
  };

  const fetchGroupRankings = async (groupId) => {
    if (!groupId) {
      groupRankings.value = [];
      return;
    }
    try {
      const response = await groupApi.getGroupRankingApi(groupId);
      const data = response?.data ?? response ?? [];
      const list = Array.isArray(data?.data) ? data.data : Array.isArray(data) ? data : [];
      groupRankings.value = list.map((item) => ({
        id: item.groupMemberId,
        userId: item.userId,
        name: item.name,
        ranking: item.ranking,
        totalPoints: item.points,
        goal: item.goalType,
        isMe: item.isMe,
        currentWeight: item.currentWeight ?? 0,
        consecutiveDays: Math.max(item.mealStreak ?? 0, item.exerciseStreak ?? 0),
        avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${item.userId}`,
      }));
    } catch (error) {
      console.error("그룹 랭킹 불러오기 실패:", error);
      throw error;
    }
  };

  const inviteMember = async (groupId, userId) => {
    if (!groupId || !userId) {
      throw new Error("그룹 ID와 유저 ID가 필요합니다.");
    }
    try {
      await groupApi.inviteGroupMemberApi(groupId, { userId });
    } catch (error) {
      console.error("그룹원 초대 실패:", error);
      throw error;
    }
  };

  const fetchInvites = async () => {
    try {
      const response = await groupApi.getInviteListApi();
      const data = response?.data ?? response ?? [];
      const list = Array.isArray(data?.data) ? data.data : Array.isArray(data) ? data : [];

      invites.value = list.map((item) => ({
        invitationId: item.groupInviteId,
        groupName: item.groupName,
        leaderName: item.leaderName,
        invitedAt: item.sentAt,
      }));
    } catch (error) {
      console.error("초대 목록 불러오기 실패:", error);
      throw error;
    }
  };

  const respondToInvite = async (invitationId, isAccept) => {
    try {
      await groupApi.responseToInviteApi({ groupInviteId: invitationId, accept: isAccept });
      await fetchInvites();
      if (isAccept) {
        await fetchGroups();
      }
    } catch (error) {
      console.error("초대 응답 실패:", error);
      throw error;
    }
  };

  const fetchMemberDailyLog = async (groupId, memberId, date) => {
    if (!groupId || !memberId || !date) {
      memberDailyLog.value = null;
      return;
    }

    try {
      const formattedDate = typeof date === "string" ? date : formatDateKey(date);

      if (!formattedDate) {
        console.error("유효하지 않은 날짜입니다:", date);
        memberDailyLog.value = null;
        return;
      }

      const response = await groupApi.getDailyLogsApi(groupId, formattedDate, memberId);
      const data = response?.data ?? response ?? null;
      memberDailyLog.value = data;
    } catch (error) {
      console.error("멤버 데일리 로그 불러오기 실패:", error);
      memberDailyLog.value = null;
    }
  };

  const fetchMyDailyLog = async (groupId, date) => {
    if (!groupId || !date) {
      memberDailyLog.value = null;
      return;
    }
    try {
      const formattedDate = typeof date === "string" ? date : formatDateKey(date);
      if (!formattedDate) {
        console.error("유효하지 않은 날짜입니다:", date);
        memberDailyLog.value = null;
        return;
      }
      const response = await groupApi.getDailyLogsApi(groupId, formattedDate);
      const data = response?.data ?? response ?? null;
      memberDailyLog.value = data;
    } catch (error) {
      console.error("내 데일리 로그 불러오기 실패:", error);
      memberDailyLog.value = null;
    }
  };

  const withdrawGroup = async (groupId) => {
    if (!groupId) return;
    try {
      await groupApi.withdrawGroupMemberApi(groupId);
      await fetchGroups();
    } catch (error) {
      console.error("그룹 탈퇴 실패:", error);
      throw error;
    }
  };

  const initializeLeaderView = async (groupId) => {
    if (!groupId) return;
    await Promise.all([fetchGroupMembers(groupId), fetchGroupRankings(groupId)]);
  };

  const initializeMemberView = async (groupId) => {
    if (!groupId) return;
    await Promise.all([
      fetchGroupRankings(groupId),
      fetchMyDailyLog(groupId, new Date()),
      fetchRecordedDatesForMember(groupId),
    ]);
  };

  // ✅ 수정된 부분: API 이름 변경 (searchUserApi -> searchUserByEmailApi)
  const searchUser = async (keyword) => {
    if (!keyword?.trim()) {
      searchResults.value = [];
      return;
    }
    try {
      const response = await userApi.searchUserByEmailApi(keyword.trim());
      const data = response?.data ?? response ?? [];
      const list = Array.isArray(data?.data) ? data.data : Array.isArray(data) ? data : [];
      searchResults.value = list;
    } catch (error) {
      console.error("유저 검색 실패:", error);
      throw error;
    }
  };

  const clearSearchResults = () => {
    searchResults.value = [];
  };

  const fetchRecordDetail = async (groupId, type, id) => {
    if (!groupId || !type || !id) {
      recordDetail.value = null;
      recordFeedbacks.value = [];
      return;
    }
    try {
      let response;
      if (type === "meal") {
        response = await groupApi.getGroupMealDetailApi(groupId, id);
      } else if (type === "exercise") {
        response = await groupApi.getGroupExerciseDetailApi(groupId, id);
      } else {
        throw new Error("알 수 없는 기록 타입입니다.");
      }

      const data = response?.data ?? response ?? {};
      if (type === "meal") {
        recordDetail.value = data.mealDetail || null;
      } else if (type === "exercise") {
        const detail = data.exerciseDetail;
        const extractedName = detail?.exerciseList?.[detail?.exerciseIndex]?.exerciseName;

        recordDetail.value = detail
          ? {
              ...detail,
              exerciseRecordId: id,
              exerciseName: extractedName,
              burnedCalorie: detail.calories,
              setCount: detail.setNo,
              repeatCount: detail.reps,
              weight: detail.weightKg,
            }
          : null;
      }
      recordFeedbacks.value = data.feedbacks || [];
    } catch (error) {
      console.error("기록 상세 불러오기 실패:", error);
      recordDetail.value = null;
      recordFeedbacks.value = [];
      throw error;
    }
  };

  const registerFeedback = async (groupId, type, id, content) => {
    if (!groupId || !type || !id || !content?.trim()) return;
    try {
      if (type === "meal") {
        await groupApi.createMealFeedbackApi(groupId, { targetLogId: id, content });
      } else if (type === "exercise") {
        await groupApi.createExerciseFeedbackApi(groupId, { targetLogId: id, content });
      } else {
        throw new Error("알 수 없는 기록 타입입니다.");
      }
      await fetchRecordDetail(groupId, type, id);
    } catch (error) {
      console.error("피드백 등록 실패:", error);
      throw error;
    }
  };

  const updateFeedback = async (groupId, type, feedbackId, content) => {
    if (!groupId || !type || !feedbackId || !content?.trim()) return;
    try {
      if (type === "meal") {
        await groupApi.updateMealFeedbackApi(groupId, feedbackId, { content });
      } else if (type === "exercise") {
        await groupApi.updateExerciseFeedbackApi(groupId, feedbackId, { content });
      } else {
        throw new Error("알 수 없는 기록 타입입니다.");
      }
      // 피드백 수정 후 상세 재조회
      const targetId = recordDetail.value?.mealId || recordDetail.value?.exerciseRecordId || null;
      await fetchRecordDetail(groupId, type, targetId);
    } catch (error) {
      console.error("피드백 수정 실패:", error);
      throw error;
    }
  };

  const deleteFeedback = async (groupId, type, feedbackId) => {
    if (!groupId || !type || !feedbackId) return;
    try {
      if (type === "meal") {
        await groupApi.deleteMealFeedbackApi(groupId, feedbackId);
      } else if (type === "exercise") {
        await groupApi.deleteExerciseFeedbackApi(groupId, feedbackId);
      } else {
        throw new Error("알 수 없는 기록 타입입니다.");
      }
      const targetId = recordDetail.value?.mealId || recordDetail.value?.exerciseRecordId || null;
      await fetchRecordDetail(groupId, type, targetId);
    } catch (error) {
      console.error("피드백 삭제 실패:", error);
      throw error;
    }
  };

  const updateGroupName = async (groupId, newName) => {
    if (!groupId || !newName?.trim()) return;
    try {
      await groupApi.updateGroupNameApi(groupId, { newGroupName: newName });
      await fetchGroups();
    } catch (error) {
      console.error("그룹 이름 수정 실패:", error);
      throw error;
    }
  };

  const deleteGroup = async (groupId) => {
    if (!groupId) return;
    try {
      await groupApi.deleteGroupApi(groupId);
      await fetchGroups();
    } catch (error) {
      console.error("그룹 삭제 실패:", error);
      throw error;
    }
  };

  const expelMember = async (groupId, memberId) => {
    if (!groupId || !memberId) return;
    try {
      await groupApi.expelGroupMemberApi(groupId, memberId);
      await fetchGroupMembers(groupId);
      await fetchGroupRankings(groupId);
    } catch (error) {
      console.error("그룹원 강퇴 실패:", error);
      throw error;
    }
  };

  const fetchRecordedDatesForLeader = async (groupId, groupMemberId) => {
    recordedMealDates.value = [];
    recordedExerciseDates.value = [];
    if (!groupId || !groupMemberId) return;
    try {
      const data = await groupApi.getRecordedDatesForLeader(groupId, groupMemberId);
      recordedMealDates.value = normalizeDateStringsToDates(data?.mealDates);
      recordedExerciseDates.value = normalizeDateStringsToDates(data?.exerciseDates);
    } catch (error) {
      console.error("그룹원 기록 날짜 조회 실패:", error);
      throw error;
    }
  };

  const fetchRecordedDatesForMember = async (groupId) => {
    recordedMealDates.value = [];
    recordedExerciseDates.value = [];
    if (!groupId) return;
    try {
      const data = await groupApi.getRecordedDatesForMember(groupId);
      recordedMealDates.value = normalizeDateStringsToDates(data?.mealDates);
      recordedExerciseDates.value = normalizeDateStringsToDates(data?.exerciseDates);
    } catch (error) {
      console.error("내 기록 날짜 조회 실패:", error);
      throw error;
    }
  };

  const fetchRecordedDates = async ({ groupId, memberId = null } = {}) => {
    recordedMealDates.value = [];
    recordedExerciseDates.value = [];
    if (!groupId) return;
    try {
      if (memberId) {
        await fetchRecordedDatesForLeader(groupId, memberId);
      } else {
        await fetchRecordedDatesForMember(groupId);
      }
    } catch (error) {
      // 개별 함수에서 로그 처리, 상태는 빈 배열 유지
    }
  };

  return {
    groups,
    isLoading,
    currentGroupMembers,
    groupRankings,
    invites,
    memberDailyLog,
    recordDetail,
    recordFeedbacks,
    searchResults,
    currentUserRole,
    recordedMealDates,
    recordedExerciseDates,
    fetchGroups,
    createGroup,
    fetchGroupMembers,
    fetchGroupRankings,
    inviteMember,
    fetchInvites,
    respondToInvite,
    fetchMemberDailyLog,
    fetchMyDailyLog,
    withdrawGroup,
    fetchRecordDetail,
    registerFeedback,
    updateFeedback,
    deleteFeedback,
    updateGroupName,
    deleteGroup,
    expelMember,
    initializeLeaderView,
    initializeMemberView,
    searchUser,
    clearSearchResults,
    fetchRecordedDatesForLeader,
    fetchRecordedDatesForMember,
    fetchRecordedDates,
  };
});
