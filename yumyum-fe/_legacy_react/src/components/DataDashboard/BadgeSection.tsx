import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { ChevronDown, ChevronUp, Award, Trophy, Flame, Target, Zap, Star } from "lucide-react";

interface BadgeItem {
  id: string;
  name: string;
  description: string;
  icon: any;
  color: string;
  earnedDate: string;
  isRecent: boolean;
}

const mockBadges: BadgeItem[] = [
  {
    id: "1",
    name: "7일 연속 기록",
    description: "7일 동안 식단을 꾸준히 기록했습니다",
    icon: Flame,
    color: "text-accent",
    earnedDate: "2025.11.05",
    isRecent: true,
  },
  {
    id: "2",
    name: "목표 달성",
    description: "일일 목표를 달성했습니다",
    icon: Target,
    color: "text-primary",
    earnedDate: "2025.11.04",
    isRecent: true,
  },
  {
    id: "3",
    name: "영양 균형",
    description: "완벽한 영양 균형을 달성했습니다",
    icon: Star,
    color: "text-[#eab308]",
    earnedDate: "2025.11.03",
    isRecent: true,
  },
  {
    id: "4",
    name: "운동 챔피언",
    description: "주간 운동 목표를 달성했습니다",
    icon: Trophy,
    color: "text-secondary",
    earnedDate: "2025.11.02",
    isRecent: true,
  },
  {
    id: "5",
    name: "빠른 시작",
    description: "첫 식단 기록을 완료했습니다",
    icon: Zap,
    color: "text-[#8b5cf6]",
    earnedDate: "2025.11.01",
    isRecent: true,
  },
  {
    id: "6",
    name: "30일 챌린지",
    description: "30일 연속 기록 달성",
    icon: Award,
    color: "text-accent",
    earnedDate: "2025.10.25",
    isRecent: false,
  },
  {
    id: "7",
    name: "체중 감량 마스터",
    description: "목표 체중 감량 달성",
    icon: Trophy,
    color: "text-primary",
    earnedDate: "2025.10.20",
    isRecent: false,
  },
  {
    id: "8",
    name: "단백질 킹",
    description: "일주일간 단백질 목표 달성",
    icon: Star,
    color: "text-secondary",
    earnedDate: "2025.10.15",
    isRecent: false,
  },
];

export function BadgeSection() {
  const [showAll, setShowAll] = useState(false);
  const recentBadges = mockBadges.filter((badge) => badge.isRecent);
  const displayBadges = showAll ? mockBadges : recentBadges;

  return (
    <Card className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-accent/10 rounded-lg flex items-center justify-center">
            <Award className="w-5 h-5 text-accent" />
          </div>
          <div>
            <h3>획득한 뱃지</h3>
            <p className="text-xs text-muted-foreground">
              총 {mockBadges.length}개 중 {recentBadges.length}개 최근 획득
            </p>
          </div>
        </div>
        <Button
          variant="outline"
          size="sm"
          onClick={() => setShowAll(!showAll)}
        >
          {showAll ? (
            <>
              접기 <ChevronUp className="w-4 h-4 ml-1" />
            </>
          ) : (
            <>
              전체보기 <ChevronDown className="w-4 h-4 ml-1" />
            </>
          )}
        </Button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        {displayBadges.map((badge) => {
          const Icon = badge.icon;
          return (
            <div
              key={badge.id}
              className="group relative bg-muted/30 rounded-lg p-4 hover:bg-muted/50 transition-all cursor-pointer border border-border hover:border-primary/50"
            >
              <div className="flex flex-col items-center text-center space-y-2">
                <div className={`w-16 h-16 rounded-full bg-card border-2 border-border flex items-center justify-center ${badge.color}`}>
                  <Icon className="w-8 h-8" />
                </div>
                <div>
                  <h4 className="text-sm mb-1">{badge.name}</h4>
                  <p className="text-xs text-muted-foreground line-clamp-2">
                    {badge.description}
                  </p>
                </div>
                <Badge variant="outline" className="text-xs">
                  {badge.earnedDate}
                </Badge>
              </div>
              
              {badge.isRecent && (
                <div className="absolute -top-2 -right-2">
                  <Badge className="bg-accent text-accent-foreground text-xs">NEW</Badge>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </Card>
  );
}
