import { useState } from "react";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { Textarea } from "./ui/textarea";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { ArrowLeft, Send, MessageSquare } from "lucide-react";
import { Member } from "./GroupLeaderPage";

interface MemberRecordDetailProps {
  member: Member;
  record: {
    type: "meal" | "exercise";
    data: any;
    date: string;
  };
  onBack: () => void;
}

interface Feedback {
  author: string;
  content: string;
  timestamp: string;
  isTrainer: boolean;
}

export function MemberRecordDetail({
  member,
  record,
  onBack,
}: MemberRecordDetailProps) {
  const [feedbackText, setFeedbackText] = useState("");
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([
    {
      author: "트레이너",
      content: "단백질 섭취가 좋습니다! 탄수화물을 조금 더 줄여보세요.",
      timestamp: "2025-11-05 20:30",
      isTrainer: true,
    },
    {
      author: member.name,
      content: "감사합니다! 내일부터 탄수화물 양을 조절해보겠습니다.",
      timestamp: "2025-11-05 21:00",
      isTrainer: false,
    },
  ]);

  const handleSubmitFeedback = () => {
    if (!feedbackText.trim()) return;

    const newFeedback: Feedback = {
      author: "트레이너",
      content: feedbackText,
      timestamp: new Date().toLocaleString("ko-KR", {
        month: "numeric",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      }),
      isTrainer: true,
    };

    setFeedbacks([...feedbacks, newFeedback]);
    setFeedbackText("");
  };

  return (
    <div className="space-y-6">
      {/* 헤더 */}
      <div>
        <Button variant="ghost" onClick={onBack} className="mb-4">
          <ArrowLeft className="w-4 h-4 mr-2" />
          목록으로 돌아가기
        </Button>

        <div className="flex items-center gap-4">
          <Avatar className="w-12 h-12">
            <AvatarImage src={member.avatar} alt={member.name} />
            <AvatarFallback>{member.name[0]}</AvatarFallback>
          </Avatar>
          <div>
            <h2>{member.name}님의 기록</h2>
            <p className="text-sm text-muted-foreground">{record.date}</p>
          </div>
        </div>
      </div>

      {/* 기록 상세 */}
      <Card className="p-6">
        <div className="flex items-center justify-between mb-6">
          <h3>
            {record.type === "meal" ? "식단 상세" : "운동 상세"}
          </h3>
          <Badge variant="secondary">
            {record.type === "meal" ? "식단" : "운동"}
          </Badge>
        </div>

        {record.type === "meal" ? (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="p-4 bg-muted/50 rounded-lg">
                <div className="text-xs text-muted-foreground mb-1">
                  식사 시간
                </div>
                <div className="text-lg">
                  {record.data.mealType === "breakfast"
                    ? "아침"
                    : record.data.mealType === "lunch"
                    ? "점심"
                    : record.data.mealType === "dinner"
                    ? "저녁"
                    : "간식"}
                </div>
              </div>
              <div className="p-4 bg-primary/10 rounded-lg">
                <div className="text-xs text-muted-foreground mb-1">
                  총 칼로리
                </div>
                <div className="text-lg text-primary">
                  {record.data.calories} kcal
                </div>
              </div>
            </div>

            <div>
              <h4 className="text-sm mb-3">섭취한 음식</h4>
              <div className="space-y-2">
                {record.data.foods.map((food: string, idx: number) => (
                  <div
                    key={idx}
                    className="flex items-center gap-3 p-3 bg-muted/50 rounded-lg"
                  >
                    <div className="w-2 h-2 rounded-full bg-primary" />
                    <span className="text-sm">{food}</span>
                  </div>
                ))}
              </div>
            </div>

            <Card className="p-4 bg-accent/5">
              <h4 className="text-sm mb-3">영양 분석</h4>
              <div className="grid grid-cols-3 gap-3 text-center">
                <div>
                  <div className="text-xs text-muted-foreground mb-1">
                    단백질
                  </div>
                  <div className="text-lg text-secondary">45g</div>
                </div>
                <div>
                  <div className="text-xs text-muted-foreground mb-1">
                    탄수화물
                  </div>
                  <div className="text-lg text-accent">38g</div>
                </div>
                <div>
                  <div className="text-xs text-muted-foreground mb-1">
                    지방
                  </div>
                  <div className="text-lg">12g</div>
                </div>
              </div>
            </Card>
          </div>
        ) : (
          <div className="space-y-4">
            <div className="grid grid-cols-3 gap-4">
              <div className="p-4 bg-muted/50 rounded-lg">
                <div className="text-xs text-muted-foreground mb-1">
                  운동 종류
                </div>
                <div className="text-lg">{record.data.type}</div>
              </div>
              <div className="p-4 bg-secondary/10 rounded-lg">
                <div className="text-xs text-muted-foreground mb-1">
                  운동 시간
                </div>
                <div className="text-lg text-secondary">
                  {record.data.duration}분
                </div>
              </div>
              <div className="p-4 bg-primary/10 rounded-lg">
                <div className="text-xs text-muted-foreground mb-1">
                  소모 칼로리
                </div>
                <div className="text-lg text-primary">
                  {record.data.calories} kcal
                </div>
              </div>
            </div>

            <Card className="p-4 bg-accent/5">
              <h4 className="text-sm mb-3">운동 세부 정보</h4>
              <div className="space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">세트 수</span>
                  <span>3 세트</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">반복 횟수</span>
                  <span>12회</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">무게</span>
                  <span>60kg</span>
                </div>
              </div>
            </Card>
          </div>
        )}
      </Card>

      {/* 피드백 섹션 */}
      <Card className="p-6">
        <div className="flex items-center gap-2 mb-4">
          <MessageSquare className="w-5 h-5 text-primary" />
          <h3>피드백</h3>
          <Badge variant="outline">{feedbacks.length}</Badge>
        </div>

        {/* 기존 피드백 목록 */}
        <div className="space-y-4 mb-6">
          {feedbacks.length > 0 ? (
            feedbacks.map((feedback, idx) => (
              <div
                key={idx}
                className={`p-4 rounded-lg ${
                  feedback.isTrainer
                    ? "bg-primary/10 border-l-4 border-primary"
                    : "bg-muted/50"
                }`}
              >
                <div className="flex items-center gap-2 mb-2">
                  <Avatar className="w-6 h-6">
                    <AvatarFallback className="text-xs">
                      {feedback.author[0]}
                    </AvatarFallback>
                  </Avatar>
                  <span className="text-sm font-medium">{feedback.author}</span>
                  {feedback.isTrainer && (
                    <Badge variant="default" className="text-xs">
                      👑 트레이너
                    </Badge>
                  )}
                  <span className="text-xs text-muted-foreground ml-auto">
                    {feedback.timestamp}
                  </span>
                </div>
                <p className="text-sm ml-8">{feedback.content}</p>
              </div>
            ))
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              <MessageSquare className="w-12 h-12 mx-auto mb-2 opacity-50" />
              <p className="text-sm">아직 피드백이 없습니다</p>
            </div>
          )}
        </div>

        {/* 새 피드백 작성 */}
        <div className="space-y-3 pt-4 border-t border-border">
          <h4 className="text-sm">새 피드백 작성</h4>
          <Textarea
            placeholder="그룹원에게 피드백을 작성해주세요..."
            value={feedbackText}
            onChange={(e) => setFeedbackText(e.target.value)}
            rows={4}
            className="resize-none"
          />
          <div className="flex justify-end gap-2">
            <Button
              variant="outline"
              onClick={() => setFeedbackText("")}
              disabled={!feedbackText.trim()}
            >
              취소
            </Button>
            <Button
              onClick={handleSubmitFeedback}
              disabled={!feedbackText.trim()}
            >
              <Send className="w-4 h-4 mr-2" />
              피드백 전송
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
}
