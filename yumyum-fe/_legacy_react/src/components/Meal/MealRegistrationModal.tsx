import { useState, useEffect } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Badge } from "./ui/badge";
import { Card } from "./ui/card";
import { Search, Plus, Minus, X, Trash2, Flame, Pizza } from "lucide-react";
import { MealData } from "./MealPage";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./ui/alert-dialog";
import { PieChart, Pie, Cell, ResponsiveContainer, Legend } from "recharts";

interface MealRegistrationModalProps {
  isOpen: boolean;
  onClose: () => void;
  mealType: "breakfast" | "lunch" | "dinner" | "snack";
  date: Date;
  onSave: (mealData: {
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
    points: number;
  }) => void;
  onDelete: () => void;
  existingMeal?: MealData;
  customTitle?: string;
  customDescription?: string;
}

interface FoodItem {
  id: string;
  name: string;
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
  servingSize: number;
  servingUnit: string;
}

interface SelectedFood extends FoodItem {
  amount: number;
}

const mockFoodDatabase: FoodItem[] = [
  {
    id: "1",
    name: "닭가슴살",
    calories: 165,
    protein: 31,
    carbs: 0,
    fat: 3.6,
    servingSize: 100,
    servingUnit: "g",
  },
  {
    id: "2",
    name: "현미밥",
    calories: 150,
    protein: 3.5,
    carbs: 32,
    fat: 1.2,
    servingSize: 100,
    servingUnit: "g",
  },
  {
    id: "3",
    name: "브로콜리",
    calories: 34,
    protein: 2.8,
    carbs: 7,
    fat: 0.4,
    servingSize: 100,
    servingUnit: "g",
  },
  {
    id: "4",
    name: "계란",
    calories: 155,
    protein: 13,
    carbs: 1.1,
    fat: 11,
    servingSize: 1,
    servingUnit: "개",
  },
  {
    id: "5",
    name: "고구마",
    calories: 86,
    protein: 1.6,
    carbs: 20,
    fat: 0.1,
    servingSize: 100,
    servingUnit: "g",
  },
];

