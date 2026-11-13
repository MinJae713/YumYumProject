import { useState } from "react";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import {
  Trophy,
  Flame,
  Target,
  Star,
  Award,
  Calendar,
  Dumbbell,
  Utensils,
  TrendingDown,
  Users,
  Lock,
  Zap,
  Heart,
  Medal,
} from "lucide-react";

interface BadgeItem {
  id: string;
  name: string;
  description: string;
  icon: React.ReactNode;
  category: "achievement" | "streak" | "social";
  achieved: boolean;
  achievedDate?: string;
  condition: string;
  color?: string;
}

export function BadgesPage() {
  const badges: BadgeItem[] = [
    {
      id: "1",
      name: "첫 걸음",
      description: "첫 식단 기록 완료",
      icon: <Star className="w-8 h-8" />,
      category: "achievement",
      achieved: true,
      achievedDate: "2025-10-15",
      condition: "첫 식단 기록하기",
      color: "text-yellow-500",
    },
    {
      id: "2",
      name: "7일 연속",
      description: "7일 연속 기록 달성",
      icon: <Flame className="w-8 h-8" />,
      category: "streak",
      achieved: true,
      achievedDate: "2025-10-22",
      condition: "7일 연속 기록하기",
      color: "text-orange-500",
    },
    {
      id: "3",
      name: "목표 달성",
      description: "첫 목표 체중 달성",
      icon: <Target className="w-8 h-8" />,
      category: "achievement",
      achieved: true,
      achievedDate: "2025-11-01",
      condition: "목표 체중 달성하기",
      color: "text-green-500",
    },
    {
      id: "4",
      name: "운동 마니아",
      description: "한 달 동안 20회 운동 완료",
      icon: <Dumbbell className="w-8 h-8" />,
      category: "achievement",
      achieved: true,
      achievedDate: "2025-11-03",
      condition: "한 달 동안 20회 운동하기",
      color: "text-blue-500",
    },
    {
      id: "5",
      name: "그룹의 힘",
      description: "그룹 활동 참여",
      icon: <Users className="w-8 h-8" />,
      category: "social",
      achieved: true,
      achievedDate: "2025-10-20",
      condition: "그룹에 가입하기",
      color: "text-purple-500",
    },
    {
      id: "6",
      name: "30일 연속",
      description: "30일 연속 기록 달성",
      icon: <Medal className="w-8 h-8" />,
      category: "streak",
      achieved: false,
      condition: "30일 연속 기록하기",
      color: "text-orange-600",
    },
    {
      id: "7",
      name: "완벽한 균형",
      description: "영양소 균형 완벽 달성 7일",
      icon: <Utensils className="w-8 h-8" />,
      category: "achievement",
      achieved: false,
      condition: "7일 동안 영양소 균형 100% 달성하기",
      color: "text-green-600",
    },
    {
      id: "8",
      name: "다이어트 킹",
      description: "10kg 감량 달성",
      icon: <TrendingDown className="w-8 h-8" />,
      category: "achievement",
      achieved: false,
      condition: "10kg 감량하기",
      color: "text-pink-500",
    },
    {
      id: "9",
      name: "100일 연속",
      description: "100일 연속 기록 달성",
      icon: <Trophy className="w-8 h-8" />,
      category: "streak",
      achieved: false,
      condition: "100일 연속 기록하기",
      color: "text-yellow-600",
    },
    {
      id: "10",
      name: "열정 가득",
      description: "한 달 동안 매일 운동",
      icon: <Zap className="w-8 h-8" />,
      category: "achievement",
      achieved: false,
      condition: "30일 연속 운동하기",
      color: "text-yellow-500",
    },
    {
      id: "11",
      name: "그룹 리더",
      description: "그룹원 10명 피드백 제공",
      icon: <Award className="w-8 h-8" />,
      category: "social",
      achieved: false,
      condition: "그룹원 10명에게 피드백 제공하기",
      color: "text-indigo-500",
    },
    {
      id: "12",
      name: "헬스케어 마스터",
      description: "모든 기본 뱃지 획득",
      icon: <Heart className="w-8 h-8" />,
      category: "achievement",
      achieved: false,
      condition: "모든 기본 뱃지 획득하기",
      color: "text-red-500",
    },
  ];

  const [selectedCategory, setSelectedCategory] = useState<"all" | "achievement" | "streak" | "social">("all");

  const achievedBadges = badges.filter((badge) => badge.achieved);
  const lockedBadges = badges.filter((badge) => !badge.achieved);

  const filteredBadges = badges.filter(
    (badge) => selectedCategory === "all" || badge.category === selectedCategory
  );

  const getCategoryLabel = (category: string) => {
    switch (category) {
      case "achievement":
        return "업적";
      case "streak":
        return "연속 기록";
      case "social":
        return "소셜";
      default:
        return "전체";
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-6">
      {/* 헤더 */}
      <div className="flex items-center gap-3 mb-6">
        <div className="w-12 h-12 bg-gradient-to-br from-yellow-400 to-orange-500 rounded-lg flex items-center justify-center">
          <Trophy className="w-6 h-6 text-white" />
        </div>
        <div>
          <h1>뱃지 컬렉션</h1>
          <p className="text-sm text-muted-foreground">
            목표를 달성하고 뱃지를 모아보세요
          </p>
        </div>
      </div>

      {/* 통계 카드 */}
      <div className="grid md:grid-cols-3 gap-4 mb-6">
        <Card className="p-4 bg-gradient-to-br from-primary/10 to-primary/5">
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm text-muted-foreground mb-1">
                획득한 뱃지
              </div>
              <div className="text-2xl text-primary">
                {achievedBadges.length}
              </div>
            </div>
            <Trophy className="w-10 h-10 text-primary opacity-50" />
          </div>
        </Card>

        <Card className="p-4 bg-gradient-to-br from-secondary/10 to-secondary/5">
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm text-muted-foreground mb-1">
                미획득 뱃지
              </div>
              <div className="text-2xl text-secondary">
                {lockedBadges.length}
              </div>
            </div>
            <Lock className="w-10 h-10 text-secondary opacity-50" />
          </div>
        </Card>

        <Card className="p-4 bg-gradient-to-br from-accent/10 to-accent/5">
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm text-muted-foreground mb-1">
                달성률
              </div>
              <div className="text-2xl text-accent">
                {Math.round((achievedBadges.length / badges.length) * 100)}%
              </div>
            </div>
            <Star className="w-10 h-10 text-accent opacity-50" />
          </div>
        </Card>
      </div>

      {/* 탭 */}
      <Tabs defaultValue="all" className="w-full">
        <TabsList className="grid w-full grid-cols-4 mb-6">
          <TabsTrigger value="all" onClick={() => setSelectedCategory("all")}>
            전체
          </TabsTrigger>
          <TabsTrigger
            value="achievement"
            onClick={() => setSelectedCategory("achievement")}
          >
            업적
          </TabsTrigger>
          <TabsTrigger
            value="streak"
            onClick={() => setSelectedCategory("streak")}
          >
            연속 기록
          </TabsTrigger>
          <TabsTrigger
            value="social"
            onClick={() => setSelectedCategory("social")}
          >
            소셜
          </TabsTrigger>
        </TabsList>

        <TabsContent value={selectedCategory} className="space-y-6">
          {/* 획득한 뱃지 */}
          {filteredBadges.filter((b) => b.achieved).length > 0 && (
            <div>
              <h3 className="mb-4">획득한 뱃지</h3>
              <div className="grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {filteredBadges
                  .filter((badge) => badge.achieved)
                  .map((badge) => (
                    <Card
                      key={badge.id}
                      className="p-6 text-center hover:shadow-lg transition-all cursor-pointer bg-gradient-to-br from-background to-muted/20"
                    >
                      <div
                        className={`w-20 h-20 mx-auto mb-4 rounded-full bg-gradient-to-br from-white to-gray-100 flex items-center justify-center shadow-lg ${badge.color}`}
                      >
                        {badge.icon}
                      </div>
                      <h4 className="text-sm mb-2">{badge.name}</h4>
                      <p className="text-xs text-muted-foreground mb-3">
                        {badge.description}
                      </p>
                      <Badge variant="secondary" className="text-xs">
                        <Calendar className="w-3 h-3 mr-1" />
                        {badge.achievedDate}
                      </Badge>
                    </Card>
                  ))}
              </div>
            </div>
          )}

          {/* 미획득 뱃지 */}
          {filteredBadges.filter((b) => !b.achieved).length > 0 && (
            <div>
              <h3 className="mb-4">미획득 뱃지</h3>
              <div className="grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {filteredBadges
                  .filter((badge) => !badge.achieved)
                  .map((badge) => (
                    <Card
                      key={badge.id}
                      className="p-6 text-center hover:shadow-lg transition-all cursor-pointer bg-muted/20 opacity-75"
                    >
                      <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-gradient-to-br from-gray-200 to-gray-300 flex items-center justify-center shadow-lg relative">
                        <div className="text-gray-400">{badge.icon}</div>
                        <div className="absolute inset-0 flex items-center justify-center bg-black/20 rounded-full">
                          <Lock className="w-8 h-8 text-white" />
                        </div>
                      </div>
                      <h4 className="text-sm mb-2 text-muted-foreground">
                        {badge.name}
                      </h4>
                      <p className="text-xs text-muted-foreground mb-3">
                        {badge.description}
                      </p>
                      <Badge variant="outline" className="text-xs">
                        {badge.condition}
                      </Badge>
                    </Card>
                  ))}
              </div>
            </div>
          )}

          {filteredBadges.length === 0 && (
            <div className="text-center py-12">
              <Trophy className="w-16 h-16 mx-auto mb-4 text-muted-foreground opacity-50" />
              <h4 className="mb-2">뱃지가 없습니다</h4>
              <p className="text-sm text-muted-foreground">
                선택한 카테고리에 뱃지가 없습니다
              </p>
            </div>
          )}
        </TabsContent>
      </Tabs>
    </div>
  );
}
