import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import {
  Trophy,
  TrendingUp,
  TrendingDown,
  Minus,
  Bell,
  Calendar,
  Flame,
  Dumbbell,
  UserPlus,
} from "lucide-react";
import { Member } from "./GroupLeaderPage";

interface GroupDashboardProps {
  members: Member[];
  onSelectMember: (member: Member) => void;
  onInviteMember?: () => void;
}

export function GroupDashboard({
  members,
  onSelectMember,
  onInviteMember,
}: GroupDashboardProps) {
  // 획득 점수 기준으로 랭킹 정렬
  const rankedMembers = [...members].sort(
    (a, b) => b.totalPoints - a.totalPoints
  );

  const getGoalLabel = (goal: string) => {
    switch (goal) {
      case "diet":
        return "다이어트";
      case "bulk":
        return "벌크업";
      case "maintain":
        return "유지";
      default:
        return goal;
    }
  };

  const getGoalIcon = (member: Member) => {
    const weightChange = member.currentWeight - member.startWeight;
    if (weightChange < 0) {
      return <TrendingDown className="w-4 h-4 text-secondary" />;
    } else if (weightChange > 0) {
      return <TrendingUp className="w-4 h-4 text-accent" />;
    } else {
      return <Minus className="w-4 h-4 text-muted-foreground" />;
    }
  };

  const getRankBadge = (index: number) => {
    switch (index) {
      case 0:
        return (
          <div className="w-10 h-10 rounded-full bg-gradient-to-br from-yellow-400 to-yellow-600 flex items-center justify-center shadow-lg">
            <Trophy className="w-5 h-5 text-white" />
          </div>
        );
      case 1:
        return (
          <div className="w-10 h-10 rounded-full bg-gradient-to-br from-gray-300 to-gray-400 flex items-center justify-center shadow-lg">
            <Trophy className="w-5 h-5 text-white" />
          </div>
        );
      case 2:
        return (
          <div className="w-10 h-10 rounded-full bg-gradient-to-br from-orange-400 to-orange-600 flex items-center justify-center shadow-lg">
            <Trophy className="w-5 h-5 text-white" />
          </div>
        );
      default:
        return (
          <div className="w-10 h-10 rounded-full bg-muted flex items-center justify-center">
            <span className="text-sm text-muted-foreground">{index + 1}</span>
          </div>
        );
    }
  };

  return (
    <div className="space-y-6">
      {/* 그룹원 랭킹 */}
      <Card className="p-6">
        <div className="flex items-center justify-between mb-4">
          <h3>그룹원 랭킹 🏆</h3>
          <Badge variant="outline">획득 점수 기준</Badge>
        </div>

        <div className="space-y-3">
          {rankedMembers.slice(0, 3).map((member, index) => (
            <Card
              key={member.id}
              className={`p-4 cursor-pointer transition-all hover:shadow-md ${
                index === 0 ? "bg-primary/5 border-primary/20" : "bg-muted/20"
              }`}
              onClick={() => onSelectMember(member)}
            >
              <div className="flex items-center gap-4">
                {getRankBadge(index)}

                <Avatar className="w-12 h-12">
                  <AvatarImage src={member.avatar} alt={member.name} />
                  <AvatarFallback>{member.name[0]}</AvatarFallback>
                </Avatar>

                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="font-medium">{member.name}</span>
                    {member.hasNewActivity && (
                      <Badge variant="default" className="text-xs">
                        <Bell className="w-3 h-3 mr-1" />새 활동
                      </Badge>
                    )}
                  </div>
                  <div className="flex items-center gap-3 text-xs text-muted-foreground">
                    <div className="flex items-center gap-1">
                      <Flame className="w-3 h-3" />
                      <span>{member.consecutiveDays}일 연속</span>
                    </div>
                    <div className="flex items-center gap-1">
                      {getGoalIcon(member)}
                      <span>
                        {member.currentWeight}kg (
                        {member.currentWeight - member.startWeight > 0
                          ? "+"
                          : ""}
                        {(member.currentWeight - member.startWeight).toFixed(1)}
                        kg)
                      </span>
                    </div>
                  </div>
                </div>

                <div className="text-right">
                  <div className="text-xs text-muted-foreground mb-1">
                    획득 점수
                  </div>
                  <div className="text-sm text-secondary">
                    {member.totalPoints}P
                  </div>
                </div>
              </div>
            </Card>
          ))}
        </div>
      </Card>

      {/* 그룹원 목록 */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-2">
            <h3>전체 그룹원</h3>
            <Badge variant="outline">{members.length}명</Badge>
          </div>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
          {members.map((member) => (
            <Card
              key={member.id}
              className="p-4 cursor-pointer transition-all hover:shadow-md hover:border-primary/50"
              onClick={() => onSelectMember(member)}
            >
              <div className="flex items-start gap-3 mb-4">
                <Avatar className="w-14 h-14">
                  <AvatarImage src={member.avatar} alt={member.name} />
                  <AvatarFallback>{member.name[0]}</AvatarFallback>
                </Avatar>

                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="font-medium">{member.name}</span>
                    {member.hasNewActivity && (
                      <div className="w-2 h-2 rounded-full bg-primary animate-pulse" />
                    )}
                  </div>
                  <Badge variant="secondary" className="text-xs">
                    {getGoalLabel(member.goal)}
                  </Badge>
                </div>
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between text-xs">
                  <span className="text-muted-foreground">현재 체중</span>
                  <div className="flex items-center gap-1">
                    {getGoalIcon(member)}
                    <span className="font-medium">{member.currentWeight}kg</span>
                  </div>
                </div>

                <div className="flex items-center justify-between text-xs">
                  <span className="text-muted-foreground">목표 체중</span>
                  <span className="font-medium">{member.targetWeight}kg</span>
                </div>

                <div className="h-px bg-border my-2" />

                <div className="grid grid-cols-2 gap-2 text-xs">
                  <div className="flex items-center gap-1 text-muted-foreground">
                    <Flame className="w-3 h-3 text-primary" />
                    <span>{member.consecutiveDays}일 연속</span>
                  </div>
                  <div className="flex items-center gap-1 text-muted-foreground">
                    <Dumbbell className="w-3 h-3 text-secondary" />
                    <span>{member.exerciseCount}회</span>
                  </div>
                </div>

                <div className="flex items-center gap-1 text-xs text-muted-foreground">
                  <Calendar className="w-3 h-3" />
                  <span>마지막 활동: {member.lastActivityDate}</span>
                </div>
              </div>

              {member.hasNewActivity && (
                <div className="mt-3 pt-3 border-t border-border">
                  <Badge variant="default" className="w-full justify-center">
                    <Bell className="w-3 h-3 mr-1" />새 기록 확인하기
                  </Badge>
                </div>
              )}
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}