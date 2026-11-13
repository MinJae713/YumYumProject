import { useState } from "react";
import { GroupDashboard } from "./GroupDashboard";
import { MemberDetailView } from "./MemberDetailView";
import { Users, ArrowLeft, UserPlus, UtensilsCrossed } from "lucide-react";
import { Button } from "./ui/button";
import { InviteMemberModal } from "./InviteMemberModal";
import { MemberSelectionModal } from "./MemberSelectionModal";
import { MealRegistrationModal } from "./MealRegistrationModal";
import { toast } from "sonner@2.0.3";

export interface Member {
  id: string;
  name: string;
  avatar: string;
  goal: "diet" | "maintain" | "bulk";
  startWeight: number;
  currentWeight: number;
  targetWeight: number;
  totalCalories: number;
  exerciseCount: number;
  consecutiveDays: number;
  hasNewActivity: boolean;
  lastActivityDate: string;
  totalPoints: number;
}

interface GroupLeaderPageProps {
  groupName?: string;
}

export function GroupLeaderPage({ groupName = "다이어트 챌린지 그룹" }: GroupLeaderPageProps) {
  const [selectedMember, setSelectedMember] = useState<Member | null>(null);
  const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);
  const [isMemberSelectionOpen, setIsMemberSelectionOpen] = useState(false);
  const [isMealInputOpen, setIsMealInputOpen] = useState(false);
  const [selectedMemberForMeal, setSelectedMemberForMeal] = useState<Member | null>(null);

  // 샘플 그룹원 데이터
  const members: Member[] = [
    {
      id: "1",
      name: "김민수",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=1",
      goal: "diet",
      startWeight: 85,
      currentWeight: 78,
      targetWeight: 75,
      totalCalories: 28500,
      exerciseCount: 15,
      consecutiveDays: 7,
      hasNewActivity: true,
      lastActivityDate: "2025-11-05",
      totalPoints: 150,
    },
    {
      id: "2",
      name: "이지은",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=2",
      goal: "bulk",
      startWeight: 55,
      currentWeight: 58,
      targetWeight: 60,
      totalCalories: 32000,
      exerciseCount: 20,
      consecutiveDays: 12,
      hasNewActivity: true,
      lastActivityDate: "2025-11-05",
      totalPoints: 200,
    },
    {
      id: "3",
      name: "박준호",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=3",
      goal: "maintain",
      startWeight: 72,
      currentWeight: 72,
      targetWeight: 72,
      totalCalories: 25000,
      exerciseCount: 18,
      consecutiveDays: 5,
      hasNewActivity: false,
      lastActivityDate: "2025-11-03",
      totalPoints: 100,
    },
    {
      id: "4",
      name: "최서연",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=4",
      goal: "diet",
      startWeight: 68,
      currentWeight: 64,
      targetWeight: 58,
      totalCalories: 22000,
      exerciseCount: 12,
      consecutiveDays: 4,
      hasNewActivity: false,
      lastActivityDate: "2025-11-04",
      totalPoints: 80,
    },
    {
      id: "5",
      name: "정현우",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=5",
      goal: "bulk",
      startWeight: 70,
      currentWeight: 73,
      targetWeight: 78,
      totalCalories: 35000,
      exerciseCount: 22,
      consecutiveDays: 10,
      hasNewActivity: true,
      lastActivityDate: "2025-11-05",
      totalPoints: 220,
    },
  ];

  const handleInviteMember = (email: string) => {
    // 실제 구현에서는 서버에 초대 요청을 보냄
    console.log(`Inviting ${email} to group ${groupName}`);
  };

  const handleMemberSelection = (member: Member) => {
    setSelectedMemberForMeal(member);
    setIsMealInputOpen(true);
  };

  const handleMealSave = (mealData: any) => {
    if (selectedMemberForMeal) {
      // 실제 구현에서는 서버에 식단 데이터를 저장
      console.log(`Saving meal for ${selectedMemberForMeal.name}:`, mealData);
      toast.success(`${selectedMemberForMeal.name}님의 추천 식단이 등록되었습니다.`);
      setIsMealInputOpen(false);
      setSelectedMemberForMeal(null);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-6">
      {selectedMember ? (
        // 그룹원 상세 보기
        <>
          <div className="mb-6">
            <Button
              variant="ghost"
              onClick={() => setSelectedMember(null)}
              className="mb-4"
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              그룹 대시보드로 돌아가기
            </Button>
          </div>
          <MemberDetailView member={selectedMember} />
        </>
      ) : (
        // 그룹 대시보드
        <>
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
                <Users className="w-6 h-6 text-primary" />
              </div>
              <div>
                <h1>그룹 관리 👑</h1>
                <p className="text-sm text-muted-foreground">
                  그룹원들의 활동을 관리하고 피드백을 제공하세요
                </p>
              </div>
            </div>
            <div className="flex gap-2">
              <Button 
                variant="outline"
                onClick={() => setIsMemberSelectionOpen(true)}
              >
                <UtensilsCrossed className="w-4 h-4 mr-2" />
                그룹원 추천 식단 입력
              </Button>
              <Button onClick={() => setIsInviteModalOpen(true)}>
                <UserPlus className="w-4 h-4 mr-2" />
                그룹원 초대
              </Button>
            </div>
          </div>

          <GroupDashboard
            members={members}
            onSelectMember={setSelectedMember}
          />
        </>
      )}

      {/* 그룹원 초대 모달 */}
      <InviteMemberModal
        open={isInviteModalOpen}
        onOpenChange={setIsInviteModalOpen}
        onInviteMember={handleInviteMember}
        groupName={groupName}
      />

      {/* 그룹원 선택 모달 */}
      <MemberSelectionModal
        open={isMemberSelectionOpen}
        onOpenChange={setIsMemberSelectionOpen}
        members={members}
        onSelectMember={handleMemberSelection}
      />

      {/* 식단 입력 모달 */}
      {selectedMemberForMeal && (
        <MealRegistrationModal
          isOpen={isMealInputOpen}
          onClose={() => {
            setIsMealInputOpen(false);
            setSelectedMemberForMeal(null);
          }}
          mealType="lunch"
          date={new Date()}
          onSave={handleMealSave}
          onDelete={() => {}}
          customTitle="추천 식단 등록"
          customDescription={`${selectedMemberForMeal.name}님에게 추천할 식단을 등록하세요`}
        />
      )}
    </div>
  );
}