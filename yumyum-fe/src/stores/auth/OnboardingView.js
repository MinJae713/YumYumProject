import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import { useUserApi } from "@/lib/api/user";

const useOnboardingViewStore = defineStore("onboardingViewStore", () => {
  const router = useRouter();
  const userApi = useUserApi();

  const currentStep = ref(1);
  const totalSteps = ref(2);

  const surveyData = ref({
    gender: "",
    birthDate: "",
    height: "",
    currentWeight: "",
    targetWeight: "",
    goalType: "lose",
    mealsPerDay: "",
    remindTime: "",
  });

  const isDirty = computed(() => Object.values(surveyData.value).some(v => v !== ""));

  const progressPercentage = computed(() => (currentStep.value / totalSteps.value) * 100);

  const handleNext = async () => {
    if (currentStep.value < totalSteps.value) {
      currentStep.value += 1;
      return;
    }

    try {
      const payload = {
        gender: surveyData.value.gender === "female" ? "FEMALE" : "MALE",
        birthDate: surveyData.value.birthDate,
        height: Number(surveyData.value.height),
        currentWeight: Number(surveyData.value.currentWeight),
        targetWeight: Number(surveyData.value.targetWeight),
        goalType: surveyData.value.goalType,
        mealsPerDay: Number(surveyData.value.mealsPerDay),
        remindTime: surveyData.value.remindTime,
      };

      const response = await userApi.updateUserResearchApi(payload);

      if (response?.code === 200) {
        router.push("/");
      } else {
        alert(response?.data?.message || "설문 저장에 실패했습니다.");
      }
    } catch (error) {
      console.error("온보딩 저장 에러:", error);
      alert("설문 저장 중 오류가 발생했습니다.");
    }
  };

  const handleBack = () => {
    if (currentStep.value > 1) currentStep.value -= 1;
  };

  return {
    currentStep,
    totalSteps,
    surveyData,
    progressPercentage,
    handleNext,
    handleBack,
    isDirty
  };
});

export default useOnboardingViewStore;
