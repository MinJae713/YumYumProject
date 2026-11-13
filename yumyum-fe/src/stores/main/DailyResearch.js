import { defineStore } from "pinia";
import { ref } from "vue";
import { useResearchApi } from "@/lib/api/research";
import useMain from "@/stores/main/mainView";


const useDailyResearch = defineStore("dailyResearch", () => {
  const researchApi = useResearchApi();

  const dietPerformance = ref(null);
  const exerciseIntensity = ref(null);
  const digestionState = ref(null);
  const tomorrowActivity = ref(null);
  const freeMeal = ref(null);
  const isModalOpen = ref(false);

  const dietPerformanceOptions = [
    { value: "LOW", label: "부족" },
    { value: "NORMAL", label: "적당" },
    { value: "HIGH", label: "과식" },
  ];

  const exerciseIntensityOptions = [
    { value: "NONE", label: "쉼" },
    { value: "NORMAL", label: "보통" },
    { value: "HIGH", label: "고강도" },
  ];

  const digestionStateOptions = [
    { value: "HUNGRY", label: "허기짐" },
    { value: "COMFORTABLE", label: "편안" },
    { value: "BLOATED", label: "더부룩" },
  ];

  const tomorrowActivityOptions = [
    { value: "LOW", label: "집콕" },
    { value: "NORMAL", label: "평소" },
    { value: "HIGH", label: "활동적" },
  ];

  const freeMealOptions = [
    { value: "NONE", label: "없음" },
    { value: "LUNCH", label: "점심" },
    { value: "DINNER", label: "저녁" },
    { value: "BOTH", label: "둘다" },
  ];

  const resetForm = () => {
    dietPerformance.value = null;
    exerciseIntensity.value = null;
    digestionState.value = null;
    tomorrowActivity.value = null;
    freeMeal.value = null;
  };

  const submitResearch = async () => {
    if (
      !dietPerformance.value ||
      !exerciseIntensity.value ||
      !digestionState.value ||
      !tomorrowActivity.value ||
      !freeMeal.value
    ) {
      alert("모든 항목을 선택해주세요.");
      return;
    }

    const payload = {
      dietPerformance: dietPerformance.value,
      exerciseIntensity: exerciseIntensity.value,
      digestionState: digestionState.value,
      tomorrowActivity: tomorrowActivity.value,
      freeMeal: freeMeal.value,
    };

    try {
      await researchApi.submitResearchApi(payload);

      // 🔹 메인 페이지 상태 즉시 갱신
      const mainStore = useMain();
      await mainStore.setMainData();

      alert("리서치가 제출되었습니다.");
      resetForm();
      isModalOpen.value = false;
    } catch (error) {
      console.error("Failed to submit research", error);
      const message =
        error?.response?.data?.message || "리서치 제출 중 오류가 발생했습니다.";
      alert(message);
    }

  };

  const openModal = () => {
    isModalOpen.value = true;
  };

  const closeModal = () => {
    isModalOpen.value = false;
    resetForm();
  };

  return {
    dietPerformance,
    exerciseIntensity,
    digestionState,
    tomorrowActivity,
    freeMeal,
    isModalOpen,
    dietPerformanceOptions,
    exerciseIntensityOptions,
    digestionStateOptions,
    tomorrowActivityOptions,
    freeMealOptions,
    submitResearch,
    resetForm,
    openModal,
    closeModal,
  };
});

export default useDailyResearch;
