import { useState } from "react";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Calendar } from "./ui/calendar";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import {
  Calendar as CalendarIcon,
  Utensils,
  Dumbbell,
  MessageSquare,
} from "lucide-react";

interface MemberCalendarViewProps {
  onRecordClick: (record: {
    type: "meal" | "exercise";
    data: any;
    date: string;
  }) => void;
}

interface DailyRecord {
  date: string;
  meals: {
    breakfast?: { calories: number; foods: string[] };
    lunch?: { calories: number; foods: string[] };
    dinner?: { calories: number; foods: string[] };
    snack?: { calories: number; foods: string[] };
  };
  exercises: {
    type: string;
    duration: number;
    calories: number;
  }[];
  totalCalories: number;
  totalExerciseCalories: number;
  hasFeedback: boolean;
  feedbackCount: number;
}

export function MemberCalendarView({ onRecordClick }: MemberCalendarViewProps) {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());

  // 샘플 기록 데이터
  const records: { [key: string]: DailyRecord } = {
    "2025-11-5": {
      date: "2025-11-05",
      meals: {
        breakfast: {
          calories: 420,
          foods: ["닭가슴살 150g", "브로콜리 100g", "현미밥 100g"],
        },
        lunch: {
          calories: 550,
          foods: ["연어구이 150g", "샐러드", "고구마 200g"],
        },
        dinner: {
          calories: 480,
          foods: ["소고기 120g", "채소볶음", "현미밥 80g"],
        },
      },
      exercises: [
        { type: "벤치프레스", duration: 45, calories: 250 },
        { type: "스쿼트", duration: 30, calories: 180 },
      ],
      totalCalories: 1450,
      totalExerciseCalories: 430,
      hasFeedback: true,
      feedbackCount: 2,
    },
    "2025-11-4": {
      date: "2025-11-04",
      meals: {
        breakfast: {
          calories: 380,
          foods: ["계란 3개", "통밀빵 2조각", "아보카도"],
        },
        lunch: {
          calories: 520,
          foods: ["닭가슴살 샐러드", "고구마 150g"],
        },
      },
      exercises: [{ type: "러닝", duration: 60, calories: 400 }],
      totalCalories: 900,
      totalExerciseCalories: 400,
      hasFeedback: true,
      feedbackCount: 1,
    },
    "2025-11-3": {
      date: "2025-11-03",
      meals: {
        breakfast: {
          calories: 450,
          foods: ["오트밀", "바나나", "견과류"],
        },
        lunch: {
          calories: 600,
          foods: ["제육볶음", "현미밥", "된장찌개"],
        },
        dinner: {
          calories: 500,
          foods: ["삼치구이", "나물 3종", "현미밥"],
        },
      },
      exercises: [
        { type: "데드리프트", duration: 40, calories: 280 },
        { type: "풀업", duration: 20, calories: 120 },
      ],
      totalCalories: 1550,
      totalExerciseCalories: 400,
      hasFeedback: false,
      feedbackCount: 0,
    },
  };

  const formatDateKey = (date: Date) => {
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
  };

  const currentRecord = records[formatDateKey(selectedDate)];
  const datesWithRecords = Object.keys(records).map((dateKey) => {
    const [year, month, day] = dateKey.split("-").map(Number);
    return new Date(year, month - 1, day);
  });

  const datesWithFeedback = Object.entries(records)
    .filter(([_, record]) => record.hasFeedback)
    .map(([dateKey]) => {
      const [year, month, day] = dateKey.split("-").map(Number);
      return new Date(year, month - 1, day);
    });

  return (
    <div className="grid lg:grid-cols-[400px_1fr] gap-6">
      {/* 캘린더 */}
      <div>
        <div className="mb-4">
          <div className="flex items-center gap-2 text-xs text-muted-foreground mb-3">
            <div className="flex items-center gap-1">
              <div className="w-2 h-2 rounded-full bg-primary" />
              <span>기록 있음</span>
            </div>
            <div className="flex items-center gap-1">
              <MessageSquare className="w-3 h-3 text-accent" />
              <span>피드백 있음</span>
            </div>
          </div>
        </div>

        <Calendar
          mode="single"
          selected={selectedDate}
          onSelect={(date) => date && setSelectedDate(date)}
          modifiers={{
            hasRecord: datesWithRecords,
            hasFeedback: datesWithFeedback,
          }}
          modifiersClassNames={{
            hasRecord:
              "relative after:absolute after:bottom-1 after:left-1/2 after:-translate-x-1/2 after:w-1 after:h-1 after:rounded-full after:bg-primary",
            hasFeedback:
              "relative before:absolute before:top-1 before:right-1 before:w-2 before:h-2 before:rounded-full before:bg-accent",
          }}
        />

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
      </div>

      {/* 일일 기록 */}
      <div>
        <div className="flex items-center gap-2 mb-6">
          <CalendarIcon className="w-5 h-5 text-primary" />
          <h3>
            {selectedDate.toLocaleDateString("ko-KR", {
              month: "long",
              day: "numeric",
              weekday: "long",
            })}
          </h3>
          {currentRecord?.hasFeedback && (
            <Badge variant="default" className="ml-2">
              <MessageSquare className="w-3 h-3 mr-1" />
              피드백 {currentRecord.feedbackCount}
            </Badge>
          )}
        </div>

        {currentRecord ? (
          <Tabs defaultValue="meals" className="w-full">
            <TabsList className="grid w-full grid-cols-2">
              <TabsTrigger value="meals">
                <Utensils className="w-4 h-4 mr-2" />
                식단
              </TabsTrigger>
              <TabsTrigger value="exercises">
                <Dumbbell className="w-4 h-4 mr-2" />
                운동
              </TabsTrigger>
            </TabsList>

            <TabsContent value="meals" className="space-y-3 mt-4">
              {/* 식단 요약 */}
              <Card className="p-4 bg-primary/5">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-muted-foreground">
                    총 섭취 칼로리
                  </span>
                  <span className="text-2xl text-primary">
                    {currentRecord.totalCalories}
                  </span>
                  <span className="text-sm text-muted-foreground">kcal</span>
                </div>
              </Card>

              {/* 식사별 기록 */}
              {Object.entries(currentRecord.meals).map(([mealType, meal]) => {
                const mealLabels: { [key: string]: string } = {
                  breakfast: "아침",
                  lunch: "점심",
                  dinner: "저녁",
                  snack: "간식",
                };

                return (
                  <Card
                    key={mealType}
                    className="p-4 cursor-pointer hover:shadow-md transition-all hover:border-primary/50"
                    onClick={() =>
                      onRecordClick({
                        type: "meal",
                        data: { mealType, ...meal },
                        date: formatDateKey(selectedDate),
                      })
                    }
                  >
                    <div className="flex items-center justify-between mb-2">
                      <h4 className="text-sm">{mealLabels[mealType]}</h4>
                      <div className="flex items-center gap-2">
                        <Badge variant="outline" className="text-xs">
                          {meal.calories} kcal
                        </Badge>
                        {currentRecord.hasFeedback && (
                          <MessageSquare className="w-4 h-4 text-accent" />
                        )}
                      </div>
                    </div>
                    <div className="space-y-1">
                      {meal.foods.map((food, idx) => (
                        <div
                          key={idx}
                          className="text-xs text-muted-foreground flex items-center gap-2"
                        >
                          <span className="text-primary">•</span>
                          <span>{food}</span>
                        </div>
                      ))}
                    </div>
                  </Card>
                );
              })}
            </TabsContent>

            <TabsContent value="exercises" className="space-y-3 mt-4">
              {/* 운동 요약 */}
              <Card className="p-4 bg-secondary/5">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-muted-foreground">
                    총 소모 칼로리
                  </span>
                  <span className="text-2xl text-secondary">
                    {currentRecord.totalExerciseCalories}
                  </span>
                  <span className="text-sm text-muted-foreground">kcal</span>
                </div>
              </Card>

              {/* 운동별 기록 */}
              {currentRecord.exercises.map((exercise, idx) => (
                <Card
                  key={idx}
                  className="p-4 cursor-pointer hover:shadow-md transition-all hover:border-secondary/50"
                  onClick={() =>
                    onRecordClick({
                      type: "exercise",
                      data: exercise,
                      date: formatDateKey(selectedDate),
                    })
                  }
                >
                  <div className="flex items-center justify-between mb-2">
                    <h4 className="text-sm">{exercise.type}</h4>
                    <div className="flex items-center gap-2">
                      <Badge variant="outline" className="text-xs">
                        {exercise.calories} kcal
                      </Badge>
                      {currentRecord.hasFeedback && (
                        <MessageSquare className="w-4 h-4 text-accent" />
                      )}
                    </div>
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {exercise.duration}분
                  </div>
                </Card>
              ))}
            </TabsContent>
          </Tabs>
        ) : (
          <div className="flex flex-col items-center justify-center py-12 text-center">
            <div className="w-16 h-16 rounded-full bg-muted flex items-center justify-center mb-4">
              <CalendarIcon className="w-8 h-8 text-muted-foreground" />
            </div>
            <h4 className="mb-2">기록이 없습니다</h4>
            <p className="text-sm text-muted-foreground">
              해당 날짜에 등록된 식단이나 운동이 없습니다.
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
