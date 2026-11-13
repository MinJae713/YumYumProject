import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Plus, Dumbbell, Clock, Flame } from "lucide-react";
import { ExerciseRegistrationModal } from "./ExerciseRegistrationModal";

interface ExerciseItem {
  name: string;
  duration: number;
  calories: number;
  sets?: number;
  reps?: number;
}

interface TodayExerciseProps {
  onExercisesChange?: (exercises: ExerciseItem[]) => void;
}

export function TodayExercise({ onExercisesChange }: TodayExerciseProps) {
  const [exercises, setExercises] = useState<ExerciseItem[]>([
    {
      name: "런닝머신",
      duration: 30,
      calories: 280,
    },
    {
      name: "벤치프레스",
      duration: 20,
      calories: 150,
      sets: 4,
      reps: 12,
    },
    {
      name: "스쿼트",
      duration: 15,
      calories: 120,
      sets: 3,
      reps: 15,
    },
  ]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedExercise, setSelectedExercise] = useState<ExerciseItem | null>(
    null
  );

  const hasExercises = exercises.length > 0;
  const totalDuration = exercises.reduce((sum, ex) => sum + ex.duration, 0);
  const totalCalories = exercises.reduce((sum, ex) => sum + ex.calories, 0);

  const handleOpenModal = (exercise?: ExerciseItem) => {
    setSelectedExercise(exercise || null);
    setIsModalOpen(true);
  };

  const handleAddExercise = () => {
    setSelectedExercise(null);
    setIsModalOpen(true);
  };

  return (
    <>
      <Card className="p-6">
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
              <Dumbbell className="w-6 h-6 text-primary" />
            </div>
            <div>
              <h3>오늘의 운동</h3>
              <p className="text-xs text-muted-foreground">
                {hasExercises
                  ? `${exercises.length}개 운동 완료`
                  : "등록된 운동이 없습니다"}
              </p>
            </div>
          </div>
          <Button size="sm" onClick={handleAddExercise}>
            <Plus className="w-4 h-4 mr-1" />
            운동 추가
          </Button>
        </div>

        {hasExercises ? (
          <div className="space-y-4">
            {/* 운동 요약 */}
            <div className="grid grid-cols-2 gap-3">
              <div className="bg-muted/50 rounded-lg p-3 flex items-center gap-3">
                <div className="w-10 h-10 bg-secondary/10 rounded-lg flex items-center justify-center">
                  <Clock className="w-5 h-5 text-secondary" />
                </div>
                <div>
                  <div className="text-xs text-muted-foreground">
                    총 운동 시간
                  </div>
                  <div className="text-lg">{totalDuration}분</div>
                </div>
              </div>
              <div className="bg-muted/50 rounded-lg p-3 flex items-center gap-3">
                <div className="w-10 h-10 bg-accent/10 rounded-lg flex items-center justify-center">
                  <Flame className="w-5 h-5 text-accent" />
                </div>
                <div>
                  <div className="text-xs text-muted-foreground">
                    소모 칼로리
                  </div>
                  <div className="text-lg">{totalCalories} kcal</div>
                </div>
              </div>
            </div>

            {/* 운동 목록 */}
            <div className="space-y-2">
              {exercises.map((exercise, index) => (
                <button
                  key={index}
                  onClick={() => handleOpenModal(exercise)}
                  className="w-full flex items-center justify-between p-3 border border-border rounded-lg hover:bg-muted/50 transition-all text-left"
                >
                  <div className="flex-1">
                    <h4 className="text-sm mb-1">{exercise.name}</h4>
                    <div className="flex items-center gap-3 text-xs text-muted-foreground">
                      <span className="flex items-center gap-1">
                        <Clock className="w-3 h-3" />
                        {exercise.duration}분
                      </span>
                      {exercise.sets && exercise.reps && (
                        <span>
                          {exercise.sets}세트 × {exercise.reps}회
                        </span>
                      )}
                    </div>
                  </div>
                  <Badge variant="outline">{exercise.calories} kcal</Badge>
                </button>
              ))}
            </div>
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center py-12">
            <div className="w-20 h-20 rounded-full bg-muted/50 flex items-center justify-center mb-4">
              <Plus className="w-10 h-10 text-muted-foreground" />
            </div>
            <p className="text-sm text-muted-foreground mb-4">
              오늘 운동을 시작해보세요!
            </p>
            <Button onClick={handleAddExercise}>
              <Plus className="w-4 h-4 mr-1" />
              첫 운동 추가하기
            </Button>
          </div>
        )}
      </Card>

      {/* 운동 등록/수정 모달 */}
      <ExerciseRegistrationModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        date={new Date()}
      />
    </>
  );
}
