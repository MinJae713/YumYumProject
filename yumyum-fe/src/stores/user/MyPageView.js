import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { useUserApi } from "@/lib/api/user";
import { useAuthStore } from "@/stores/auth"; // 👈 1. Auth 스토어 임포트 (필수!)
import { useRouter } from "vue-router";

const useMyPageView = defineStore("myPageView", () => {
  const userApi = useUserApi();
  const authStore = useAuthStore(); // 👈 2. Auth 스토어 사용 선언 (필수!)
  const router = useRouter();

  const user = ref({
    userId: null,
    name: "",
    email: "",
    userRoleName: "",
    avatar: "",
    gender: null,
    birthDate: null,
  });

  const stats = ref({
    height: null,
    currentWeight: null,
    targetWeight: null,
    goalType: "",
    mealsPerDay: null,
  });

  const settings = ref({
    notifications: true,
    researchReminder: true,
    remindTime: "",
  });

  const dialogOpen = ref(false);
  const profileDraft = ref({
    name: user.value.name,
    email: user.value.email,
    gender: user.value.gender,
    birthDate: user.value.birthDate,
    height: stats.value.height,
    goalType: stats.value.goalType,
    currentWeight: stats.value.currentWeight,
    targetWeight: stats.value.targetWeight,
    mealsPerDay: stats.value.mealsPerDay,
    remindTime: settings.value.remindTime,
  });
  const passwordDialogOpen = ref(false);
  const passwordForm = ref({
    current: "",
    new: "",
    confirm: "",
  });

  const userRoleBadge = computed(() =>
    user.value.userRoleName === "BUSINESS" ? "전문가" : "일반 회원"
  );

  const goalLabel = computed(() => {
    const map = { lose: "다이어트", maintain: "유지", gain: "벌크업" };
    return map[stats.value.goalType] || "-";
  });

  const fetchUserInfo = async () => {
    try {
      const response = await userApi.getUserDetailApi();
      const data = response?.data ?? response;

      if (!data) throw new Error("유저 정보를 불러오지 못했습니다.");

      user.value = {
        userId: data.userId ?? null,
        name: data.name ?? "",
        email: data.email ?? "",
        userRoleName: data.userRoleName ?? "",
        gender: data.gender ?? null,
        birthDate: data.birthDate ? data.birthDate.substring(0, 10) : null,
        // 👇 아바타 생성 로직 (유지)
        avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${data.name ?? "User"}`,
      };

      stats.value = {
        height: data.height ?? null,
        currentWeight: data.currentWeight ?? null,
        targetWeight: data.targetWeight ?? null,
        goalType: data.goalType ?? "",
        mealsPerDay: data.mealsPerDay ?? null,
      };

      settings.value = {
        ...settings.value,
        remindTime: data.remindTime ? data.remindTime.substring(0, 5) : "",
      };

      profileDraft.value = {
        name: user.value.name,
        email: user.value.email,
        gender: user.value.gender,
        birthDate: user.value.birthDate,
        height: stats.value.height,
        goalType: stats.value.goalType,
        currentWeight: stats.value.currentWeight,
        targetWeight: stats.value.targetWeight,
        mealsPerDay: stats.value.mealsPerDay,
        remindTime: settings.value.remindTime,
      };
    } catch (error) {
      console.error("마이페이지 정보 로드 실패:", error);
    }
  };

  const updateUserInfo = async (updates = {}) => {
    const remindTimeValue = updates.remindTime ?? settings.value.remindTime;
    const payload = {
      name: updates.name ?? user.value.name,
      remindTime:
        remindTimeValue === undefined || remindTimeValue === null
          ? remindTimeValue
          : remindTimeValue.substring(0, 5),
      mealsPerDay: updates.mealsPerDay ?? stats.value.mealsPerDay,
      targetWeight: updates.targetWeight ?? stats.value.targetWeight,
      gender: updates.gender ?? user.value.gender ?? null,
      birthDate: updates.birthDate ?? user.value.birthDate ?? null,
      height: updates.height ?? stats.value.height,
      goalType: updates.goalType ?? stats.value.goalType,
      currentWeight: updates.currentWeight ?? stats.value.currentWeight,
    };

    Object.keys(payload).forEach((key) => {
      const val = payload[key];
      if (val === null || val === undefined) {
        delete payload[key];
      }
    });

    const response = await userApi.updateUserInfoApi(payload);
    const data = response?.data ?? response;
    const updatedName = data.name ?? payload.name;
    const updatedAvatar = `https://api.dicebear.com/7.x/avataaars/svg?seed=${updatedName}`;

    user.value = {
      ...user.value,
      name: updatedName,
      // 이름이 바뀌면 아바타도 바뀌도록 업데이트
      avatar: updatedAvatar,
      gender: data.gender ?? payload.gender ?? user.value.gender,
      birthDate: data.birthDate ? data.birthDate.substring(0, 10) : payload.birthDate ?? user.value.birthDate,
    };

    const syncedAuthUser = {
      ...(authStore.user || {}),
      ...user.value,
      id: authStore.user?.id ?? authStore.user?.userId ?? user.value.userId,
      userId: authStore.user?.userId ?? user.value.userId,
      name: updatedName,
      avatar: updatedAvatar,
    };
    authStore.setAuthData(authStore.accessToken, syncedAuthUser);

    settings.value = {
      ...settings.value,
      remindTime: data.remindTime ? data.remindTime.substring(0, 5) : payload.remindTime,
    };

    stats.value = {
      ...stats.value,
      mealsPerDay: data.mealsPerDay ?? payload.mealsPerDay,
      targetWeight: data.targetWeight ?? payload.targetWeight,
      height: data.height ?? payload.height,
      goalType: data.goalType ?? payload.goalType,
      currentWeight: data.currentWeight ?? payload.currentWeight,
    };

    profileDraft.value = {
      name: user.value.name,
      email: user.value.email,
      gender: user.value.gender,
      birthDate: user.value.birthDate,
      height: stats.value.height,
      goalType: stats.value.goalType,
      currentWeight: stats.value.currentWeight,
      targetWeight: stats.value.targetWeight,
      mealsPerDay: stats.value.mealsPerDay,
      remindTime: settings.value.remindTime,
    };
  };

  const handleUpdateSettings = async (updated) => {
    settings.value = { ...settings.value, ...updated };
    if (updated.mealsPerDay !== undefined) {
      stats.value = { ...stats.value, mealsPerDay: updated.mealsPerDay };
    }
    try {
      await updateUserInfo(updated);
    } catch (error) {
      console.error("설정 업데이트 실패:", error);
    }
  };

  const handleUpdateProfile = async (updated) => {
    try {
      await updateUserInfo(updated);
    } catch (error) {
      console.error("프로필 업데이트 실패:", error);
    }
  };

  const handleSaveProfile = async () => {
    await handleUpdateProfile({ ...profileDraft.value });
    dialogOpen.value = false;
  };

  const handleEdit = () => {
    profileDraft.value = {
      name: user.value.name,
      email: user.value.email,
      gender: user.value.gender,
      birthDate: user.value.birthDate,
      height: stats.value.height,
      goalType: stats.value.goalType,
      currentWeight: stats.value.currentWeight,
      targetWeight: stats.value.targetWeight,
      mealsPerDay: stats.value.mealsPerDay,
      remindTime: settings.value.remindTime,
    };
    dialogOpen.value = true;
  };

  // 👇 3. 여기가 핵심! 로그아웃 연결
  const handleLogout = async () => {
    await authStore.logout(); 
  };

  const handleEditProfile = () => {
    handleEdit();
  };

  const handleChangePassword = async ({ currentPassword, newPassword }) => {
    try {
      const response = await userApi.changePasswordApi({
        currentPassword,
        newPassword,
      });
      const success = response?.code === 200 || response?.status === 200;
      if (success) {
        alert("비밀번호가 변경되었습니다.");
        return true;
      }
      alert(response?.data?.message || "비밀번호 변경에 실패했습니다.");
      return false;
    } catch (error) {
      console.error("비밀번호 변경 실패:", error);
      alert(error?.response?.data?.message || "비밀번호 변경 중 오류가 발생했습니다.");
      return false;
    }
  };

  const summary = computed(() => {
    const height = stats.value.height ?? "-";
    const weight = stats.value.currentWeight ?? "-";
    const target = stats.value.targetWeight ?? "-";
    const goal = goalLabel.value || "-";
    return `키: ${height}cm | 현재: ${weight}kg | 목표: ${target}kg (${goal})`;
  });

  const resetPasswordForm = () => {
    passwordForm.value = { current: "", new: "", confirm: "" };
  };

  const submitPasswordChange = async () => {
    if (passwordForm.value.new !== passwordForm.value.confirm) {
      alert("새 비밀번호가 일치하지 않습니다.");
      return;
    }
    const ok = await handleChangePassword({
      currentPassword: passwordForm.value.current,
      newPassword: passwordForm.value.new,
    });
    if (ok) {
      resetPasswordForm();
      passwordDialogOpen.value = false;
    }
  };

  const handleWithdrawal = async () => {
    try {
      const response = await userApi.deleteUserApi();
      const success = response?.code === 200 || response?.status === 200;
      if (success) {
        authStore.setAccessToken(null);
        router.push("/auth/login");
        alert("회원 탈퇴가 완료되었습니다.");
        return true;
      }
      alert(response?.data?.message || "회원 탈퇴에 실패했습니다.");
      return false;
    } catch (error) {
      console.error("회원 탈퇴 실패:", error);
      alert(error?.response?.data?.message || "회원 탈퇴 중 오류가 발생했습니다.");
      return false;
    }
  };

  const handleWithdrawalClick = async () => {
    if (window.confirm("정말로 탈퇴하시겠습니까? 모든 정보가 삭제됩니다.")) {
      await handleWithdrawal();
    }
  };

  return {
    user,
    stats,
    settings,
    dialogOpen,
    profileDraft,
    passwordDialogOpen,
    passwordForm,
    userRoleBadge,
    goalLabel,
    summary,
    fetchUserInfo,
    handleUpdateSettings,
    handleUpdateProfile,
    handleSaveProfile,
    handleEdit,
    handleLogout,
    handleEditProfile,
    handleChangePassword,
    resetPasswordForm,
    submitPasswordChange,
    handleWithdrawal,
    handleWithdrawalClick,
  };
});

export default useMyPageView;
