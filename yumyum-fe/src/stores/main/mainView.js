import { defineStore } from "pinia";
import { ref } from "vue";
import useMealView from "../meal/MealView";
import useExerciseView from "../exercise/ExerciseView";
import { useMainApi } from "@/lib/api/main";
import { getNutrionLabel, getRecommendLabel } from "@/utils/mainUtils";

const useMain = defineStore("mainPage", () => {
  const mainApi = useMainApi();

  const todayMealsStore = useMealView();
  const todayExerciseStore = useExerciseView();

  const setMealData = todayMealsStore.setMealData;
  const setExerciseData = todayExerciseStore.setExerciseData;

  //////////// data //////////////
  // 오늘 날짜 
  const dateString = ref(new Date().toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
    weekday: "long",
  }));
  
  // 리서치 완료 여부는 메인 화면에 '버튼 활성화' 상태를 위해 남겨둘 수 있지만,
  // 실제로는 API 로드 시 백엔드에서 받아온 값으로 세팅해야 합니다.
  const isResearchComplete = ref(false); // 리서치 완료 여부

  const streakRecord = ref(null); // 연속 기록

  const dailyGoals = ref(null); // 오늘의 목표 기록

  const recommendedMeals = ref(null); // 추천 식단 목록

  // Recommended Meals Detail Modal
  const recommendedSelectedMeal = ref(null); // 선택된 추천 식단
  const recommendedIsDetailModalOpen = ref(false); // 추천 식단 모달 창
  
  ////////// operation ///////////
  // DailyTargetAchieved
  const getRate = (nutrionKey) => {
    const modelValue = dailyGoals.value[nutrionKey].rate;
    if (modelValue) 
      return modelValue > 100 ? 
        100 : modelValue;
    else return 0;
  }
  
  // Recommended Meals Carousel
  const recommendedHandleViewDetail = (meal) => { // 추천 식단 모달 창 열 때
    recommendedSelectedMeal.value = meal;
    recommendedIsDetailModalOpen.value = true;
  };
  
  // Recommended Meals Detail Modal
  const closeModal = () => { // 추천 식단 모달 창 닫을 때
    recommendedSelectedMeal.value = null;
    recommendedIsDetailModalOpen.value = false;
  }

  //////// api /////////
  const setMainData = async () => {
    const response = await mainApi.getMainPageApi();

    isResearchComplete.value = !!response.data.isResearchComplete;
    setStreakData(response.data.streakRecord);
    setDailyTargetAchievedData(response.data.todayIntake);
    setRecommendedMealsCarouselData(response.data.todayRecommendMeal);
    setMealData();
    setExerciseData();
  }
  const setStreakData = (streakRecordOrigin) => {
    streakRecord.value = {};
    streakRecordOrigin.forEach(record => {
      streakRecord.value = {
        ...streakRecord.value,
        [record.streakType]: {
          name: record.name,
          longestStreak: record.longestStreak
        }
      }
    });
  }
  const setDailyTargetAchievedData = (todayIntake) => {
    dailyGoals.value = {};
    Object.keys(todayIntake).forEach(key => {
      const nutrition = todayIntake[key];
      dailyGoals.value = {
        ...dailyGoals.value,
        [key]: {
          label: getNutrionLabel(key),
          current: Math.round(nutrition.intake),
          target: Math.round(nutrition.target),
          rate: Math.round(nutrition.rate*100),
          unitName: nutrition.unitName
        }
      }
    });
  }
  const setRecommendedMealsCarouselData = (todayRecommendMeal) => {
    recommendedMeals.value = [];
    todayRecommendMeal.forEach(meal => {
      let foods = [];
      const foodOrigin = meal.foods;
      foodOrigin.forEach(food => foods.push({
        name: food.name,
        amount: food.amount,
        unit: food.unitName
      }));
      recommendedMeals.value.push({
        id: meal.mealId,
        type: meal.mealCreatorName.toLowerCase(),
        label: getRecommendLabel(meal.mealCreatorName),
        title: meal.title,
        calories: Math.round(meal.totalCalorie),
        protein: Math.round(meal.totalProtein),
        carbs: Math.round(meal.totalCarbohydrate),
        fat: Math.round(meal.totalFat),
        description: meal.description,
        foods
      });
    });
  }

  return {
    dateString,
    streakRecord,
    isResearchComplete,
    dailyGoals,
    recommendedMeals,
    recommendedSelectedMeal,
    recommendedIsDetailModalOpen,
    
    getRate,
    setMainData,
    recommendedHandleViewDetail,
    closeModal,
    setRecommendedMealsCarouselData,
  }
});

export default useMain;
