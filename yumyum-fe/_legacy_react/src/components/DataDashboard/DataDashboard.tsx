import { BadgeSection } from "./BadgeSection";
import { WeightChart } from "./WeightChart";
import { NutritionDonutChart } from "./NutritionDonutChart";
import { NutritionRadarChart } from "./NutritionRadarChart";
import { BarChart3 } from "lucide-react";

export function DataDashboard() {
  return (
    <div className="max-w-7xl mx-auto px-4 py-6 space-y-6">
      {/* 헤더 */}
      <div className="flex items-center gap-3">
        <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
          <BarChart3 className="w-6 h-6 text-primary" />
        </div>
        <div>
          <h1>데이터 대시보드</h1>
          <p className="text-sm text-muted-foreground">
            건강 데이터를 한눈에 확인하고 분석하세요
          </p>
        </div>
      </div>

      {/* 뱃지 섹션 */}
      <BadgeSection />

      {/* 차트 그리드 */}
      <div className="grid lg:grid-cols-2 gap-6">
        {/* 몸무게 변화 그래프 */}
        <div className="lg:col-span-2">
          <WeightChart />
        </div>

        {/* 영양소 섭취 비율 도넛 차트 */}
        <NutritionDonutChart />

        {/* 권장량 대비 섭취량 레이더 차트 */}
        <NutritionRadarChart />
      </div>

      {/* 추가 통계 카드들 */}
      <div className="grid md:grid-cols-4 gap-4">
        <div className="bg-card border border-border rounded-lg p-4">
          <div className="text-xs text-muted-foreground mb-2">주간 평균 칼로리</div>
          <div className="text-2xl mb-1">1,850</div>
          <div className="text-xs text-primary">목표 대비 92%</div>
        </div>
        <div className="bg-card border border-border rounded-lg p-4">
          <div className="text-xs text-muted-foreground mb-2">주간 운동 시간</div>
          <div className="text-2xl mb-1">320분</div>
          <div className="text-xs text-secondary">전주 대비 +15%</div>
        </div>
        <div className="bg-card border border-border rounded-lg p-4">
          <div className="text-xs text-muted-foreground mb-2">연속 기록 일수</div>
          <div className="text-2xl mb-1">7일</div>
          <div className="text-xs text-accent">최고 기록 갱신!</div>
        </div>
        <div className="bg-card border border-border rounded-lg p-4">
          <div className="text-xs text-muted-foreground mb-2">평균 수분 섭취</div>
          <div className="text-2xl mb-1">2.1L</div>
          <div className="text-xs text-primary">목표 달성</div>
        </div>
      </div>
    </div>
  );
}
