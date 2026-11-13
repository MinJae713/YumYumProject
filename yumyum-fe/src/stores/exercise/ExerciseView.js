import { defineStore } from "pinia";
import { computed, ref, watch } from "vue";
import { useExerciseApi } from "@/lib/api/exercise";
import { formatDateKey } from "@/utils/commonUtils";
import { getExerciseKey } from "@/utils/exerciseUtils";

const useExerciseView = defineStore("exerciseView", () => {

  const exerciseApi = useExerciseApi();

  // data
  const selectedDate = ref(new Date()); // calendar - 선택 날짜

  const exerciseCategory = ref([
    { id: 1, label: '근력 운동'},
    { id: 2, label: '기타'},
    { id: 3, label: '유산소 운동'},
    { id: 4, label: '맨몸 운동'},
  ]); // 운동 종류 목록 -> 임시로 위와 같이 지정
  // refactor 시 페이지 열릴 때 서버에서 이 카테고리를 가져와야 함 (DTO 수정)
  const exerciseCatalog = ref([]); // 종류별 운동 목록

  const datesWithExercise = ref([]); // 운동 기록이 있는 날짜 목록

  const exercises = ref([]); // 그 날 운동 목록은 여기
  
  const totalDuration = computed(() => // 총 운동 시간
    exercises.value.reduce((sum, ex) => sum + (ex.durationMin || 0), 0));
  const totalCalories = computed(() => // 총 칼로리
    (exercises.value || []).reduce((sum, ex) => sum + (ex.calories || 0), 0),
  );
  const totalPoints = computed(() => // 총 점수
    (exercises.value || []).reduce((sum, ex) => sum + (ex.points || 0), 0),
  );
  
  const modalOpen = ref(false); // 모달 열림

  // 운동 칼로리 및 운동 점수
  const calculateCalories = ref(0);
  const calculatePoints = ref(0);

  // 모달 창에서 다뤄지는 운동 데이터는 얘로 처리
  // 운동 추가 시 추가 정보는 여기로 드감
  // 운동 수정 시 기존 정보가 selectedExercise에 있었다면,
  // 모달 창 띄울 때 해당 exericse 정보는 여기 exerciseData로 드감
  const exerciseData = ref({
    id: null,
    categoryId: null,
    type: "",
    catalogId: null,
    name: "",
    setNo: undefined,
    reps: undefined,
    weight: undefined,
    duration: 1,
  });

  const showDeleteDialog = ref(false); 
  // 정말 삭제하겠습니까? 표시

  const selectedExercise = ref(null); // 선택된 운동 (모달 열 때 지정됨)
  // 조회된 DB 버전의 운동 데이터는 여기 있다고 보면 됨
  // 이거 parsing 해서 exerciseData가 만들어짐

  const isGroupMember = ref(false); // 그룹 멤버인지 여부
  const normalizeRecordedDates = (recordedDates = []) =>
    (recordedDates || [])
      .map((dateKey) => {
        if (!dateKey) return null;
        if (dateKey instanceof Date) return dateKey;
        if (typeof dateKey === "string") {
          const [year, month, day] = dateKey.split("-").map(Number);
          if (!Number.isNaN(year) && !Number.isNaN(month) && !Number.isNaN(day)) {
            return new Date(year, month - 1, day);
          }
        }
        const parsed = new Date(dateKey);
        return Number.isNaN(parsed.getTime()) ? null : parsed;
      })
      .filter(Boolean);

  const fetchMonthRecords = async (dateInput = selectedDate.value) => {
    let date = dateInput;
    if (typeof date === "string") date = new Date(date);
    const response = await exerciseApi.getMonthRecordApi(
      date.getFullYear(),
      date.getMonth() + 1
    );
    const payload = response?.data ?? response;
    const recordedDates =
      payload?.recordedDates ??
      payload?.data?.recordedDates ??
      payload?.data ??
      payload ??
      [];
    datesWithExercise.value = normalizeRecordedDates(recordedDates);
  };

  // operations
  // ExerciseCalendar
  watch(() => [selectedDate.value, modalOpen.value], async () => { // [API] 선택 날짜 운동 조회
    const todayStr = formatDateKey(new Date());
    // 운동 목록 지정
    exercises.value = [];
    if (selectedDate.value > todayStr) return;
    const response = await exerciseApi.getExercisesOfDateApi(selectedDate.value);
    response.data.exercises.forEach(exercise => {
      if (exercise.points) isGroupMember.value = true;
      exercises.value.push({
        id: exercise.exerciseRecordId,
        type: getExerciseKey(exercise.exerciseCategoryName),
        name: exercise.exerciseName,
        calories: exercise.calories,
        points: exercise.points,
        exerciseOptionDetail: exercise.exerciseOptionDetail,
        durationMin: exercise.durationMin
      })
    });
  });

  const getNextMonth = async () => { // 다음 달 선택
    let date = selectedDate.value;
    if ((typeof date) === 'string') 
      date = new Date(date);
    date.setMonth(date.getMonth()+1);
    date.setDate(1);
    await fetchMonthRecords(date);
    handleSelectDate(formatDateKey(date));
  }
  const getPrevMonth = async () => { // 이전 달 선택
    let date = selectedDate.value;
    if ((typeof date) === 'string') 
      date = new Date(date);
    date.setMonth(date.getMonth()-1);
    date.setDate(1);
    await fetchMonthRecords(date);
    handleSelectDate(formatDateKey(date));
  }

  const handleSelectDate = (date) => { // 날짜 선택
    if (date) selectedDate.value = date;
  };
  
  // DailyExerciseList
  const openModal = (exercise = null) => { 
    // 모달 열 때 동작 -> 이 때 선택 운동 지정
    selectedExercise.value = exercise;
    modalOpen.value = true;
  };

  watch( // [API] 운동 세부 조회
    () => [selectedExercise.value, modalOpen.value],
    async ([selectedExercise, modalOpen]) => {
      if (selectedExercise && modalOpen) { // 운동 선택 및 모달 열릴 시
        const response = await exerciseApi.getExerciseDetailApi(selectedExercise.id);
        const exerciseCategoryList = response.data.exerciseCategoryList;
        const exerciseCategoryIndex = response.data.exerciseCategoryIndex;
        const exerciseList = response.data.exerciseList;
        const exerciseIndex = response.data.exerciseIndex;
        
        const selectedCategory = exerciseCategoryList[exerciseCategoryIndex];
        const selectedCatalog = exerciseList[exerciseIndex];

        // 운동 카테고리 초기화 -> DB에서 가져온 목록으로 초기화
        exerciseCategory.value = [];
        exerciseCategoryList.forEach(category => exerciseCategory.value.push({
          id: category.exerciseCategoryId,
          label: category.exerciseCategoryName
        }));

        // 운동 목록 초기화 -> DB에서 가져온 목록으로 초기화
        exerciseCatalog.value = [];
        exerciseList.forEach(exercise => exerciseCatalog.value.push({
          exerciseId: exercise.exerciseId,
          exerciseName: exercise.exerciseName
        }));

        // 선택 운동 정보 입력
        const selectedExerciseData = {
          id: selectedExercise.id,
          categoryId: selectedCategory.exerciseCategoryId,
          type: getExerciseKey(selectedCategory.exerciseCategoryName),
          catalogId: selectedCatalog.exerciseId,
          name: selectedCatalog.exerciseName,
          setNo: response.data.setNo,
          reps: response.data.reps,
          weight: response.data.weightKg,
          duration: response.data.durationMin
        }

        exerciseData.value = selectedExerciseData;
      } else if (!selectedExercise && modalOpen) { // 그게 아니면
        resetForm();
      }
    }
  );

  // ExerciseRegistrationModal
  watch(() => [exerciseData.value.duration], async ([durationMin]) => {
    // [API] 운동 점수 계산
    // 입력을 양수로만 받게 하고 싶으나.... 쉽지 않음 (해결해야 할 사항)
    // 됐다!
    if (durationMin < 0) exerciseData.data.duration = 0;
    const exerciseId = exerciseData.value.catalogId;
    if (exerciseId && checkSubmitAble()) {
      const response = await exerciseApi.scoreExerciseApi({
        exerciseId, durationMin
      });
      const calories = response.data.calories;
      calculateCalories.value = calories ? Math.round(calories) : null;
      calculatePoints.value = response.data.points;
    }
  });

  const checkSubmitAble = () => {
    const type = exerciseData.value.type;
    const setNo = exerciseData.value.setNo;
    const reps = exerciseData.value.reps;
    const weightKg = exerciseData.value.weight;
    const duration = exerciseData.value.duration;
    if (calculateCalories.value == null || 
        duration === null || duration <= 0) return false;
    // strength, cardio, bodyweight, etc

    switch (type) {
      case 'strength':
        return setNo !== null && setNo > 0 && 
                reps !== null && reps > 0 && 
                weightKg !== null && weightKg > 0;
      case 'bodyweight':
        return setNo !== null && setNo > 0 && 
                reps !== null && reps > 0;
    }
    return true;
  }

  const onSelectedCategory = async () => { // [API] 운동 목록 조회
    // exerciseDate, 칼로리 및 점수 초기화
    const id = exerciseData.value.id;
    const categoryId = exerciseData.value.categoryId;
    const category = exerciseCategory.value.find(category => category.id === categoryId);
    const type = getExerciseKey(category.label);
    exerciseData.value = {
      id,
      categoryId,
      type,
      catalogId: null,
      name: "",
      setNo: undefined,
      reps: undefined,
      weight: undefined,
      duration: 1,
    };
    calculateCalories.value = 0;
    calculatePoints.value = 0;

    // exerciseCatalog(카테고리 따른 운동 목록) 재지정
    const response = await exerciseApi.getExerciseListApi(categoryId);
    exerciseCatalog.value = [];
    response.data.forEach(catalog => exerciseCatalog.value.push({
      exerciseId: catalog.exerciseId,
      exerciseName: catalog.exerciseName
    }));
  }

  const onSelectedCatalog = () => { // 운동 선택
    // 실행 정보, 칼로리 및 점수 초기화 (그냥 초기화..? 좀 고민임)
    exerciseData.value.setNo = undefined;
    exerciseData.value.reps = undefined;
    exerciseData.value.weight = undefined;
    exerciseData.value.duration = 1;
    calculateCalories.value = 0;
    calculatePoints.value = 0;

    // 운동 선택 (name 지정)
    const catalogId = exerciseData.value.catalogId;
    const catalog = exerciseCatalog.value.find(catalog => catalog.exerciseId === catalogId);
    const name = catalog.exerciseName;
    exerciseData.value.name = name;

    // 같은 날짜에 동일한 운동이 이미 있으면 수정 모드로 전환
    const duplicate = (exercises.value || []).find(
      (exercise) => exercise.name === name,
    );
    if (duplicate) {
      selectedExercise.value = duplicate;
      exerciseData.value.id = duplicate.id;
    } else {
      selectedExercise.value = null;
      exerciseData.value.id = null;
    }
  }

  const closeModal = () => { // 모달 닫음
    modalOpen.value = false;
    selectedExercise.value = null;
  };

  const handleCancle = () => { // 취소
    resetForm(); 
    closeModal(); 
  }

  const resetForm = () => {
    // 모달 폼 초기화
    exerciseData.value = {
      id: null,
      categoryId: null,
      type: "",
      catalogId: null,
      name: "",
      setNo: undefined,
      reps: undefined,
      weight: undefined,
      duration: 1,
    };
  };

  const handleSubmit = (setMainData = null) => {
    if (calculateCalories.value <= 0) {
      console.warn("칼로리가 계산되지 않아 저장을 중단합니다.");
      return;
    }
    if (selectedExercise.value) handleEdit();
    else handleSave();
    setMainData?.();
  }

  const handleEdit = async () => { // [API] 운동 수정
    const exerciseRecordId = exerciseData.value.id;
    const exerciseId = exerciseData.value.catalogId;
    const calories = calculateCalories.value;
    const points = calculatePoints.value;
    const setNo = exerciseData.value.setNo;
    const reps = exerciseData.value.reps;
    const weightKg = exerciseData.value.weight;
    const durationMin = exerciseData.value.duration;
    await exerciseApi.updateExerciseApi({
      exerciseRecordId,
      exerciseId,
      calories,
      points,
      setNo,
      reps,
      weightKg,
      durationMin
    });
    handleCancle();
  }

  const handleSave = async () => { // [API] 운동 추가
    const exerciseDate = selectedDate.value;
    const exerciseId = exerciseData.value.catalogId;
    const calories = calculateCalories.value;
    const points = calculatePoints.value;
    const setNo = exerciseData.value.setNo;
    const reps = exerciseData.value.reps;
    const weightKg = exerciseData.value.weight;
    const durationMin = exerciseData.value.duration;
    await exerciseApi.addExerciseApi({
      exerciseDate,
      exerciseId,
      calories,
      points,
      setNo,
      reps,
      weightKg,
      durationMin
    });
    handleCancle();
  };

  const handleDelete = async (setMainData = null) => { // [API] 운동 삭제
    await exerciseApi.deleteExerciseApi(exerciseData.value.id);
    showDeleteDialog.value = false;
    handleCancle();
    setMainData?.();
  };

  const setExerciseData = async () => {
    const today = formatDateKey(new Date());
    selectedDate.value = today;
    await fetchMonthRecords(today);
  }

  return {
    selectedDate,
    isGroupMember,
    exerciseCategory,
    exerciseCatalog,
    handleSelectDate,
    datesWithExercise,
    getNextMonth,
    getPrevMonth,
    handleSubmit,
    handleDelete,
    handleCancle,

    modalOpen,
    exercises,
    totalDuration,
    totalCalories,
    totalPoints,
    selectedExercise,
    openModal,
    onSelectedCategory,
    onSelectedCatalog,
    closeModal,

    checkSubmitAble,
    exerciseData,
    showDeleteDialog,
    resetForm,
    calculateCalories,
    calculatePoints,

    setExerciseData
  }
});
export default useExerciseView;
