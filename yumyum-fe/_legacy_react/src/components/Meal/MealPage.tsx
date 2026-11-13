import { useState } from "react";
import { MealCalendar } from "./MealCalendar";
import { DailyMealList } from "./DailyMealList";
import { Utensils } from "lucide-react";

export interface MealData {
  type: "breakfast" | "lunch" | "dinner" | "snack";
  foods: Array<{
    id: string;
    name: string;
    amount: number;
    unit: string;
  }>;
  totalCalories: number;
  protein: number;
  carbs: number;
  fat: number;
  points?: number;
}

export interface DailyMeals {
  [dateKey: string]: {
    breakfast?: MealData;
    lunch?: MealData;
    dinner?: MealData;
    snack?: MealData;
  };
}

export function MealPage() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [mealsData, setMealsData] = useState<DailyMeals>({
    // 초기 샘플 데이터
    "2025-11-5": {
      breakfast: {
        type: "breakfast",
        foods: [
          { id: "1", name: "닭가슴살", amount: 150, unit: "g" },
          { id: "2", name: "브로콜리", amount: 100, unit: "g" },
          { id: "3", name: "현미밥", amount: 100, unit: "g" },
        ],
        totalCalories: 420,
        protein: 48,
        carbs: 39,
        fat: 6.2,
        points: 14,
      },
      lunch: {
        type: "lunch",
        foods: [
          { id: "1", name: "연어구이", amount: 120, unit: "g" },
          { id: "2", name: "시금치나물", amount: 80, unit: "g" },
          { id: "3", name: "된장국", amount: 1, unit: "그릇" },
        ],
        totalCalories: 380,
        protein: 35,
        carbs: 28,
        fat: 14,
        points: 13,
      },
    },
  });

  const formatDateKey = (date: Date) => {
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
  };

  const handleAddMeal = (
    mealType: "breakfast" | "lunch" | "dinner" | "snack",
    mealData: Omit<MealData, "type">
  ) => {
    const dateKey = formatDateKey(selectedDate);
    setMealsData((prev) => ({
      ...prev,
      [dateKey]: {
        ...prev[dateKey],
        [mealType]: {
          type: mealType,
          ...mealData,
        },
      },
    }));
  };

  const handleDeleteMeal = (mealType: "breakfast" | "lunch" | "dinner" | "snack") => {
    const dateKey = formatDateKey(selectedDate);
    setMealsData((prev) => {
      const dayMeals = { ...prev[dateKey] };
      delete dayMeals[mealType];
      
      // 해당 날짜에 식단이 하나도 없으면 날짜 자체를 삭제
      if (Object.keys(dayMeals).length === 0) {
        const newData = { ...prev };
        delete newData[dateKey];
        return newData;
      }
      
      return {
        ...prev,
        [dateKey]: dayMeals,
      };
    });
  };

  const currentDayMeals = mealsData[formatDateKey(selectedDate)] || {};

  return (
    <div className="max-w-7xl mx-auto px-4 py-6">
      {/* 헤더 */}
      <div className="flex items-center gap-3 mb-6">
        <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
          <Utensils className="w-6 h-6 text-primary" />
        </div>
        <div>
          <h1>식단 관리</h1>
          <p className="text-sm text-muted-foreground">
            날짜별 식단을 기록하고 영양소를 관리하세요
          </p>
        </div>
      </div>

      {/* 2단 레이아웃: 캘린더 + 식단 목록 */}
      <div className="grid lg:grid-cols-[400px_1fr] gap-6">
        {/* 좌측: 캘린더 */}
        <div>
          <MealCalendar
            selectedDate={selectedDate}
            onSelectDate={(date) => date && setSelectedDate(date)}
            mealsData={mealsData}
          />
        </div>

        {/* 우측: 식단 목록 */}
        <div>
          <DailyMealList
            selectedDate={selectedDate}
            meals={currentDayMeals}
            onAddMeal={handleAddMeal}
            onDeleteMeal={handleDeleteMeal}
          />
        </div>
      </div>
    </div>
  );
}