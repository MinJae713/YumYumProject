import { useState } from "react";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { Users, Bell, Calendar as CalendarIcon } from "lucide-react";
import { GroupRanking } from "./GroupRanking";
import { MemberCalendarView } from "./MemberCalendarView";
import { MemberRecordWithFeedback } from "./MemberRecordWithFeedback";

export interface GroupMember {
  id: string;
  name: string;
  avatar: string;
  goal: "diet" | "maintain" | "bulk";
  currentWeight: number;
  consecutiveDays: number;
  exerciseCount: number;
  isMe: boolean;
  totalPoints: number;
}

export function GroupMemberPage() {
  const [view, setView] = useState<"calendar" | "record-detail">("calendar");
  const [selectedRecord, setSelectedRecord] = useState<{
    type: "meal" | "exercise";
    data: any;
    date: string;
  } | null>(null);
  const [unreadFeedbackCount, setUnreadFeedbackCount] = useState(2);

  // 샘플 그룹 랭킹 데이터
  const groupMembers: GroupMember[] = [
    {
      id: "1",
      name: "김민수",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=1",
      goal: "diet",
      currentWeight: 78,
      consecutiveDays: 7,
      exerciseCount: 15,
      isMe: false,
      totalPoints: 150,
    },
    {
      id: "2",
      name: "이지은",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=2",
      goal: "bulk",
      currentWeight: 58,
      consecutiveDays: 12,
      exerciseCount: 20,
      isMe: true, // 본인
      totalPoints: 200,
    },
    {
      id: "3",
      name: "박준호",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=3",
      goal: "maintain",
      currentWeight: 72,
      consecutiveDays: 5,
      exerciseCount: 18,
      isMe: false,
      totalPoints: 180,
    },
    {
      id: "4",
      name: "최서연",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=4",
      goal: "diet",
      currentWeight: 64,
      consecutiveDays: 4,
      exerciseCount: 12,
      isMe: false,
      totalPoints: 120,
    },
    {
      id: "5",
      name: "정현우",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=5",
      goal: "bulk",
      currentWeight: 73,
      consecutiveDays: 10,
      exerciseCount: 22,
      isMe: false,
      totalPoints: 220,
    },
  ];

  const handleRecordClick = (record: {
    type: "meal" | "exercise";
    data: any;
    date: string;
  }) => {
    setSelectedRecord(record);
    setView("record-detail");
    // 피드백을 확인하면 알림 카운트 감소
    if (unreadFeedbackCount > 0) {
      setUnreadFeedbackCount(unreadFeedbackCount - 1);
    }
  };

  const handleBackToCalendar = () => {
    setView("calendar");
    setSelectedRecord(null);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-6">
      {/* 헤더 */}
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
            <Users className="w-6 h-6 text-primary" />
          </div>
          <div>
            <h1>그룹 활동</h1>
            <p className="text-sm text-muted-foreground">
              그룹원들과 함께 목표를 달성하세요
            </p>
          </div>
        </div>

        {/* 새 피드백 알림 */}
        {unreadFeedbackCount > 0 && (
          <Button variant="outline" className="relative">
            <Bell className="w-4 h-4 mr-2" />
            새 피드백
            <Badge className="ml-2 bg-destructive">{unreadFeedbackCount}</Badge>
            <span className="absolute -top-1 -right-1 w-3 h-3 bg-destructive rounded-full animate-pulse" />
          </Button>
        )}
      </div>

      {/* 그룹 랭킹 */}
      <div className="mb-6">
        <GroupRanking members={groupMembers} />
      </div>

      {/* 메인 컨텐츠 */}
      {view === "calendar" && selectedRecord === null && (
        <Card className="p-6">
          <div className="flex items-center gap-2 mb-6">
            <CalendarIcon className="w-5 h-5 text-primary" />
            <h3>나의 활동 기록</h3>
          </div>
          <MemberCalendarView onRecordClick={handleRecordClick} />
        </Card>
      )}

      {view === "record-detail" && selectedRecord && (
        <MemberRecordWithFeedback
          record={selectedRecord}
          onBack={handleBackToCalendar}
        />
      )}
    </div>
  );
}