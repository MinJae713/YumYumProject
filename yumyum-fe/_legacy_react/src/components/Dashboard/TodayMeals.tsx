import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Plus, Coffee, Sun, Moon } from "lucide-react";
import { MealRegistrationModal } from "./MealRegistrationModal";
import type { MealData } from "./MealPage";

interface MealCardData {
  time: "breakfast" | "lunch" | "dinner";
  label: string;
  icon: any;
  iconColor: string;
  data?: MealData;
}

interface TodayMealsProps {
  onMealsChange?: (meals: { [key: string]: MealData }) => void;
}

export function TodayMeals({ onMealsChange }: TodayMealsProps) {
  const [mealsData, setMealsData] = useState<{
    breakfast?: MealData;
    lunch?: MealData;
    dinner?: MealData;
  }>({
    breakfast: {
      type: "breakfast",
      foods: [
        { id: "1", name: "닭가슴살", amount: 150, unit: "g" },
        { id: "2", name: "브로콜리", amount: 100, unit: "g" },
        { id: "3", name: "방울토마토", amount: 50, unit: "g" },
      ],
      totalCalories: 320,
      protein: 45,
      carbs: 12,
      fat: 5,
    },
    lunch: {
      type: "lunch",
      foods: [
        { id: "1", name: "현미밥", amount: 100, unit: "g" },
        { id: "2", name: "연어구이", amount: 120, unit: "g" },
        { id: "3", name: "시금치나물", amount: 80, unit: "g" },
        { id: "4", name: "된장국", amount: 1, unit: "그릇" },
      ],
      totalCalories: 580,
      protein: 40,
      carbs: 50,
      fat: 20,
    },
  });

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedMealType, setSelectedMealType] = useState<
    "breakfast" | "lunch" | "dinner" | "snack"
  >("breakfast");

  const meals: MealCardData[] = [
    {
      time: "breakfast",
      label: "아침",
      icon: Coffee,
      iconColor: "text-[#f97316]",
      data: mealsData.breakfast,
    },
    {
      time: "lunch",
      label: "점심",
      icon: Sun,
      iconColor: "text-[#eab308]",
      data: mealsData.lunch,
    },
    {
      time: "dinner",
      label: "저녁",
      icon: Moon,
      iconColor: "text-[#8b5cf6]",
      data: mealsData.dinner,
    },
  ];

  const handleOpenModal = (mealType: "breakfast" | "lunch" | "dinner") => {
    setSelectedMealType(mealType);
    setIsModalOpen(true);
  };

  const handleSaveMeal = (mealData: Omit<MealData, "type">) => {
    const newMealsData = {
      ...mealsData,
      [selectedMealType]: {
        type: selectedMealType,
        ...mealData,
      },
    };
    setMealsData(newMealsData);
    onMealsChange?.(newMealsData);
  };

  const handleDeleteMeal = () => {
    const newMealsData = { ...mealsData };
    delete newMealsData[selectedMealType];
    setMealsData(newMealsData);
    onMealsChange?.(newMealsData);
  };

  return (
    <>
      <div className="grid md:grid-cols-3 gap-4">
        {meals.map((meal) => {
          const Icon = meal.icon;

          return (
            <Card
              key={meal.time}
              className="p-6 transition-all hover:shadow-md cursor-pointer"
              onClick={() =>
                meal.data
                  ? handleOpenModal(meal.time)
                  : handleOpenModal(meal.time)
              }
            >
              <div className="flex items-center gap-3 mb-4">
                <div
                  className={`w-12 h-12 rounded-lg bg-muted/50 flex items-center justify-center ${meal.iconColor}`}
                >
                  <Icon className="w-6 h-6" />
                </div>
                <div>
                  <h4>{meal.label}</h4>
                  <p className="text-xs text-muted-foreground">
                    {meal.time === "breakfast" && "07:00 - 09:00"}
                    {meal.time === "lunch" && "12:00 - 14:00"}
                    {meal.time === "dinner" && "18:00 - 20:00"}
                  </p>
                </div>
              </div>

              {meal.data ? (
                <div className="space-y-4">
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <h4 className="text-sm">
                        {meal.data.foods.map((f) => f.name).join(", ")}
                      </h4>
                      <Badge variant="outline">
                        {meal.data.totalCalories} kcal
                      </Badge>
                    </div>
                    <div className="space-y-1">
                      {meal.data.foods.slice(0, 3).map((item) => (
                        <div
                          key={item.id}
                          className="text-xs text-muted-foreground flex items-start gap-2"
                        >
                          <span className="text-primary">•</span>
                          <span>
                            {item.name} {item.amount}
                            {item.unit}
                          </span>
                        </div>
                      ))}
                      {meal.data.foods.length > 3 && (
                        <div className="text-xs text-muted-foreground flex items-start gap-2">
                          <span className="text-primary">•</span>
                          <span>외 {meal.data.foods.length - 3}개</span>
                        </div>
                      )}
                    </div>
                  </div>
                  <div
                    className="text-xs text-primary hover:underline"
                    onClick={(e) => {
                      e.stopPropagation();
                      handleOpenModal(meal.time);
                    }}
                  >
                    수정하기
                  </div>
                </div>
              ) : (
                <div className="flex flex-col items-center justify-center py-8">
                  <div className="w-16 h-16 rounded-full bg-muted/50 flex items-center justify-center mb-3">
                    <Plus className="w-8 h-8 text-muted-foreground" />
                  </div>
                  <p className="text-sm text-muted-foreground mb-3">
                    등록된 식단이 없습니다
                  </p>
                  <Button
                    size="sm"
                    onClick={(e) => {
                      e.stopPropagation();
                      handleOpenModal(meal.time);
                    }}
                  >
                    <Plus className="w-4 h-4 mr-1" />
                    식단 추가
                  </Button>
                </div>
              )}
            </Card>
          );
        })}
      </div>

      {/* 식단 등록/수정 모달 */}
      <MealRegistrationModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        mealType={selectedMealType}
        date={new Date()}
        onSave={handleSaveMeal}
        onDelete={handleDeleteMeal}
        existingMeal={mealsData[selectedMealType]}
      />
    </>
  );
}
