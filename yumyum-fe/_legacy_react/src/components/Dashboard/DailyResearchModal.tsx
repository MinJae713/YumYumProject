import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import { Button } from "./ui/button";
import { Card } from "./ui/card";
import { CheckCircle2, Sparkles } from "lucide-react";

interface DailyResearchModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onComplete: () => void;
}

export function DailyResearchModal({
  open,
  onOpenChange,
  onComplete,
}: DailyResearchModalProps) {
  const [selectedCondition, setSelectedCondition] = useState<number | null>(
    null
  );
  const [selectedSchedule, setSelectedSchedule] = useState<string | null>(null);

  const conditions = [
    { value: 5, emoji: "😄", label: "매우 좋음" },
    { value: 4, emoji: "🙂", label: "좋음" },
    { value: 3, emoji: "😐", label: "보통" },
    { value: 2, emoji: "😕", label: "나쁨" },
    { value: 1, emoji: "😞", label: "매우 나쁨" },
  ];

  const schedules = [
    { value: "same", label: "평소와 같아요", color: "bg-primary/10" },
    { value: "slightly", label: "조금 달라요", color: "bg-secondary/10" },
    { value: "busy", label: "많이 바빠요", color: "bg-accent/10" },
  ];

  const handleSubmit = () => {
    if (selectedCondition && selectedSchedule) {
      // 리서치 결과 저장 로직
      console.log("Condition:", selectedCondition);
      console.log("Schedule:", selectedSchedule);
      onComplete();
      // 선택 초기화
      setSelectedCondition(null);
      setSelectedSchedule(null);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <div className="flex items-center gap-2 mb-2">
            <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-primary to-secondary flex items-center justify-center">
              <Sparkles className="w-5 h-5 text-white" />
            </div>
            <DialogTitle>오늘의 컨디션 체크</DialogTitle>
          </div>
          <DialogDescription>
            AI 추천을 위해 오늘의 상태를 알려주세요. 빠르게 답변할 수 있어요!
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6 py-4">
          {/* 질문 1: 컨디션 */}
          <div>
            <h4 className="text-sm mb-3">
              오늘 컨디션은 어떤가요?
            </h4>
            <div className="grid grid-cols-5 gap-2">
              {conditions.map((condition) => (
                <button
                  key={condition.value}
                  onClick={() => setSelectedCondition(condition.value)}
                  className={`p-3 rounded-lg border-2 transition-all hover:scale-105 ${
                    selectedCondition === condition.value
                      ? "border-primary bg-primary/10 shadow-md"
                      : "border-border hover:border-primary/50"
                  }`}
                >
                  <div className="text-3xl mb-1">{condition.emoji}</div>
                  <div className="text-xs text-center">{condition.label}</div>
                </button>
              ))}
            </div>
          </div>

          {/* 질문 2: 스케줄 */}
          <div>
            <h4 className="text-sm mb-3">
              내일 스케줄은 평소와 다른가요?
            </h4>
            <div className="grid grid-cols-1 gap-2">
              {schedules.map((schedule) => (
                <button
                  key={schedule.value}
                  onClick={() => setSelectedSchedule(schedule.value)}
                  className={`p-4 rounded-lg border-2 transition-all text-left ${
                    selectedSchedule === schedule.value
                      ? "border-primary bg-primary/10 shadow-md"
                      : "border-border hover:border-primary/50"
                  } ${schedule.color}`}
                >
                  <div className="flex items-center justify-between">
                    <span className="text-sm">{schedule.label}</span>
                    {selectedSchedule === schedule.value && (
                      <CheckCircle2 className="w-5 h-5 text-primary" />
                    )}
                  </div>
                </button>
              ))}
            </div>
          </div>

          {/* 선택 완료 표시 */}
          {selectedCondition && selectedSchedule && (
            <Card className="p-3 bg-gradient-to-r from-primary/10 to-secondary/10 border-primary/20">
              <div className="flex items-center gap-2 text-sm">
                <CheckCircle2 className="w-4 h-4 text-primary" />
                <span className="text-muted-foreground">
                  모든 질문에 답변하셨습니다. 제출해주세요!
                </span>
              </div>
            </Card>
          )}
        </div>

        <DialogFooter>
          <Button
            variant="outline"
            onClick={() => {
              setSelectedCondition(null);
              setSelectedSchedule(null);
              onOpenChange(false);
            }}
          >
            나중에
          </Button>
          <Button
            onClick={handleSubmit}
            disabled={!selectedCondition || !selectedSchedule}
          >
            제출하기
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
