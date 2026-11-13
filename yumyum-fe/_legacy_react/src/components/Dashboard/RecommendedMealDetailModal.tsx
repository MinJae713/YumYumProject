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
import { Sparkles, Users, Flame, Pizza } from "lucide-react";
import { PieChart, Pie, Cell, ResponsiveContainer, Legend } from "recharts";

interface FoodItem {
  name: string;
  amount: number;
  unit: string;
}

interface MealRecommendation {
  id: string;
  title: string;
  type: "ai" | "group";
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
  description: string;
  tags: string[];
  foods: FoodItem[];
  trainerName?: string;
}

interface RecommendedMealDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  meal: MealRecommendation | null;
}

export function RecommendedMealDetailModal({
  isOpen,
  onClose,
  meal,
}: RecommendedMealDetailModalProps) {
  if (!meal) return null;

  // 탄단지 비율 데이터
  const nutritionData = [
    { name: "탄수화물", value: meal.carbs, color: "#3b82f6" },
    { name: "단백질", value: meal.protein, color: "#10b981" },
    { name: "지방", value: meal.fat, color: "#f59e0b" },
  ];

  const totalNutrition = meal.carbs + meal.protein + meal.fat;

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-[600px] max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <div className="flex items-center gap-3 mb-2">
            {meal.type === "ai" ? (
              <div className="w-12 h-12 bg-gradient-to-br from-primary to-secondary rounded-lg flex items-center justify-center">
                <Sparkles className="w-6 h-6 text-white" />
              </div>
            ) : (
              <div className="w-12 h-12 bg-gradient-to-br from-secondary to-accent rounded-lg flex items-center justify-center">
                <Users className="w-6 h-6 text-white" />
              </div>
            )}
            <div className="flex-1">
              <Badge
                variant={meal.type === "ai" ? "default" : "secondary"}
                className="mb-1"
              >
                {meal.type === "ai" ? "AI 추천" : "그룹장 추천"}
              </Badge>
              <DialogTitle>{meal.title}</DialogTitle>
            </div>
          </div>
          {meal.trainerName && (
            <p className="text-sm text-muted-foreground">
              추천인: {meal.trainerName}
            </p>
          )}
          <DialogDescription>{meal.description}</DialogDescription>
        </DialogHeader>

        <div className="space-y-6 py-4">
          {/* 태그 */}
          <div className="flex flex-wrap gap-2">
            {meal.tags.map((tag) => (
              <Badge key={tag} variant="outline">
                {tag}
              </Badge>
            ))}
          </div>

          {/* 총 칼로리 */}
          <Card className="p-4 bg-gradient-to-r from-primary/10 to-secondary/10 border-primary/20">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 bg-primary/20 rounded-lg flex items-center justify-center">
                <Flame className="w-6 h-6 text-primary" />
              </div>
              <div>
                <div className="text-sm text-muted-foreground">총 칼로리</div>
                <div className="text-2xl">{meal.calories} kcal</div>
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
                    <span className="text-sm mr-2">{meal.carbs}g</span>
                    <span className="text-xs text-muted-foreground">
                      ({Math.round((meal.carbs / totalNutrition) * 100)}%)
                    </span>
                  </div>
                </div>
                <div className="flex items-center justify-between p-3 bg-green-50 rounded-lg">
                  <div className="flex items-center gap-2">
                    <div className="w-3 h-3 rounded-full bg-green-500"></div>
                    <span className="text-sm">단백질</span>
                  </div>
                  <div>
                    <span className="text-sm mr-2">{meal.protein}g</span>
                    <span className="text-xs text-muted-foreground">
                      ({Math.round((meal.protein / totalNutrition) * 100)}%)
                    </span>
                  </div>
                </div>
                <div className="flex items-center justify-between p-3 bg-orange-50 rounded-lg">
                  <div className="flex items-center gap-2">
                    <div className="w-3 h-3 rounded-full bg-orange-500"></div>
                    <span className="text-sm">지방</span>
                  </div>
                  <div>
                    <span className="text-sm mr-2">{meal.fat}g</span>
                    <span className="text-xs text-muted-foreground">
                      ({Math.round((meal.fat / totalNutrition) * 100)}%)
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
                {meal.foods.map((food, index) => (
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
            <Button className="flex-1">이 식단 적용하기</Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
