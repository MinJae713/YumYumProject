import { useState } from "react";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { Textarea } from "./ui/textarea";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { ArrowLeft, Send, MessageSquare, Crown } from "lucide-react";

interface MemberRecordWithFeedbackProps {
  record: {
    type: "meal" | "exercise";
    data: any;
    date: string;
  };
  onBack: () => void;
}

interface Feedback {
  id: string;
  author: string;
  content: string;
  timestamp: string;
  isTrainer: boolean;
  avatar: string;
}

export function MemberRecordWithFeedback({
  record,
  onBack,
}: MemberRecordWithFeedbackProps) {
  const [feedbackText, setFeedbackText] = useState("");
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([
    {
      id: "1",
      author: "김코치",
      content: "단백질 섭취가 아주 좋습니다! 👍 탄수화물을 조금 더 줄이면 목표 달성이 더 빨라질 거예요.",
      timestamp: "2025-11-05 20:30",
      isTrainer: true,
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=coach",
    },
    {
      id: "2",
      author: "나",
      content: "감사합니다! 내일부터 탄수화물 양을 조절해보겠습니다.",
      timestamp: "2025-11-05 21:00",
      isTrainer: false,
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=2",
    },
    {
      id: "3",
      author: "김코치",
      content: "좋아요! 그리고 저녁에는 현미밥을 고구마로 대체해보는 것도 추천드립니다.",
      timestamp: "2025-11-05 21:15",
      isTrainer: true,
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=coach",
    },
  ]);

  const handleSubmitFeedback = () => {
    if (!feedbackText.trim()) return;

    const newFeedback: Feedback = {
      id: Date.now().toString(),
      author: "나",
      content: feedbackText,
      timestamp: new Date().toLocaleString("ko-KR", {
        month: "numeric",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      }),
      isTrainer: false,
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=2",
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
          캘린더로 돌아가기
        </Button>

        <div className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-lg bg-primary/10 flex items-center justify-center">
            {record.type === "meal" ? (
              <span className="text-2xl">🍽️</span>
            ) : (
              <span className="text-2xl">💪</span>
            )}
          </div>
          <div>
            <h2>나의 기록</h2>
            <p className="text-sm text-muted-foreground">{record.date}</p>
          </div>
        </div>
      </div>

      {/* 기록 상세 */}
      <Card className="p-6">
        <div className="flex items-center justify-between mb-6">
          <h3>{record.type === "meal" ? "식단 상세" : "운동 상세"}</h3>
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
          <h3>피드백 및 댓글</h3>
          <Badge variant="outline">{feedbacks.length}</Badge>
        </div>

        {/* 피드백 목록 */}
        <div className="space-y-4 mb-6">
          {feedbacks.map((feedback) => (
            <div
              key={feedback.id}
              className={`p-4 rounded-lg transition-all ${
                feedback.isTrainer
                  ? "bg-gradient-to-r from-primary/10 to-secondary/10 border-l-4 border-primary"
                  : "bg-muted/50"
              }`}
            >
              <div className="flex items-start gap-3">
                <Avatar className="w-8 h-8">
                  <AvatarImage src={feedback.avatar} alt={feedback.author} />
                  <AvatarFallback>{feedback.author[0]}</AvatarFallback>
                </Avatar>

                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="text-sm font-medium">
                      {feedback.author}
                    </span>
                    {feedback.isTrainer && (
                      <Badge variant="default" className="text-xs">
                        <Crown className="w-3 h-3 mr-1" />
                        트레이너
                      </Badge>
                    )}
                    <span className="text-xs text-muted-foreground ml-auto">
                      {feedback.timestamp}
                    </span>
                  </div>
                  <p className="text-sm">{feedback.content}</p>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* 새 댓글 작성 */}
        <div className="space-y-3 pt-4 border-t border-border">
          <h4 className="text-sm">댓글 작성</h4>
          <Textarea
            placeholder="트레이너의 피드백에 답변하거나 추가 질문을 남겨보세요..."
            value={feedbackText}
            onChange={(e) => setFeedbackText(e.target.value)}
            rows={3}
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
              댓글 작성
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
}
