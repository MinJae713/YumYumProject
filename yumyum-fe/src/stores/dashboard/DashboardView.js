import { defineStore } from "pinia";
import useBadgeSection from "./BadgeSection";
import useWeightChart from "./WeightChart";
import useNutritionDonutChart from "./NutritionDonutChart";
import useNutritionRadarChart from "./NutritionRadarChart";
import { useDashboardApi } from "@/lib/api/dashboard";
import { ref } from "vue";

const badgeSectionStore = useBadgeSection();
const weightChartStore = useWeightChart();
const nutritionDonutChartStore = useNutritionDonutChart();
const nutritionRadarChartStore = useNutritionRadarChart();

const setBadgeSectionData = badgeSectionStore.setBadgeSectionData;
const setWeightChartData = weightChartStore.setWeightChartData;
const setNutritionDonutChartData = nutritionDonutChartStore.setNutritionDonutChartData;
const setNutritionRadarChartData = nutritionRadarChartStore.setNutritionRadarChartData;

const useDashboard = defineStore("dashboard", () => {
  // 보류 부분 또한 여기서 받을 것
  const dashboardStore = useDashboardApi();

  const spendedCalorie = ref({});
  const totalDuration = ref({});
  const streakRecords = ref([]);

  const setDashboardData = async () => {
    const response = await dashboardStore.getDashboardPageApi();
    setBadgeSectionData(response.data.recentBadgeList);
    setWeightChartData(response.data.weightDelta);
    setNutritionDonutChartData(response.data.weeklyDailyNutrionIntakeRate);
    setNutritionRadarChartData(response.data.dailyIntakeRelativeToRecommended);

    spendedCalorie.value = {};
    const dailyAverageSpendedCalorie = response.data.dailyAverageSpendedCalorie;
    spendedCalorie.value.spendedAverageCalorie = 
      Math.round(dailyAverageSpendedCalorie.spendedAverageCalorie);
    spendedCalorie.value.percentFromLastWeek = 
      Math.round(dailyAverageSpendedCalorie.percentFromLastWeek*10000)/100;

    totalDuration.value = {};
    const weeklyTotalDuration = response.data.weeklyTotalDuration;
    totalDuration.value.exerciseTimeSameWeek = weeklyTotalDuration.exerciseTimeSameWeek;
    totalDuration.value.percentFromLastWeek = 
      Math.round(weeklyTotalDuration.percentFromLastWeek*1000)/10;

    streakRecords.value = [];
    const streakRecordList = response.data.streakRecordList;
    streakRecordList.forEach(record => streakRecords.value.push({
      name: record.name,
      longestStreak: record.longestStreak
    }));
  }
  return {
    spendedCalorie,
    totalDuration,
    streakRecords,
    setDashboardData
  }
});

export default useDashboard;