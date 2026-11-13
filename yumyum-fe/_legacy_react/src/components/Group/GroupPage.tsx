import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Users, Crown } from "lucide-react";
import { GroupLeaderPage } from "./GroupLeaderPage";
import { GroupMemberPage } from "./GroupMemberPage";
import { GroupSelectionPage } from "./GroupSelectionPage";

export function GroupPage() {
  // 실제로는 사용자 정보에서 역할을 가져와야 하지만, 데모용으로 토글 가능하게 구현
  const [userRole, setUserRole] = useState<"leader" | "member">("member");
  const [selectedGroup, setSelectedGroup] = useState<{
    id: string;
    isLeader: boolean;
  } | null>(null);

  const handleSelectGroup = (groupId: string, isLeader: boolean) => {
    setSelectedGroup({ id: groupId, isLeader });
  };

  const handleBackToSelection = () => {
    setSelectedGroup(null);
  };

  return (
    <div>
      {/* 역할 전환 버튼 (데모용) */}
      <div className="max-w-7xl mx-auto px-4 py-4">
        <Card className="p-4 bg-gradient-to-r from-primary/10 to-secondary/10">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <Users className="w-5 h-5 text-primary" />
              <div>
                <div className="text-sm font-medium">
                  현재 역할: {userRole === "leader" ? "그룹장 (트레이너)" : "그룹원"}
                </div>
                <div className="text-xs text-muted-foreground">
                  {userRole === "leader"
                    ? "그룹원들을 관리하고 피드백을 제공할 수 있습니다"
                    : "그룹 활동에 참여하고 피드백을 받을 수 있습니다"}
                </div>
              </div>
            </div>
            <div className="flex gap-2">
              <Button
                variant={userRole === "member" ? "default" : "outline"}
                size="sm"
                onClick={() => {
                  setUserRole("member");
                  setSelectedGroup(null);
                }}
              >
                그룹원 뷰
              </Button>
              <Button
                variant={userRole === "leader" ? "default" : "outline"}
                size="sm"
                onClick={() => {
                  setUserRole("leader");
                  setSelectedGroup(null);
                }}
              >
                <Crown className="w-4 h-4 mr-1" />
                그룹장 뷰
              </Button>
            </div>
          </div>
        </Card>
      </div>

      {/* 그룹 선택 페이지 또는 그룹 상세 뷰 */}
      {!selectedGroup ? (
        <GroupSelectionPage
          userRole={userRole}
          onSelectGroup={handleSelectGroup}
        />
      ) : (
        <>
          {/* 뒤로가기 버튼 */}
          <div className="max-w-7xl mx-auto px-4 pt-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={handleBackToSelection}
            >
              ← 그룹 선택으로 돌아가기
            </Button>
          </div>
          
          {/* 역할에 따른 뷰 렌더링 */}
          {selectedGroup.isLeader ? <GroupLeaderPage /> : <GroupMemberPage />}
        </>
      )}
    </div>
  );
}