import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Users, Plus, ChevronRight } from "lucide-react";
import { CreateGroupModal } from "./CreateGroupModal";

interface Group {
  id: string;
  name: string;
  memberCount: number;
  isLeader: boolean;
}

interface GroupSelectionPageProps {
  userRole: "leader" | "member";
  onSelectGroup: (groupId: string, isLeader: boolean) => void;
}

export function GroupSelectionPage({
  userRole,
  onSelectGroup,
}: GroupSelectionPageProps) {
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [groups, setGroups] = useState<Group[]>([
    {
      id: "1",
      name: "다이어트 챌린지 그룹",
      memberCount: 5,
      isLeader: userRole === "leader",
    },
    {
      id: "2",
      name: "벌크업 트레이닝",
      memberCount: 8,
      isLeader: false,
    },
    {
      id: "3",
      name: "헬스 비긴 그룹",
      memberCount: 12,
      isLeader: userRole === "leader",
    },
  ]);

  const handleCreateGroup = (groupName: string) => {
    const newGroup: Group = {
      id: `group-${Date.now()}`,
      name: groupName,
      memberCount: 1,
      isLeader: true,
    };
    setGroups([...groups, newGroup]);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-6">
      {/* 헤더 */}
      <div className="flex items-center gap-3 mb-6">
        <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
          <Users className="w-6 h-6 text-primary" />
        </div>
        <div>
          <h1>그룹 선택</h1>
          <p className="text-sm text-muted-foreground">
            {userRole === "leader"
              ? "관리할 그룹을 선택하거나 새로운 그룹을 생성하세요"
              : "참여 중인 그룹을 선택하세요"}
          </p>
        </div>
      </div>

      {/* 그룹 목록 */}
      <div className="space-y-3">
        {groups.map((group) => (
          <Card
            key={group.id}
            className="p-4 hover:shadow-md transition-shadow cursor-pointer"
            onClick={() => onSelectGroup(group.id, group.isLeader)}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3 flex-1">
                <div className="w-10 h-10 bg-primary/10 rounded-lg flex items-center justify-center">
                  <Users className="w-5 h-5 text-primary" />
                </div>
                <div className="flex-1">
                  <div className="flex items-center gap-2">
                    <h3 className="text-sm">{group.name}</h3>
                    {group.isLeader && (
                      <span className="text-base">👑</span>
                    )}
                  </div>
                  <p className="text-xs text-muted-foreground">
                    그룹원 {group.memberCount}명
                  </p>
                </div>
              </div>
              <ChevronRight className="w-5 h-5 text-muted-foreground" />
            </div>
          </Card>
        ))}

        {/* 그룹 생성 버튼 (그룹장만) */}
        {userRole === "leader" && (
          <Button
            variant="outline"
            className="w-full h-16 border-dashed"
            onClick={() => setIsCreateModalOpen(true)}
          >
            <Plus className="w-5 h-5 mr-2" />
            그룹 생성
          </Button>
        )}
      </div>

      {/* 그룹이 없는 경우 */}
      {groups.length === 0 && (
        <Card className="p-8 text-center">
          <Users className="w-12 h-12 text-muted-foreground mx-auto mb-3" />
          <h3 className="text-sm mb-2">참여 중인 그룹이 없습니다</h3>
          <p className="text-sm text-muted-foreground mb-4">
            {userRole === "leader"
              ? "새로운 그룹을 생성하여 그룹원들을 관리해보세요"
              : "그룹장의 초대를 통해 그룹에 참여할 수 있습니다"}
          </p>
          {userRole === "leader" && (
            <Button onClick={() => setIsCreateModalOpen(true)}>
              <Plus className="w-4 h-4 mr-2" />
              첫 그룹 만들기
            </Button>
          )}
        </Card>
      )}

      {/* 그룹 생성 모달 */}
      <CreateGroupModal
        open={isCreateModalOpen}
        onOpenChange={setIsCreateModalOpen}
        onCreateGroup={handleCreateGroup}
      />
    </div>
  );
}