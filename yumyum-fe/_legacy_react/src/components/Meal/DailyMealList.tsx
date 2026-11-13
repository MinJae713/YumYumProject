import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Plus, Coffee, Sun, Moon, Cookie, Edit } from "lucide-react";
import { MealRegistrationModal } from "./MealRegistrationModal";
import { MealDetailModal } from "./MealDetailModal";
import { MealData } from "./MealPage";

interface MealSlot {
  type: "breakfast" | "lunch" | "dinner" | "snack";
  label: string;
  icon: any;
  iconColor: string;
  timeRange: string;
  data?: MealData;
}

interface DailyMealListProps {
  selectedDate: Date;
  meals: {
    breakfast?: MealData;
    lunch?: MealData;
    dinner?: MealData;
    snack?: MealData;
  };
  onAddMeal: (
    mealType: "breakfast" | "lunch" | "dinner" | "snack",
    mealData: Omit<MealData, "type">
  ) => void;
  onDeleteMeal: (mealType: "breakfast" | "lunch" | "dinner" | "snack") => void;
}

export function DailyMealList({ selectedDate, meals, onAddMeal, onDeleteMeal }: DailyMealListProps) {
  const [registrationModalState, setRegistrationModalState] = useState<{
    isOpen: boolean;
    mealType: "breakfast" | "lunch" | "dinner" | "snack";
    existingData?: MealData;
  }>({
    isOpen: false,
    mealType: "breakfast",
    existingData: undefined,
  });

  const [detailModalState, setDetailModalState] = useState<{
    isOpen: boolean;
    mealData: MealData | null;
  }>({
    isOpen: false,
    mealData: null,
  });

  const mealSlots: MealSlot[] = [
    {
      type: "breakfast",
      label: "아침",
      icon: Coffee,
      iconColor: "text-[#f97316]",
      timeRange: "07:00 - 09:00",
      data: meals.breakfast,
    },
    {
      type: "lunch",
      label: "점심",
      icon: Sun,
      iconColor: "text-[#eab308]",
      timeRange: "12:00 - 14:00",
      data: meals.lunch,
    },
    {
      type: "dinner",
      label: "저녁",
      icon: Moon,
      iconColor: "text-[#8b5cf6]",
      timeRange: "18:00 - 20:00",
      data: meals.dinner,
    },
    {
      type: "snack",
      label: "간식",
      icon: Cookie,
      iconColor: "text-[#ec4899]",
      timeRange: "언제든지",
      data: meals.snack,
    },
  ];

  // 상세 보기 모달 열기 (카드 배경 클릭)
  const openDetailModal = (mealData: MealData) => {
    setDetailModalState({ isOpen: true, mealData });
  };

  // 상세 보기 모달 닫기
  const closeDetailModal = () => {
    setDetailModalState({ isOpen: false, mealData: null });
  };

  // 등록/수정 모달 열기
  const openRegistrationModal = (
    mealType: "breakfast" | "lunch" | "dinner" | "snack",
    existingData?: MealData
  ) => {
    setRegistrationModalState({ isOpen: true, mealType, existingData });
  };

  // 등록/수정 모달 닫기
  const closeRegistrationModal = () => {
    setRegistrationModalState({ 
      isOpen: false, 
      mealType: registrationModalState.mealType, 
      existingData: undefined 
    });
  };

  // 상세 보기 모달에서 수정하기 버튼 클릭
  const handleEditFromDetail = () => {
    if (detailModalState.mealData) {
      closeDetailModal();
      openRegistrationModal(detailModalState.mealData.type, detailModalState.mealData);
    }
  };

  const totalDailyCalories = mealSlots.reduce(
    (sum, meal) => sum + (meal.data?.totalCalories || 0),
    0
  );

  const totalDailyPoints = mealSlots.reduce(
    (sum, meal) => sum + (meal.data?.points || 0),
    0
  );

  return (
    <>
      <Card className="p-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h3 className="mb-1">식단 목록</h3>
            <p className="text-xs text-muted-foreground">
              {selectedDate.toLocaleDateString("ko-KR", {
                month: "long",
                day: "numeric",
                weekday: "long",
              })}
            </p>
          </div>
          <div className="flex gap-3">
            <div className="text-right">
              <div className="text-xs text-muted-foreground mb-1">총 칼로리</div>
              <div className="text-2xl text-primary">{totalDailyCalories}</div>
              <div className="text-xs text-muted-foreground">kcal</div>
            </div>
            <div className="text-right">
              <div className="text-xs text-muted-foreground mb-1">획득 점수</div>
              <div className="text-2xl text-secondary">{totalDailyPoints}</div>
              <div className="text-xs text-muted-foreground">points</div>
            </div>
          </div>
        </div>

        <div className="space-y-4">
          {mealSlots.map((meal) => {
            const Icon = meal.icon;
            const hasData = !!meal.data;

            return (
              <Card key={meal.type} className="p-4 bg-muted/20">
                <div className="flex items-start gap-3">
                  <div
                    className={`w-12 h-12 rounded-lg bg-card border border-border flex items-center justify-center ${meal.iconColor} flex-shrink-0`}
                  >
                    <Icon className="w-6 h-6" />
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center justify-between mb-2">
                      <div>
                        <h4 className="text-sm mb-1">{meal.label}</h4>
                        <p className="text-xs text-muted-foreground">
                          {meal.timeRange}
                        </p>
                      </div>
                      {hasData && (
                        <div className="flex items-center gap-2">
                          <Badge variant="secondary" className="text-xs">
                            {meal.data.totalCalories} kcal
                          </Badge>
                          <Badge variant="outline" className="text-xs">
                            {meal.data.points || 0}P
                          </Badge>
                        </div>
                      )}
                    </div>

                    {hasData ? (
                      <div className="space-y-3">
                        {/* 카드 배경 영역 (상세보기) */}
                        <div 
                          className="space-y-3 cursor-pointer hover:bg-muted/30 -mx-2 -mt-2 p-2 rounded-lg transition-colors"
                          onClick={() => openDetailModal(meal.data!)}
                        >
                          <div className="space-y-1">
                            {meal.data.foods.slice(0, 3).map((food, index) => (
                              <div
                                key={index}
                                className="text-xs text-muted-foreground flex items-center gap-2"
                              >
                                <span className="text-primary">•</span>
                                <span>
                                  {food.name} {food.amount}
                                  {food.unit}
                                </span>
                              </div>
                            ))}
                            {meal.data.foods.length > 3 && (
                              <div className="text-xs text-muted-foreground pl-4">
                                외 {meal.data.foods.length - 3}개 항목
                              </div>
                            )}
                          </div>

                          <div className="flex items-center gap-4 text-xs">
                            <div className="flex items-center gap-1">
                              <span className="text-muted-foreground">단백질</span>
                              <span className="text-secondary">
                                {meal.data.protein}g
                              </span>
                            </div>
                            <div className="flex items-center gap-1">
                              <span className="text-muted-foreground">
                                탄수화물
                              </span>
                              <span className="text-accent">{meal.data.carbs}g</span>
                            </div>
                            <div className="flex items-center gap-1">
                              <span className="text-muted-foreground">지방</span>
                              <span>{meal.data.fat}g</span>
                            </div>
                          </div>

                          <div className="text-xs text-center text-primary pt-1">
                            클릭하여 상세보기
                          </div>
                        </div>

                        {/* 수정 버튼 */}
                        <Button
                          size="sm"
                          variant="outline"
                          className="w-full"
                          onClick={(e) => {
                            e.stopPropagation();
                            openRegistrationModal(meal.type, meal.data);
                          }}
                        >
                          <Edit className="w-3 h-3 mr-1" />
                          수정하기
                        </Button>
                      </div>
                    ) : (
                      <div className="flex flex-col items-center py-4">
                        <div className="w-12 h-12 rounded-full bg-muted flex items-center justify-center mb-2">
                          <Plus className="w-6 h-6 text-muted-foreground" />
                        </div>
                        <p className="text-xs text-muted-foreground mb-3">
                          등록된 식단이 없습니다
                        </p>
                        <Button size="sm" onClick={() => openRegistrationModal(meal.type)}>
                          <Plus className="w-3 h-3 mr-1" />
                          식단 추가
                        </Button>
                      </div>
                    )}
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      </Card>

      {/* 상세 보기 모달 (조회 전용) */}
      <MealDetailModal
        isOpen={detailModalState.isOpen}
        onClose={closeDetailModal}
        onEdit={handleEditFromDetail}
        mealData={detailModalState.mealData}
        date={selectedDate}
      />

      {/* 등록/수정 모달 */}
      <MealRegistrationModal
        isOpen={registrationModalState.isOpen}
        onClose={closeRegistrationModal}
        mealType={registrationModalState.mealType}
        date={selectedDate}
        onSave={(mealData) => onAddMeal(registrationModalState.mealType, mealData)}
        onDelete={() => onDeleteMeal(registrationModalState.mealType)}
        existingMeal={registrationModalState.existingData}
      />
    </>
  );
}