import { TrendingDown, TrendingUp, Minus } from "lucide-vue-next";

// 목표(Goal) 영문 -> 한글 변환
export const formatGoalLabel = (goal) => {
  if (!goal) return "";
  const lowerGoal = goal.toLowerCase();
  switch (lowerGoal) {
    case "diet":
      return "다이어트";
    case "gain":
      return "증량";
    case "maintain":
      return "유지";
    default:
      return goal;
  }
};

// 목표에 따른 뱃지 색상 클래스 반환
export const getGoalColorClass = (goal) => {
  if (!goal) return "";
  const lowerGoal = goal.toLowerCase();
  switch (lowerGoal) {
    case "diet":
      return "bg-secondary/10 text-secondary";
    case "bulk":
    case "bulkup":
      return "bg-accent/10 text-accent";
    case "maintain":
    case "maintenance":
      return "bg-primary/10 text-primary";
    default:
      return "";
  }
};

// 숫자 포맷팅 (소수점 2자리, 불필요한 0 제거)
export const formatNumber = (val) => {
  if (val === undefined || val === null) return "0";
  const num = Number(val);
  if (isNaN(num)) return "0";
  return parseFloat(num.toFixed(2));
};

// 날짜 포맷팅 (YYYY-MM-DD)
export const formatDateKey = (date) => {
  if (!date) return "";
  const d = new Date(date);
  if (isNaN(d.getTime())) return "";
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
};

// 체중 변화에 따른 아이콘 반환
export const getWeightChangeIcon = (currentWeight, startWeight) => {
  const weightChange = (currentWeight || 0) - (startWeight || 0);
  if (weightChange < 0) return TrendingDown;
  if (weightChange > 0) return TrendingUp;
  return Minus;
};

// 식단의 총 영양소 계산
export const calculateTotalNutrients = (foods) => {
  if (!foods?.length) {
    return { carbohydrate: 0, protein: 0, fat: 0, calorie: 0 };
  }
  return foods.reduce(
    (acc, food) => ({
      carbohydrate: acc.carbohydrate + (food.carbohydrate ?? 0),
      protein: acc.protein + (food.protein ?? 0),
      fat: acc.fat + (food.fat ?? 0),
      calorie: acc.calorie + (food.calorie ?? 0),
    }),
    { carbohydrate: 0, protein: 0, fat: 0, calorie: 0 },
  );
};
