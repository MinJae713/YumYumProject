import { useState } from "react";
import { Card } from "./ui/card";
import { Progress } from "./ui/progress";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { RecommendedMealsCarousel } from "./RecommendedMealsCarousel";
import { TodayMeals } from "./TodayMeals";
import { TodayExercise } from "./TodayExercise";
import { DailyResearchModal } from "./DailyResearchModal";
import { Calendar, TrendingUp, Award, ClipboardCheck } from "lucide-react";

export function MainDashboard() {
  // 오늘 날짜
  const today = new Date();
  const dateString = today.toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
    weekday: "long",
  });

  // 목표 데이터 (실제로는 사용자 데이터에서 가져옴)
  const dailyGoals = {
    calories: { current: 900, target: 2000 },
    protein: { current: 65, target: 120 },
    water: { current: 1.5, target: 2.5 },
  };

  // 데일리 리서치 상태
  const [isResearchComplete, setIsResearchComplete] = useState(false);
  const [isResearchModalOpen, setIsResearchModalOpen] = useState(false);

  const handleResearchComplete = () => {
    setIsResearchComplete(true);
    setIsResearchModalOpen(false);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-6 space-y-6">
      {/* 헤더 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="mb-1">오늘의 요약</h1>
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <Calendar className="w-4 h-4" />
            <span>{dateString}</span>
          </div>
        </div>
        <Badge variant="default" className="text-sm py-2 px-4">
          <Award className="w-4 h-4 mr-2" />
          7일 연속 기록 중!
        </Badge>
      </div>

      {/* 일일 목표 현황 */}
      <Card className="p-6">
        <div className="flex items-center gap-2 mb-4">
          <TrendingUp className="w-5 h-5 text-primary" />
          <h3>오늘의 목표 달성률</h3>
        </div>
        <div className="grid md:grid-cols-3 gap-6">
          {/* 칼로리 */}
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span>칼로리</span>
              <span className="text-muted-foreground">
                {dailyGoals.calories.current} / {dailyGoals.calories.target} kcal
              </span>
            </div>
            <Progress
              value={(dailyGoals.calories.current / dailyGoals.calories.target) * 100}
              className="h-2"
            />
            <div className="text-xs text-muted-foreground">
              {Math.round((dailyGoals.calories.current / dailyGoals.calories.target) * 100)}% 달성
            </div>
          </div>

          {/* 단백질 */}
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span>단백질</span>
              <span className="text-muted-foreground">
                {dailyGoals.protein.current} / {dailyGoals.protein.target} g
              </span>
            </div>
            <Progress
              value={(dailyGoals.protein.current / dailyGoals.protein.target) * 100}
              className="h-2"
            />
            <div className="text-xs text-muted-foreground">
              {Math.round((dailyGoals.protein.current / dailyGoals.protein.target) * 100)}% 달성
            </div>
          </div>

          {/* 수분 섭취 */}
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span>수분 섭취</span>
              <span className="text-muted-foreground">
                {dailyGoals.water.current} / {dailyGoals.water.target} L
              </span>
            </div>
            <Progress
              value={(dailyGoals.water.current / dailyGoals.water.target) * 100}
              className="h-2"
            />
            <div className="text-xs text-muted-foreground">
              {Math.round((dailyGoals.water.current / dailyGoals.water.target) * 100)}% 달성
            </div>
          </div>
        </div>
      </Card>

      {/* 추천 식단 캐러셀 */}
      <div>
        <div className="mb-4">
          <h2>추천 식단</h2>
          <p className="text-sm text-muted-foreground">
            AI와 전문가가 추천하는 맞춤형 식단을 확인해보세요
          </p>
        </div>
        <RecommendedMealsCarousel />
      </div>

      {/* 오늘의 식단 */}
      <div>
        <div className="mb-4">
          <h2>오늘의 식단</h2>
          <p className="text-sm text-muted-foreground">
            시간대별로 식단을 관리하세요
          </p>
        </div>
        <TodayMeals />
      </div>

      {/* 오늘의 운동 */}
      <div>
        <div className="mb-4">
          <h2>운동 기록</h2>
          <p className="text-sm text-muted-foreground">
            오늘의 운동 활동을 기록하고 관리하세요
          </p>
        </div>
        <TodayExercise />
      </div>

      {/* 플로팅 액션 버튼 (FAB) - 데일리 리서치 */}
      <Button
        size="lg"
        onClick={() => !isResearchComplete && setIsResearchModalOpen(true)}
        disabled={isResearchComplete}
        className={`fixed bottom-6 right-6 w-16 h-16 rounded-full shadow-2xl transition-all z-50 ${
          isResearchComplete
            ? "bg-muted text-muted-foreground cursor-not-allowed opacity-60"
            : "bg-gradient-to-br from-primary to-secondary hover:shadow-xl hover:scale-110 animate-pulse"
        }`}
      >
        <ClipboardCheck className="w-7 h-7" />
      </Button>

      {/* 데일리 리서치 모달 */}
      <DailyResearchModal
        open={isResearchModalOpen}
        onOpenChange={setIsResearchModalOpen}
        onComplete={handleResearchComplete}
      />
    </div>
  );
}
