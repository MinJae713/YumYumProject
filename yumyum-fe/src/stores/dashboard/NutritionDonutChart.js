import { defineStore } from "pinia";
import { computed, ref } from "vue";

const useNutritionDonutChart = defineStore("nutritionDonutChart", () => {
  const isEmptyRecord = ref(true);
  const nutritionData = ref([]);

  const totalCalories = ref(0);

  const initNutritionData = () => {
    totalCalories.value = 0;
    nutritionData.value = [
      { name: "탄수화물", value: 0, intakeCalorie:0, color: "#3b82f6" },
      { name: "단백질", value: 0, intakeCalorie:0, color: "#10b981" },
      { name: "지방", value: 0, intakeCalorie:0, color: "#f97316" },
    ];
  }

  const chartData = computed(() => ({
    labels: nutritionData.value.map((item) => item.name),
    datasets: [
      {
        data: nutritionData.value.map((item) => item.value),
        backgroundColor: nutritionData.value.map((item) => item.color),
        borderColor: "#ffffff",
        borderWidth: 4,
        hoverOffset: 8,
      },
    ],
  }));

  const chartOptions = ref({
    responsive: true,
    maintainAspectRatio: false,
    cutout: "70%",
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          label: (context) => `${context.label}: ${context.formattedValue}%`,
        },
      },
    },
  });
  const setNutritionDonutChartData = (weeklyDailyNutrionIntakeRate) => {
    // const weekly = weeklyDailyNutrionIntakeRate.weekly;
    // 위는 주간 섭취 비율 => 혹시 필요할 지 몰라서 받아둠
    initNutritionData();
    const daily = weeklyDailyNutrionIntakeRate.daily;
    const carbohydrate = daily.carbohydrate;
    nutritionData.value[0].value = Math.round(carbohydrate.rate*1000)/10;
    nutritionData.value[0].intakeCalorie = Math.round(carbohydrate.intakeCalorie);
    totalCalories.value += Math.round(carbohydrate.intakeCalorie);
    const protein = daily.protein;
    nutritionData.value[1].value = Math.round(protein.rate*1000)/10;
    nutritionData.value[1].intakeCalorie = Math.round(protein.intakeCalorie);
    totalCalories.value += Math.round(protein.intakeCalorie);
    const fat = daily.fat;
    nutritionData.value[2].value = Math.round(fat.rate*1000)/10;
    nutritionData.value[2].intakeCalorie = Math.round(fat.intakeCalorie);
    totalCalories.value += Math.round(fat.intakeCalorie);
    isEmptyRecord.value = totalCalories.value === 0;
  }
  return {
    isEmptyRecord,
    nutritionData,
    totalCalories,
    chartData,
    chartOptions,
    setNutritionDonutChartData
  }
});
export default useNutritionDonutChart;