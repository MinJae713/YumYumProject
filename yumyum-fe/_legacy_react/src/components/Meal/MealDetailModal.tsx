import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Card } from "./ui/card";
import { Coffee, Sun, Moon, Cookie, Flame, Pizza, Edit } from "lucide-react";
import { PieChart, Pie, Cell, ResponsiveContainer, Legend } from "recharts";
import { MealData } from "./MealPage";

interface MealDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  onEdit: () => void;
  mealData: MealData | null;
  date: Date;
}

export function MealDetailModal({
  isOpen,
  onClose,
  onEdit,
  mealData,
  date,
}: MealDetailModalProps) {
  if (!mealData) return null;

  const mealTypeConfig = {
    breakfast: { label: "아침", icon: Coffee, color: "text-[#f97316]", bgColor: "bg-[#f97316]/10" },
    lunch: { label: "점심", icon: Sun, color: "text-[#eab308]", bgColor: "bg-[#eab308]/10" },
    dinner: { label: "저녁", icon: Moon, color: "text-[#8b5cf6]", bgColor: "bg-[#8b5cf6]/10" },
    snack: { label: "간식", icon: Cookie, color: "text-[#ec4899]", bgColor: "bg-[#ec4899]/10" },
  };

  const config = mealTypeConfig[mealData.type];
  const Icon = config.icon;

  // 영양소 비율 데이터
  const nutritionData = [
    { name: "탄수화물", value: mealData.carbs, color: "#3b82f6" },
    { name: "단백질", value: mealData.protein, color: "#10b981" },
    { name: "지방", value: mealData.fat, color: "#f59e0b" },
  ];

  const totalNutrition = mealData.carbs + mealData.protein + mealData.fat;

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-[600px] max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <div className="flex items-center gap-3 mb-2">
            <div className={`w-12 h-12 ${config.bgColor} rounded-lg flex items-center justify-center`}>
              <Icon className={`w-6 h-6 ${config.color}`} />
            </div>
            <div className="flex-1">
              <Badge variant="outline" className="mb-1">
                {config.label}
              </Badge>
              <DialogTitle>식단 상세 정보</DialogTitle>
            </div>
          </div>
          <DialogDescription>
            {date.toLocaleDateString("ko-KR", {
              month: "long",
              day: "numeric",
              weekday: "long",
            })}
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6 py-4">
          {/* 총 칼로리 */}
          <Card className="p-4 bg-gradient-to-r from-primary/10 to-secondary/10 border-primary/20">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 bg-primary/20 rounded-lg flex items-center justify-center">
                <Flame className="w-6 h-6 text-primary" />
              </div>
              <div>
                <div className="text-sm text-muted-foreground">총 칼로리</div>
                <div className="text-2xl">{mealData.totalCalories} kcal</div>
              </div>
            </div>
          </Card>

          {/* 영양소 비율 (원형 그래프) */}
          <div>
            <h4 className="mb-3">영양소 구성 비율</h4>
            <div className="grid md:grid-cols-2 gap-4">
              <div className="h-[200px]">
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={nutritionData}
                      cx="50%"
                      cy="50%"
                      innerRadius={50}
                      outerRadius={80}
                      paddingAngle={5}
                      dataKey="value"
                    >
                      {nutritionData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.color} />
                      ))}
                    </Pie>
                    <Legend />
                  </PieChart>
                </ResponsiveContainer>
              </div>
              <div className="space-y-3">
                <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
                  <div className="flex items-center gap-2">
                    <div className="w-3 h-3 rounded-full bg-blue-500"></div>
                    <span className="text-sm">탄수화물</span>
                  </div>
                  <div>
                    <span className="text-sm mr-2">{mealData.carbs}g</span>
                    <span className="text-xs text-muted-foreground">
                      ({Math.round((mealData.carbs / totalNutrition) * 100)}%)
                    </span>
                  </div>
                </div>
                <div className="flex items-center justify-between p-3 bg-green-50 rounded-lg">
                  <div className="flex items-center gap-2">
                    <div className="w-3 h-3 rounded-full bg-green-500"></div>
                    <span className="text-sm">단백질</span>
                  </div>
                  <div>
                    <span className="text-sm mr-2">{mealData.protein}g</span>
                    <span className="text-xs text-muted-foreground">
                      ({Math.round((mealData.protein / totalNutrition) * 100)}%)
                    </span>
                  </div>
                </div>
                <div className="flex items-center justify-between p-3 bg-orange-50 rounded-lg">
                  <div className="flex items-center gap-2">
                    <div className="w-3 h-3 rounded-full bg-orange-500"></div>
                    <span className="text-sm">지방</span>
                  </div>
                  <div>
                    <span className="text-sm mr-2">{mealData.fat}g</span>
                    <span className="text-xs text-muted-foreground">
                      ({Math.round((mealData.fat / totalNutrition) * 100)}%)
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* 식단 구성 목록 */}
          <div>
            <div className="flex items-center gap-2 mb-3">
              <Pizza className="w-5 h-5 text-primary" />
              <h4>식단 구성</h4>
            </div>
            <Card className="p-4">
              <div className="space-y-2">
                {mealData.foods.map((food, index) => (
                  <div
                    key={index}
                    className="flex items-center justify-between py-2 border-b last:border-0"
                  >
                    <div className="flex items-center gap-2">
                      <div className="w-2 h-2 rounded-full bg-primary"></div>
                      <span className="text-sm">{food.name}</span>
                    </div>
                    <span className="text-sm text-muted-foreground">
                      {food.amount}
                      {food.unit}
                    </span>
                  </div>
                ))}
              </div>
            </Card>
          </div>

          {/* 하단 버튼 */}
          <div className="flex gap-2">
            <Button variant="outline" className="flex-1" onClick={onClose}>
              닫기
            </Button>
            <Button className="flex-1" onClick={onEdit}>
              <Edit className="w-4 h-4 mr-2" />
              수정하기
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
