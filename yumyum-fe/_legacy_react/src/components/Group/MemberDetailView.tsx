import { useState } from "react";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Calendar } from "./ui/calendar";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import {
  TrendingUp,
  TrendingDown,
  Minus,
  Target,
  Calendar as CalendarIcon,
  Utensils,
  Dumbbell,
} from "lucide-react";
import { Member } from "./GroupLeaderPage";
import { MemberRecordDetail } from "./MemberRecordDetail";

interface MemberDetailViewProps {
  member: Member;
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
  feedback?: {
    author: string;
    content: string;
    timestamp: string;
  }[];
}

export function MemberDetailView({ member }: MemberDetailViewProps) {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [selectedRecord, setSelectedRecord] = useState<{
    type: "meal" | "exercise";
    data: any;
    date: string;
  } | null>(null);

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
      feedback: [
        {
          author: "트레이너",
          content: "단백질 섭취가 좋습니다! 탄수화물을 조금 더 줄여보세요.",
          timestamp: "2025-11-05 20:30",
        },
      ],
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

  const getGoalIcon = () => {
    const weightChange = member.currentWeight - member.startWeight;
    if (weightChange < 0) {
      return <TrendingDown className="w-5 h-5 text-secondary" />;
    } else if (weightChange > 0) {
      return <TrendingUp className="w-5 h-5 text-accent" />;
    } else {
      return <Minus className="w-5 h-5 text-muted-foreground" />;
    }
  };

  const getGoalLabel = (goal: string) => {
    switch (goal) {
      case "diet":
        return "다이어트";
      case "bulk":
        return "벌크업";
      case "maintain":
        return "유지";
      default:
        return goal;
    }
  };

  if (selectedRecord) {
    return (
      <MemberRecordDetail
        member={member}
        record={selectedRecord}
        onBack={() => setSelectedRecord(null)}
      />
    );
  }

  return (
    <div className="space-y-6">
      {/* 회원 정보 카드 */}
      <Card className="p-6">
        <div className="flex items-start gap-6">
          <Avatar className="w-20 h-20">
            <AvatarImage src={member.avatar} alt={member.name} />
            <AvatarFallback>{member.name[0]}</AvatarFallback>
          </Avatar>

          <div className="flex-1">
            <div className="flex items-center gap-3 mb-2">
              <h2>{member.name}</h2>
              <Badge variant="secondary">{getGoalLabel(member.goal)}</Badge>
            </div>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4">
              <div>
                <div className="text-xs text-muted-foreground mb-1">
                  시작 체중
                </div>
                <div className="text-lg">{member.startWeight}kg</div>
              </div>
              <div>
                <div className="text-xs text-muted-foreground mb-1">
                  현재 체중
                </div>
                <div className="flex items-center gap-2">
                  {getGoalIcon()}
                  <span className="text-lg">{member.currentWeight}kg</span>
                </div>
              </div>
              <div>
                <div className="text-xs text-muted-foreground mb-1">
                  목표 체중
                </div>
                <div className="flex items-center gap-2">
                  <Target className="w-5 h-5 text-primary" />
                  <span className="text-lg">{member.targetWeight}kg</span>
                </div>
              </div>
              <div>
                <div className="text-xs text-muted-foreground mb-1">
                  변화량
                </div>
                <div className="text-lg">
                  {member.currentWeight - member.startWeight > 0 ? "+" : ""}
                  {(member.currentWeight - member.startWeight).toFixed(1)}kg
                </div>
              </div>
            </div>
          </div>
        </div>
      </Card>

      {/* 캘린더 + 일일 기록 */}
      <div className="grid lg:grid-cols-[400px_1fr] gap-6">
        {/* 캘린더 */}
        <Card className="p-6">
          <div className="mb-4">
            <h3 className="mb-2">활동 캘린더</h3>
            <div className="flex items-center gap-2 text-xs text-muted-foreground">
              <div className="flex items-center gap-1">
                <div className="w-2 h-2 rounded-full bg-primary" />
                <span>기록 있음</span>
              </div>
            </div>
          </div>

          <Calendar
            mode="single"
            selected={selectedDate}
            onSelect={(date) => date && setSelectedDate(date)}
            modifiers={{
              hasRecord: datesWithRecords,
            }}
            modifiersClassNames={{
              hasRecord:
                "relative after:absolute after:bottom-1 after:left-1/2 after:-translate-x-1/2 after:w-1 after:h-1 after:rounded-full after:bg-primary",
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
        </Card>

        {/* 일일 기록 */}
        <Card className="p-6">
          <div className="flex items-center gap-2 mb-6">
            <CalendarIcon className="w-5 h-5 text-primary" />
            <h3>
              {selectedDate.toLocaleDateString("ko-KR", {
                month: "long",
                day: "numeric",
                weekday: "long",
              })}
            </h3>
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
                      className="p-4 cursor-pointer hover:shadow-md transition-all"
                      onClick={() =>
                        setSelectedRecord({
                          type: "meal",
                          data: { mealType, ...meal },
                          date: formatDateKey(selectedDate),
                        })
                      }
                    >
                      <div className="flex items-center justify-between mb-2">
                        <h4 className="text-sm">{mealLabels[mealType]}</h4>
                        <Badge variant="outline" className="text-xs">
                          {meal.calories} kcal
                        </Badge>
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
                    className="p-4 cursor-pointer hover:shadow-md transition-all"
                    onClick={() =>
                      setSelectedRecord({
                        type: "exercise",
                        data: exercise,
                        date: formatDateKey(selectedDate),
                      })
                    }
                  >
                    <div className="flex items-center justify-between mb-2">
                      <h4 className="text-sm">{exercise.type}</h4>
                      <Badge variant="outline" className="text-xs">
                        {exercise.calories} kcal
                      </Badge>
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
        </Card>
      </div>
    </div>
  );
}
