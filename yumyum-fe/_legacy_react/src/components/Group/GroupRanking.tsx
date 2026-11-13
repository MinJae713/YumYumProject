import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Trophy, Flame, TrendingUp, TrendingDown, Minus } from "lucide-react";
import { GroupMember } from "./GroupMemberPage";

interface GroupRankingProps {
  members: GroupMember[];
}

export function GroupRanking({ members }: GroupRankingProps) {
  // 획득 점수 기준으로 랭킹 정렬
  const rankedMembers = [...members].sort(
    (a, b) => b.totalPoints - a.totalPoints
  );

  const getRankBadge = (index: number) => {
    switch (index) {
      case 0:
        return (
          <div className="w-8 h-8 rounded-full bg-gradient-to-br from-yellow-400 to-yellow-600 flex items-center justify-center shadow-lg">
            <Trophy className="w-4 h-4 text-white" />
          </div>
        );
      case 1:
        return (
          <div className="w-8 h-8 rounded-full bg-gradient-to-br from-gray-300 to-gray-400 flex items-center justify-center shadow-lg">
            <Trophy className="w-4 h-4 text-white" />
          </div>
        );
      case 2:
        return (
          <div className="w-8 h-8 rounded-full bg-gradient-to-br from-orange-400 to-orange-600 flex items-center justify-center shadow-lg">
            <Trophy className="w-4 h-4 text-white" />
          </div>
        );
      default:
        return (
          <div className="w-8 h-8 rounded-full bg-muted flex items-center justify-center">
            <span className="text-xs text-muted-foreground">{index + 1}</span>
          </div>
        );
    }
  };

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

  return (
    <Card className="p-6">
      <div className="flex items-center justify-between mb-4">
        <h3>그룹 랭킹 🏆</h3>
        <Badge variant="outline">획득 점수 기준</Badge>
      </div>

      <div className="space-y-2">
        {rankedMembers.map((member, index) => (
          <Card
            key={member.id}
            className={`p-4 transition-all ${
              member.isMe
                ? "bg-primary/10 border-primary shadow-md"
                : index < 3
                ? "bg-muted/20"
                : "bg-background"
            }`}
          >
            <div className="flex items-center gap-3">
              {getRankBadge(index)}

              <Avatar className="w-10 h-10">
                <AvatarImage src={member.avatar} alt={member.name} />
                <AvatarFallback>{member.name[0]}</AvatarFallback>
              </Avatar>

              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2 mb-1">
                  <span className="text-sm font-medium">{member.name}</span>
                  {member.isMe && (
                    <Badge variant="default" className="text-xs">
                      나
                    </Badge>
                  )}
                </div>
                <div className="flex items-center gap-2 text-xs text-muted-foreground">
                  <Badge variant="secondary" className="text-xs">
                    {getGoalLabel(member.goal)}
                  </Badge>
                  <span>{member.currentWeight}kg</span>
                </div>
              </div>

              <div className="flex items-center gap-4 text-sm">
                <div className="flex items-center gap-1 text-primary">
                  <Flame className="w-4 h-4" />
                  <span className="font-medium">{member.consecutiveDays}일</span>
                </div>
                <div className="text-secondary font-medium">
                  {member.totalPoints}P
                </div>
              </div>
            </div>
          </Card>
        ))}
      </div>

      {/* 나의 순위 요약 */}
      {(() => {
        const myRank =
          rankedMembers.findIndex((member) => member.isMe) + 1;
        const myData = rankedMembers.find((member) => member.isMe);
        if (myData) {
          return (
            <Card className="mt-4 p-4 bg-gradient-to-r from-primary/10 to-secondary/10">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-primary flex items-center justify-center">
                    <span className="text-white font-medium">
                      {myRank}위
                    </span>
                  </div>
                  <div>
                    <div className="text-sm font-medium">나의 순위</div>
                    <div className="text-xs text-muted-foreground">
                      총 {myData.totalPoints}P 획득
                    </div>
                  </div>
                </div>
                <div className="text-right">
                  <div className="text-xs text-muted-foreground mb-1">
                    다음 순위까지
                  </div>
                  <div className="text-sm font-medium text-primary">
                    {myRank > 1
                      ? `${
                          rankedMembers[myRank - 2].totalPoints -
                          myData.totalPoints
                        }P 남음`
                      : "1위 달성! 🎉"}
                  </div>
                </div>
              </div>
            </Card>
          );
        }
        return null;
      })()}
    </Card>
  );
}