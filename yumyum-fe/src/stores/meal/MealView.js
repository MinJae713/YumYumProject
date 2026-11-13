import { defineStore } from "pinia";
import { computed, ref, watch } from "vue";
import { Coffee, Sun, Moon, Cookie } from "lucide-vue-next";
import { useMealApi } from "@/lib/api/meal";
import { formatDateKey } from "@/utils/commonUtils";

const useMealView = defineStore("mealView", () => {
  const mealApi = useMealApi();

  ////////////// data ////////////////
  const selectedDate = ref(new Date());
  const mealSlots = ref([
    { key: "breakfast", label: "아침", icon: Coffee, time: "07:00 - 09:00", color: "text-orange-500", bg: "border-orange-200" },
    { key: "lunch", label: "점심", icon: Sun, time: "12:00 - 14:00", color: "text-yellow-500", bg: "border-yellow-200" },
    { key: "dinner", label: "저녁", icon: Moon, time: "18:00 - 20:00", color: "text-violet-500", bg: "border-violet-200" },
    { key: "snack", label: "간식", icon: Cookie, time: "언제든지", color: "text-pink-500", bg: "border-pink-200" },
  ]);

  const isRegistrationOpen = ref(false);
  const selectedMealsData = ref({});
  const selectedMeal = ref(null);
  const selectedMealType = ref("breakfast");
  const mealTypeLabel = computed(() => {
    const labels = { breakfast: "아침", lunch: "점심", dinner: "저녁", snack: "간식" };
    return labels[selectedMealType.value] || selectedMealType.value;
  });
  const datesWithMeals = ref([]); 
  const searchQuery = ref("");
  const selectedFoods = ref([]);
  const showDeleteDialog = ref(false);
  const totalNutrition = ref({ point:0, calories: 0, protein: 0, carbs: 0, fat: 0 });
  const updateFoodLog = ref([]);
  const isGroupMember = ref(false);

  //////////////// operation ////////////////

  const handleSelectDate = (date) => {
    if (date) selectedDate.value = date;
  };

  const getNextMonth = async () => {
    let date = selectedDate.value;
    if ((typeof date) === 'string') date = new Date(date);
    date.setMonth(date.getMonth()+1);
    date.setDate(1);
    const response = await mealApi.getMonthRecordApi(date.getFullYear(), date.getMonth()+1);
    datesWithMeals.value = response.data;
    handleSelectDate(formatDateKey(date));
  }

  const getPrevMonth = async () => {
    let date = selectedDate.value;
    if ((typeof date) === 'string') date = new Date(date);
    date.setMonth(date.getMonth()-1);
    date.setDate(1);
    const response = await mealApi.getMonthRecordApi(date.getFullYear(), date.getMonth()+1);
    datesWithMeals.value = response.data;
    handleSelectDate(formatDateKey(date));
  }

  // [API] 선택 날짜 식단 조회
  watch(() => [selectedDate.value, isRegistrationOpen.value], async () => {
    const key = formatDateKey(selectedDate.value);
    const response = await mealApi.getMealOfDateApi(key);
    datesWithMeals.value = response.data.recordedDates.map((dateKey) =>{
      const [year, month, day] = dateKey.split("-").map(Number);
      return new Date(year, month - 1, day);
    });
    const todayMeals = response.data.meals;
    selectedMealsData.value = {
      mealTotalCalorie: Math.round(response.data.mealTotalCalorie),
      mealTotalPoint: response.data.mealTotalPoint,
      mealTotalBenefitPoint: response.data.mealTotalBenefitPoint
    };

    todayMeals.forEach(meal => {
      const foods = [];
      if (meal.totalPoint != null) isGroupMember.value = true;
      meal.foods.forEach(food => foods.push({
        id: food.foodId, name: food.name, amount: food.amount, unit: food.unitName
      }));

      const mealType = meal.mealTypeName;
      selectedMealsData.value = {
        ...selectedMealsData.value,
        [mealType]: {
          id: meal.mealId, type: mealType, foods,
          points: meal.totalPoint, totalCalories: Math.round(meal.totalCalorie),
          protein: Math.round(meal.totalProtein), carbs: Math.round(meal.totalCarbohydrate), fat: Math.round(meal.totalFat)
        }
      }
    });
  });

  // MealRegistrationModal
  
  const filteredFoods = ref([]);
  watch(searchQuery, async () => { 
    if (!searchQuery.value) filteredFoods.value = [];
    else {
      const response = await mealApi.searchFoodApi(searchQuery.value);
      filteredFoods.value = response.data.map(food => ({
        id: food.foodId, name: food.name, calories: food.calorie,
        carbs: food.carbohydrate, fat: food.fat, protein: food.protein,
        servingSize: food.amount, servingUnit: food.unitName
      }));
    }
  });
  
  const handleSave = async (setMainData = null) => {
    const mealData = {
      mealDate: formatDateKey(selectedDate.value),
      mealTypeName: mealTypeLabel.value,
      mealCreatorName: "USER",
      totalPoint: totalNutrition.value.point,
      totalCalorie: Math.round(totalNutrition.value.calories),
      foods: selectedFoods.value.map((food) => ({ foodId: food.id, amount: food.amount }))
    };
    await mealApi.addMealApi(mealData);
    closeModal();
    setMainData?.();
  };

  const handleUpdate = async (setMainData = null) => { 
    if (updateFoodLog.value.length === 0) {
      alert("수정 사항이 없습니다!");
      return;
    }
    const updateRequest = {
      mealId: selectedMeal.value.id,
      totalPoint: totalNutrition.value.point,
      totalCalorie: totalNutrition.value.calories,
      updateFoods: updateFoodLog.value
    };
    await mealApi.updateMealApi(updateRequest);
    closeModal();
    setMainData?.();
  };

const handleApply = async (setMainData = null) => {
  if (!selectedMealType.value) {
    alert("식단 종류는 반드시 입력해 주세요!");
    return;
  }

  const mealTypeName = mealSlots.value
    .find(meal => meal.key === selectedMealType.value).label;

  if (selectedMealsData.value[mealTypeName]) {
    alert("해당 끼니에는 이미 식단이 존재합니다!\n다른 끼니를 선택해 주세요");
    return;
  }

  const applyRequest = {
    mealId: selectedMeal.value.id,
    mealTypeName,
    totalPoint: totalNutrition.value.point,
    totalCalorie: totalNutrition.value.calories,
    updateFoods: updateFoodLog.value
  };

  await mealApi.applyRecommendMealApi(applyRequest);
  closeModal();
  setMainData?.();
};


  const modalStatus = ref({ // 모달창 모드 정의
    ADD: {
      modalTitle: "식단 추가",
      key: "ADD",
      label: "저장하기",
      handleSubmit: handleSave,
      isRecommend: false,
      searchAble: true,
      mealTypeSelectAble: false
    },
    EDIT: {
      modalTitle: "식단 수정",
      key: "EDIT",
      label: "수정하기",
      handleSubmit: handleUpdate,
      isRecommend: false,
      searchAble: true,
      mealTypeSelectAble: false
    },
    RECOMMEND: {
      modalTitle: "추천 식단 적용",
      key: "RECOMMEND",
      label: "적용하기",
      handleSubmit: handleApply,
      isRecommend: true,
      searchAble: false,
      mealTypeSelectAble: true
    },
    RECOMMEND_EDIT: {
      modalTitle: "추천 식단 수정",
      key: "RECOMMEND_EDIT",
      label: "수정하기",
      handleSubmit: handleUpdate,
      isRecommend: true,
      searchAble: false,
      mealTypeSelectAble: false
    }
  });
  const currentModalStatus = ref(null);

  const openModal = (type, data = null) => {
    currentModalStatus.value = data == null ? modalStatus.value.ADD : modalStatus.value.EDIT;
    selectedMealType.value = type;
    isRegistrationOpen.value = true;
    selectedMeal.value = data;
  };

  // [API] 식단 세부 내용 조회 (모달 창 열릴 때 동작)
  watch(
    () => [selectedMeal.value, isRegistrationOpen.value],
    async ([selectedMeal, isOpen]) => {
      if (isOpen) {
        if (selectedMeal && selectedMeal.foods) {
          const response = await mealApi.getMealDetailApi(selectedMeal.id);
          selectedFoods.value = [];
          if (response.data.foods) {
            response.data.foods.forEach(food => selectedFoods.value.push({
              id: food.foodId || Date.now().toString(),
              name: food.name,
              calories: food.calorie, // 상세 정보가 없다면 0 처리
              protein: food.protein,
              carbs: food.carbohydrate,
              fat: food.fat,
              servingSize: 100,
              servingUnit: food.unitName,
              amount: food.amount
            }));
            updateFoodLog.value = [];
            parseToTotalAPI();
          }
          // 추천 식단인데 끼니가 정해져있다? -> 이미 선택한 추천 식단을 수정하는 상황
          // 추천 식단 수정 모드로 지정
          if (response.data.mealCreatorName !== 'USER' && 
              response.data.mealTypeName !== null)
            currentModalStatus.value = 
              modalStatus.value.RECOMMEND_EDIT;
        } else {
          // 등록 모드: 초기화
          selectedFoods.value = [];
        }
        searchQuery.value = "";
      }
    },
    { immediate: true }
  );

  // [API] 식단 삭제
  const handleDelete = async (setMainData = null) => {
    await mealApi.deleteMealApi(selectedMeal.value.id);
    showDeleteDialog.value = false;
    closeModal();
    setMainData?.();
  };

  const addUpdateFoodLog = (food, updateType) => {
    updateFoodLog.value.push({ foodId: food.id, updateType, amount: food.amount });
  }

  // 수정됨: 10단위 추가
  const addFood = (food) => {
    const existing = selectedFoods.value.find((f) => f.id === food.id);
    if (existing) {
      existing.amount += 10;
      addUpdateFoodLog(existing, "MODIFY");
    } else {
      selectedFoods.value.push({ ...food, amount: food.servingSize || 100 });
      const newFood = selectedFoods.value[selectedFoods.value.length-1];
      addUpdateFoodLog(newFood, "ADD");
    }
    parseToTotalAPI();
  };

  const removeFood = (id) => {
    const food = selectedFoods.value.find((f) => f.id === id);
    if (food) {
        addUpdateFoodLog({ ...food, amount: null }, "REMOVE");
        selectedFoods.value = selectedFoods.value.filter((f) => f.id !== id);
        parseToTotalAPI();
    }
  };

  // 수정됨: 0까지 허용 및 직접 입력(delta가 아닌 절대값) 대응 가능하도록 개선
  const updateAmount = (id, deltaOrValue, isAbsolute = false) => {
    const food = selectedFoods.value.find((f) => f.id === id);
    if (food) {
      if (isAbsolute) {
        food.amount = Math.max(0, deltaOrValue);
      } else {
        food.amount = Math.max(0, food.amount + deltaOrValue);
      }
      addUpdateFoodLog(food, "MODIFY");
      parseToTotalAPI();
    }
  };

  const parseToTotalAPI = async () => {
    if (selectedFoods.value.length === 0) {
      totalNutrition.value = { point: 0, calories: 0, protein: 0, carbs: 0, fat: 0 };
      return;
    }
    const isSnack = selectedMealType.value === 'snack';
    const foods = selectedFoods.value.map(food => ({ foodId: food.id, amount: food.amount }));
    const response = await mealApi.totalFoodApi({ isSnack, foods });
    totalNutrition.value = {
      point: response.data.totalPoint,
      calories: response.data.totalCalorie,
      protein: response.data.totalCarbohydrate,
      carbs: response.data.totalProtein,
      fat: response.data.totalFat
    };
  }

  const openDeleteDialog = () => { showDeleteDialog.value = true; }

  const closeModal = () => {
    currentModalStatus.value = null;
    updateFoodLog.value = [];
    searchQuery.value = "";
    selectedFoods.value = [];
    selectedMeal.value = null;
    selectedMealType.value = null;
    isRegistrationOpen.value = false;
  }

  const setMealData = () => { selectedDate.value = new Date(); }

  const openApplyModal = (data) => {
    selectedMealType.value = null;
    currentModalStatus.value = modalStatus.value.RECOMMEND;
    selectedMeal.value = data;
    isRegistrationOpen.value = true;
  }

  return {
    selectedDate, 
    handleSelectDate, 
    getNextMonth, 
    getPrevMonth, 
    isGroupMember,
    datesWithMeals, 
    handleDelete, 
    selectedMealsData, 
    isRegistrationOpen,
    selectedMeal, 
    selectedMealType, 
    mealSlots, 
    openModal, 
    searchQuery,
    selectedFoods, 
    closeModal, 
    showDeleteDialog, 
    mealTypeLabel, 
    addFood,
    removeFood, 
    updateAmount, 
    totalNutrition, 
    filteredFoods, 
    openDeleteDialog,
    setMealData, 
    openApplyModal, 
    currentModalStatus
  }
});
export default useMealView;