import { Calendar } from "./ui/calendar";
import { Card } from "./ui/card";
import { DailyMeals } from "./MealPage";

interface MealCalendarProps {
  selectedDate: Date;
  onSelectDate: (date: Date | undefined) => void;
  mealsData: DailyMeals;
}

export function MealCalendar({ selectedDate, onSelectDate, mealsData }: MealCalendarProps) {
  // mealsData에서 식단이 있는 날짜들을 추출
  const datesWithMeals = Object.keys(mealsData).map((dateKey) => {
    const [year, month, day] = dateKey.split("-").map(Number);
    return new Date(year, month - 1, day);
  });

  return (
    <Card className="p-6">
      <div className="mb-4">
        <h3 className="mb-2">식단 캘린더</h3>
        <div className="flex items-center gap-2 text-xs text-muted-foreground">
          <div className="flex items-center gap-1">
            <div className="w-2 h-2 rounded-full bg-primary" />
            <span>식단 등록됨</span>
          </div>
        </div>
      </div>
      
      <div className="relative">
        <Calendar
          mode="single"
          selected={selectedDate}
          onSelect={onSelectDate}
          modifiers={{
            hasMeal: datesWithMeals,
          }}
          modifiersClassNames={{
            hasMeal: "relative after:absolute after:bottom-1 after:left-1/2 after:-translate-x-1/2 after:w-1 after:h-1 after:rounded-full after:bg-primary",
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
