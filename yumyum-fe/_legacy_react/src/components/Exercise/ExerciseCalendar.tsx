import { Calendar } from "./ui/calendar";
import { Card } from "./ui/card";

interface ExerciseCalendarProps {
  selectedDate: Date;
  onSelectDate: (date: Date | undefined) => void;
}

// 운동이 등록된 날짜 목록 (실제로는 API에서 가져옴)
const datesWithExercise = [
  new Date(2025, 10, 1),
  new Date(2025, 10, 2),
  new Date(2025, 10, 3),
  new Date(2025, 10, 4),
  new Date(2025, 10, 5),
];

export function ExerciseCalendar({ selectedDate, onSelectDate }: ExerciseCalendarProps) {
  return (
    <Card className="p-6">
      <div className="mb-4">
        <h3 className="mb-2">운동 캘린더</h3>
        <div className="flex items-center gap-2 text-xs text-muted-foreground">
          <div className="flex items-center gap-1">
            <div className="w-2 h-2 rounded-full bg-secondary" />
            <span>운동 기록됨</span>
          </div>
        </div>
      </div>
      
      <div className="relative">
        <Calendar
          mode="single"
          selected={selectedDate}
          onSelect={onSelectDate}
          modifiers={{
            hasExercise: datesWithExercise,
          }}
          modifiersClassNames={{
            hasExercise: "relative after:absolute after:bottom-1 after:left-1/2 after:-translate-x-1/2 after:w-1 after:h-1 after:rounded-full after:bg-secondary",
          }}
        />
      </div>
      
      <div className="mt-4 p-3 bg-muted/50 rounded-lg">
        <div className="text-xs text-muted-foreground mb-1">선택한 날짜</div>
        <div className="text-sm">
          {selectedDate.toLocaleDateString("ko-KR", {
            year: "numeric",
            month: "long",
            day: "numeric",
            weekday: "long",
          })}
        </div>
      </div>
    </Card>
  );
}
