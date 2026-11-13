import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Sparkles, Users, ChevronRight } from "lucide-react";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "./ui/carousel";
import { RecommendedMealDetailModal } from "./RecommendedMealDetailModal";

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

const mockRecommendations: MealRecommendation[] = [
  {
    id: "1",
    type: "ai",
    title: "균형잡힌 다이어트 식단",
    calories: 450,
    protein: 35,
    carbs: 40,
    fat: 15,
    description: "당신의 목표에 최적화된 AI 맞춤 식단입니다.",
    tags: ["저칼로리", "고단백"],
    foods: [
      { name: "닭가슴살", amount: 150, unit: "g" },
      { name: "현미밥", amount: 80, unit: "g" },
      { name: "브로콜리", amount: 100, unit: "g" },
      { name: "방울토마토", amount: 50, unit: "g" },
      { name: "올리브유", amount: 1, unit: "큰술" },
    ],
  },
  {
    id: "2",
    type: "ai",
    title: "에너지 충전 식단",
    calories: 620,
    protein: 28,
    carbs: 75,
    fat: 18,
    description: "운동 전 에너지 보충에 좋은 식단입니다.",
    tags: ["운동전", "에너지"],
    foods: [
      { name: "고구마", amount: 200, unit: "g" },
      { name: "계란", amount: 2, unit: "개" },
      { name: "바나나", amount: 1, unit: "개" },
      { name: "아몬드", amount: 20, unit: "g" },
      { name: "그릭요거트", amount: 150, unit: "g" },
    ],
  },
  {
    id: "3",
    type: "group",
    title: "김트레이너의 벌크업 식단",
    calories: 780,
    protein: 45,
    carbs: 85,
    fat: 22,
    description: "근육 성장을 위한 그룹장 추천 식단입니다.",
    tags: ["벌크업", "근성장"],
    trainerName: "김트레이너",
    foods: [
      { name: "흰쌀밥", amount: 150, unit: "g" },
      { name: "소고기", amount: 150, unit: "g" },
      { name: "계란", amount: 3, unit: "개" },
      { name: "시금치", amount: 100, unit: "g" },
      { name: "아보카도", amount: 50, unit: "g" },
      { name: "견과류", amount: 30, unit: "g" },
    ],
  },
  {
    id: "4",
    type: "group",
    title: "이영양사의 클린 이팅",
    calories: 380,
    protein: 30,
    carbs: 35,
    fat: 12,
    description: "깨끗한 식단으로 건강을 챙기세요.",
    tags: ["클린", "건강"],
    trainerName: "이영양사",
    foods: [
      { name: "연어", amount: 120, unit: "g" },
      { name: "퀴노아", amount: 80, unit: "g" },
      { name: "샐러드", amount: 150, unit: "g" },
      { name: "레몬드레싱", amount: 1, unit: "큰술" },
    ],
  },
];

export function RecommendedMealsCarousel() {
  const [selectedMeal, setSelectedMeal] = useState<MealRecommendation | null>(
    null
  );
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  const handleViewDetail = (meal: MealRecommendation) => {
    setSelectedMeal(meal);
    setIsDetailModalOpen(true);
  };

  return (
    <>
      <Carousel
        opts={{
          align: "start",
          loop: true,
        }}
        className="w-full"
      >
        <CarouselContent>
          {mockRecommendations.map((meal) => (
            <CarouselItem key={meal.id} className="md:basis-1/2 lg:basis-1/2">
              <Card className="p-6 h-full flex flex-col">
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center gap-2">
                    {meal.type === "ai" ? (
                      <div className="w-10 h-10 bg-primary/10 rounded-lg flex items-center justify-center">
                        <Sparkles className="w-5 h-5 text-primary" />
                      </div>
                    ) : (
                      <div className="w-10 h-10 bg-secondary/10 rounded-lg flex items-center justify-center">
                        <Users className="w-5 h-5 text-secondary" />
                      </div>
                    )}
                    <div>
                      <Badge
                        variant={meal.type === "ai" ? "default" : "secondary"}
                        className="mb-1"
                      >
                        {meal.type === "ai" ? "AI 추천" : "그룹장 추천"}
                      </Badge>
                      <h4>{meal.title}</h4>
                    </div>
                  </div>
                </div>

                <p className="text-sm text-muted-foreground mb-4">
                  {meal.description}
                </p>

                {/* 칼로리 요약 */}
                <div className="mb-4">
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-xs text-muted-foreground">
                      총 칼로리
                    </span>
                    <span className="text-sm">
                      {meal.calories} kcal
                    </span>
                  </div>
                  <div className="flex gap-2 text-xs">
                    <div className="flex-1 bg-blue-100 rounded px-2 py-1 text-center">
                      탄 {meal.carbs}g
                    </div>
                    <div className="flex-1 bg-green-100 rounded px-2 py-1 text-center">
                      단 {meal.protein}g
                    </div>
                    <div className="flex-1 bg-orange-100 rounded px-2 py-1 text-center">
                      지 {meal.fat}g
                    </div>
                  </div>
                </div>

                {/* 식단 구성 리스트 */}
                <div className="mb-4 flex-1">
                  <h5 className="text-xs text-muted-foreground mb-2">
                    식단 구성
                  </h5>
                  <div className="space-y-1.5">
                    {meal.foods.slice(0, 4).map((food, index) => (
                      <div
                        key={index}
                        className="flex items-center justify-between text-sm bg-muted/30 rounded px-3 py-1.5"
                      >
                        <div className="flex items-center gap-2">
                          <div className="w-1.5 h-1.5 rounded-full bg-primary"></div>
                          <span>{food.name}</span>
                        </div>
                        <span className="text-muted-foreground text-xs">
                          {food.amount}
                          {food.unit}
                        </span>
                      </div>
                    ))}
                    {meal.foods.length > 4 && (
                      <div className="text-xs text-muted-foreground text-center py-1">
                        외 {meal.foods.length - 4}개 항목
                      </div>
                    )}
                  </div>
                </div>

                {/* 태그 */}
                <div className="flex flex-wrap gap-2 mb-4">
                  {meal.tags.map((tag) => (
                    <Badge key={tag} variant="outline" className="text-xs">
                      {tag}
                    </Badge>
                  ))}
                </div>

                {/* 자세히 보기 버튼 */}
                <Button
                  variant="outline"
                  className="w-full"
                  onClick={() => handleViewDetail(meal)}
                >
                  자세히 보기
                  <ChevronRight className="w-4 h-4 ml-1" />
                </Button>
              </Card>
            </CarouselItem>
          ))}
        </CarouselContent>
        <CarouselPrevious className="left-4" />
        <CarouselNext className="right-4" />
      </Carousel>

      {/* 추천 식단 상세 모달 */}
      <RecommendedMealDetailModal
        isOpen={isDetailModalOpen}
        onClose={() => setIsDetailModalOpen(false)}
        meal={selectedMeal}
      />
    </>
  );
}
