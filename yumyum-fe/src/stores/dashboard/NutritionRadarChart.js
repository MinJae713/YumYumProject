import { defineStore } from "pinia";
import { computed, ref } from "vue";

const useNutritionRadarChart = defineStore("nutritionRadarChart", () => {
  const isEmptyRecord = ref(true);
  const nutritionComparisonData = ref([
    { subject: "칼로리", actual: 0, recommended: 100 },
    { subject: "탄수화물", actual: 0, recommended: 100 },
    { subject: "단백질", actual: 0, recommended: 100 },
    { subject: "지방", actual: 0, recommended: 100 },
    { subject: "식이섬유", actual: 0, recommended: 100 },
    { subject: "칼슘", actual: 0, recommended: 100 },
  ]);

  const averageAchievement = computed(() =>
    Math.round(
      nutritionComparisonData.value.reduce((sum, item) => sum + item.actual, 0) /
      nutritionComparisonData.value.length,
    ),
  );

  const chartData = computed(() => ({
    labels: nutritionComparisonData.value.map((item) => item.subject),
    datasets: [
      {
        label: "권장량",
        data: nutritionComparisonData.value.map((item) => item.recommended),
        backgroundColor: "rgba(203, 213, 225, 0.4)",
        borderColor: "#cbd5e1",
        pointBackgroundColor: "#cbd5e1",
        pointBorderColor: "#cbd5e1",
        borderWidth: 2,
        fill: true,
      },
      {
        label: "실제 섭취",
        data: nutritionComparisonData.value.map((item) => item.actual),
        backgroundColor: "rgba(16, 185, 129, 0.2)",
        borderColor: "#10b981",
        pointBackgroundColor: "#10b981",
        pointBorderColor: "#10b981",
        borderWidth: 2,
        fill: true,
      },
    ],
  }));

  const chartOptions = ref({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: "bottom",
      },
      tooltip: {
        callbacks: {
          label: (context) => `${context.dataset.label}: ${context.formattedValue}%`,
        },
      },
    },
    scales: {
      r: {
        min: 0,
        max: 100,
        ticks: {
          stepSize: 20,
          backdropColor: "transparent",
        },
        grid: {
          color: "rgba(0,0,0,0.08)",
        },
        angleLines: {
          color: "rgba(0,0,0,0.08)",
        },
      },
    },
  });
  const setNutritionRadarChartData = (dailyIntakeRelativeToRecommended) => {
    const calorie = dailyIntakeRelativeToRecommended.calorie;
    nutritionComparisonData.value[0].actual = Math.round(calorie.rate*1000)/10;

    const carbohydrate = dailyIntakeRelativeToRecommended.carbohydrate;
    nutritionComparisonData.value[1].actual = Math.round(carbohydrate.rate*1000)/10;

    const protein = dailyIntakeRelativeToRecommended.protein;
    nutritionComparisonData.value[2].actual = Math.round(protein.rate*1000)/10;

    const fat = dailyIntakeRelativeToRecommended.fat;
    nutritionComparisonData.value[3].actual = Math.round(fat.rate*1000)/10;

    const fiber = dailyIntakeRelativeToRecommended.fiber;
    nutritionComparisonData.value[4].actual = Math.round(fiber.rate*1000)/10;

    const calcium = dailyIntakeRelativeToRecommended.calcium;
    nutritionComparisonData.value[5].actual = Math.round(calcium.rate*1000)/10;
    isEmptyRecord.value = 
      calorie.intake === 0 || 
      carbohydrate.intake === 0 ||
      protein.intake === 0 ||
      fat.intake === 0;
  }
  return {
    isEmptyRecord,
    nutritionComparisonData,
    averageAchievement,
    chartData,
    chartOptions,
    setNutritionRadarChartData
  }
});
export default useNutritionRadarChart;