export function MealRegistrationModal({
  isOpen,
  onClose,
  mealType,
  date,
  onSave,
  onDelete,
  existingMeal,
  customTitle,
  customDescription,
}: MealRegistrationModalProps) {
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedFoods, setSelectedFoods] = useState<SelectedFood[]>([]);
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);

  // 기존 식단 데이터를 selectedFoods로 로드
  useEffect(() => {
    if (existingMeal && isOpen) {
      const loadedFoods: SelectedFood[] = existingMeal.foods.map((food) => {
        // mockFoodDatabase에서 음식 정보 찾기
        const foodItem = mockFoodDatabase.find((f) => f.name === food.name);
        if (foodItem) {
          return {
            ...foodItem,
            amount: food.amount,
          };
        }
        // 데이터베이스에 없는 경우 기본값 사용
        return {
          id: food.id,
          name: food.name,
          calories: 0,
          protein: 0,
          carbs: 0,
          fat: 0,
          servingSize: food.amount,
          servingUnit: food.unit,
          amount: food.amount,
        };
      });
      setSelectedFoods(loadedFoods);
    }
  }, [existingMeal, isOpen]);

  const mealTypeLabel = {
    breakfast: "아침",
    lunch: "점심",
    dinner: "저녁",
    snack: "간식",
  }[mealType];

  const filteredFoods = mockFoodDatabase.filter((food) =>
    food.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const addFood = (food: FoodItem) => {
    const existing = selectedFoods.find((f) => f.id === food.id);
    if (existing) {
      setSelectedFoods(
        selectedFoods.map((f) =>
          f.id === food.id ? { ...f, amount: f.amount + food.servingSize } : f
        )
      );
    } else {
      setSelectedFoods([...selectedFoods, { ...food, amount: food.servingSize }]);
    }
  };

  const removeFood = (id: string) => {
    setSelectedFoods(selectedFoods.filter((f) => f.id !== id));
  };

  const updateAmount = (id: string, delta: number) => {
    setSelectedFoods(
      selectedFoods.map((f) => {
        if (f.id === id) {
          const newAmount = Math.max(f.servingSize, f.amount + delta);
          return { ...f, amount: newAmount };
        }
        return f;
      })
    );
  };

  const calculateTotal = () => {
    return selectedFoods.reduce(
      (acc, food) => {
        const multiplier = food.amount / food.servingSize;
        return {
          calories: acc.calories + food.calories * multiplier,
          protein: acc.protein + food.protein * multiplier,
          carbs: acc.carbs + food.carbs * multiplier,
          fat: acc.fat + food.fat * multiplier,
        };
      },
      { calories: 0, protein: 0, carbs: 0, fat: 0 }
    );
  };

  const total = calculateTotal();

  const handleSave = () => {
    const total = calculateTotal();
    // 점수 계산: 기본 10점 + 칼로리/100 (최소 10점)
    const calculatedPoints = Math.max(10, Math.round(10 + total.calories / 100));
    
    const mealData = {
      foods: selectedFoods.map((food) => ({
        id: food.id,
        name: food.name,
        amount: food.amount,
        unit: food.servingUnit,
      })),
      totalCalories: Math.round(total.calories),
      protein: parseFloat(total.protein.toFixed(1)),
      carbs: parseFloat(total.carbs.toFixed(1)),
      fat: parseFloat(total.fat.toFixed(1)),
      points: calculatedPoints,
    };
    
    onSave(mealData);
    setSelectedFoods([]);
    setSearchQuery("");
    onClose();
  };

  const handleDelete = () => {
    onDelete();
    setShowDeleteDialog(false);
    setSelectedFoods([]);
    setSearchQuery("");
    onClose();
  };

  return (
    <>
      <Dialog
        open={isOpen}
        onOpenChange={(open) => {
          if (!open) {
            setSelectedFoods([]);
            setSearchQuery("");
          }
          onClose();
        }}
      >
        <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>
              {customTitle || `${mealTypeLabel} 식단 ${existingMeal ? "수정" : "등록"}`}
            </DialogTitle>
            <DialogDescription>
              {customDescription || date.toLocaleDateString("ko-KR", {
                month: "long",
                day: "numeric",
                weekday: "long",
              })}
            </DialogDescription>
          </DialogHeader>

        <div className="space-y-6">
          {/* 음식 검색 */}
          <div className="space-y-2">
            <Label>음식 검색</Label>
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="음식 이름을 검색하세요"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>

          {/* 검색 결과 */}
          {searchQuery && (
            <div className="space-y-2">
              <Label>검색 결과</Label>
              <div className="max-h-48 overflow-y-auto space-y-2 border rounded-lg p-2">
                {filteredFoods.length > 0 ? (
                  filteredFoods.map((food) => (
                    <div
                      key={food.id}
                      className="flex items-center justify-between p-2 hover:bg-muted rounded-lg cursor-pointer"
                      onClick={() => addFood(food)}
                    >
                      <div>
                        <div className="text-sm">{food.name}</div>
                        <div className="text-xs text-muted-foreground">
                          {food.servingSize}
                          {food.servingUnit} · {food.calories}kcal
                        </div>
                      </div>
                      <Button size="sm" variant="ghost">
                        <Plus className="w-4 h-4" />
                      </Button>
                    </div>
                  ))
                ) : (
                  <div className="text-center text-sm text-muted-foreground py-4">
                    검색 결과가 없습니다
                  </div>
                )}
              </div>
            </div>
          )}

          {/* 선택한 음식 목록 */}
          <div className="space-y-2">
            <Label>선택한 음식</Label>
            {selectedFoods.length > 0 ? (
              <div className="space-y-3 border rounded-lg p-4">
                {selectedFoods.map((food) => {
                  const multiplier = food.amount / food.servingSize;
                  return (
                    <div
                      key={food.id}
                      className="flex items-center gap-3 p-3 bg-muted/50 rounded-lg"
                    >
                      <div className="flex-1">
                        <div className="text-sm mb-1">{food.name}</div>
                        <div className="flex items-center gap-2 text-xs text-muted-foreground">
                          <span>{Math.round(food.calories * multiplier)}kcal</span>
                          <span>·</span>
                          <span>단백질 {(food.protein * multiplier).toFixed(1)}g</span>
                          <span>·</span>
                          <span>탄수화물 {(food.carbs * multiplier).toFixed(1)}g</span>
                          <span>·</span>
                          <span>지방 {(food.fat * multiplier).toFixed(1)}g</span>
                        </div>
                      </div>
                      <div className="flex items-center gap-2">
                        <Button
                          size="sm"
                          variant="outline"
                          onClick={() => updateAmount(food.id, -food.servingSize)}
                        >
                          <Minus className="w-3 h-3" />
                        </Button>
                        <div className="text-sm min-w-[60px] text-center">
                          {food.amount}
                          {food.servingUnit}
                        </div>
                        <Button
                          size="sm"
                          variant="outline"
                          onClick={() => updateAmount(food.id, food.servingSize)}
                        >
                          <Plus className="w-3 h-3" />
                        </Button>
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={() => removeFood(food.id)}
                        >
                          <X className="w-4 h-4" />
                        </Button>
                      </div>
                    </div>
                  );
                })}
              </div>
            ) : (
              <div className="text-center text-sm text-muted-foreground py-8 border rounded-lg">
                음식을 검색하여 추가해주세요
              </div>
            )}
          </div>

          {/* 영양소 합계 */}
          {selectedFoods.length > 0 && (
            <div className="space-y-2">
              <Label>영양소 합계</Label>
              <div className="grid grid-cols-4 gap-3">
                <div className="bg-primary/10 rounded-lg p-3 text-center">
                  <div className="text-xs text-muted-foreground mb-1">칼로리</div>
                  <div className="text-lg text-primary">{Math.round(total.calories)}</div>
                  <div className="text-xs text-muted-foreground">kcal</div>
                </div>
                <div className="bg-secondary/10 rounded-lg p-3 text-center">
                  <div className="text-xs text-muted-foreground mb-1">단백질</div>
                  <div className="text-lg text-secondary">{total.protein.toFixed(1)}</div>
                  <div className="text-xs text-muted-foreground">g</div>
                </div>
                <div className="bg-accent/10 rounded-lg p-3 text-center">
                  <div className="text-xs text-muted-foreground mb-1">탄수화물</div>
                  <div className="text-lg text-accent">{total.carbs.toFixed(1)}</div>
                  <div className="text-xs text-muted-foreground">g</div>
                </div>
                <div className="bg-muted rounded-lg p-3 text-center">
                  <div className="text-xs text-muted-foreground mb-1">지방</div>
                  <div className="text-lg">{total.fat.toFixed(1)}</div>
                  <div className="text-xs text-muted-foreground">g</div>
                </div>
              </div>
            </div>
          )}

          {/* 버튼 */}
          <div className="flex gap-2 pt-4">
            {existingMeal && (
              <Button
                variant="destructive"
                onClick={() => setShowDeleteDialog(true)}
              >
                <Trash2 className="w-4 h-4 mr-2" />
                삭제
              </Button>
            )}
            <Button variant="outline" onClick={onClose} className="flex-1">
              취소
            </Button>
            <Button
              onClick={handleSave}
              disabled={selectedFoods.length === 0}
              className="flex-1"
            >
              {existingMeal ? "수정하기" : "저장하기"}
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>

    <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>식단 삭제</AlertDialogTitle>
          <AlertDialogDescription>
            정말로 이 식단을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>취소</AlertDialogCancel>
          <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">
            삭제
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </>
  );
}