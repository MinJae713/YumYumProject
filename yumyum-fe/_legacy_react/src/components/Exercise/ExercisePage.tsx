import { useState } from "react";
import { ExerciseCalendar } from "./ExerciseCalendar";
import { DailyExerciseList } from "./DailyExerciseList";
import { Dumbbell } from "lucide-react";

export function ExercisePage() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());

  return (
    <div className="max-w-7xl mx-auto px-4 py-6">
      {/* 헤더 */}
      <div className="flex items-center gap-3 mb-6">
        <div className="w-12 h-12 bg-secondary/10 rounded-lg flex items-center justify-center">
          <Dumbbell className="w-6 h-6 text-secondary" />
        </div>
        <div>
          <h1>운동 관리</h1>
          <p className="text-sm text-muted-foreground">
            날짜별 운동을 기록하고 칼로리 소모량을 확인하세요
          </p>
        </div>
      </div>

      {/* 2단 레이아웃: 캘린더 + 운동 목록 */}
      <div className="grid lg:grid-cols-[400px_1fr] gap-6">
        {/* 좌측: 캘린더 */}
        <div>
          <ExerciseCalendar
            selectedDate={selectedDate}
            onSelectDate={(date) => date && setSelectedDate(date)}
          />
        </div>

        {/* 우측: 운동 목록 */}
        <div>
          <DailyExerciseList selectedDate={selectedDate} />
        </div>
      </div>
    </div>
  );
}
