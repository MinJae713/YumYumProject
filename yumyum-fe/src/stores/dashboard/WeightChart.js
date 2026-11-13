import { useUserApi } from "@/lib/api/user";
import { defineStore } from "pinia";
import { computed, ref } from "vue";

const useWeightChart = defineStore("weightChart", () => {

  const userApi = useUserApi();

  const isEmptyRecord = ref(true);
  const weightData = ref([]);
  const currentWeight = ref(0);
  const startWeight = ref(0);
  const weightLoss = ref(0);
  const goalWeight = ref(0);
  const minWeight = ref(300);
  const maxWeight = ref(0);

  const chartData = computed(() => ({
    labels: weightData.value.map((item) => item.date),
    datasets: [
      {
        label: "체중",
        data: weightData.value.map((item) => item.weight),
        borderColor: "#10b981",
        backgroundColor: "rgba(16, 185, 129, 0.2)",
        tension: 0.3,
        fill: true,
        pointRadius: 4,
        pointBackgroundColor: "#10b981",
        pointBorderColor: "#10b981",
      },
      {
        label: "목표 체중",
        data: weightData.value.map((item) => item.goal),
        borderColor: "#f97316",
        backgroundColor: "transparent",
        borderDash: [6, 6],
        pointRadius: 0,
        borderWidth: 2,
        fill: false,
      },
    ],
  }));
  const chartOptions = computed(() => ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: "bottom",
      },
      tooltip: {
        mode: "index",
        intersect: false,
      },
    },
    scales: {
      y: {
        min: minWeight.value,
        max: maxWeight.value,
        ticks: {
          stepSize: 1,
          callback: (value) => `${value}kg`,
        },
        grid: {
          color: "rgba(0,0,0,0.05)",
        },
      },
      x: {
        grid: {
          display: false,
        },
      },
    },
  }));
  const setWeightChartData = (weightDelta) => {
    minWeight.value = 300;
    maxWeight.value = 0;
    weightData.value = [];
    const weightRecords = weightDelta.dateOfWeight;
    if (weightRecords.length == 0) {
      isEmptyRecord.value = true;
      return;
    }
    currentWeight.value = weightDelta.currentWeight;
    startWeight.value = weightDelta.startWeight;
    weightLoss.value = Math.round(weightDelta.lossWeight*10)/10;
    goalWeight.value = weightDelta.targetWeight;
    const startIndex = weightRecords.length - 5;
    weightRecords.filter((weightRecord, index) => index >= startIndex)
      .forEach(weightRecord => {
        if (minWeight.value > weightRecord.weight)
          minWeight.value = weightRecord.weight;
        if (maxWeight.value < weightRecord.weight)
          maxWeight.value = weightRecord.weight;
        weightData.value.push({
          date: weightRecord.recordedDate,
          weight: weightRecord.weight,
          goal: goalWeight.value
        });
      });
    maxWeight.value = (Math.round(maxWeight.value/10)+1)*10;
    minWeight.value = minWeight.value > goalWeight.value ? 
      goalWeight.value : minWeight.value;
    minWeight.value = (Math.round(minWeight.value/10)-1)*10;
    isEmptyRecord.value = false;
  }

  // WeightEditModal
  const modalOpen = ref(false);
  const date = ref("");
  const weight = ref(null);

  const openModal = () => {
    modalOpen.value = true;
  }

  const closeModal = () => {
    date.value = "";
    weight.value = null;
    modalOpen.value = false;
  };

  const onDateSelect = () => {
    const foundWeight = weightData.value.find(weight => weight.date === date.value);
    if (foundWeight) weight.value = foundWeight.weight;
    else weight.value = null;
  }

  const handleSubmit = async (setDashboardData) => {
    const submitWeight = {
      recordDate: date.value,
      currentWeight: weight.value,
    };
    await userApi.updateUserWeightApi(submitWeight);
    closeModal();
    setDashboardData();
  };

  return {
    isEmptyRecord,
    weightData,
    currentWeight,
    startWeight,
    weightLoss,
    goalWeight,
    chartData,
    chartOptions,
    setWeightChartData,

    modalOpen,
    date,
    weight,
    onDateSelect,
    openModal,
    closeModal,
    handleSubmit
  }
});
export default useWeightChart;