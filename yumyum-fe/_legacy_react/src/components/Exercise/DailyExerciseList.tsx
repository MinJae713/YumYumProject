import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Plus, Dumbbell, Activity, Heart, Edit } from "lucide-react";
import { ExerciseRegistrationModal } from "./ExerciseRegistrationModal";

interface ExerciseRecord {
  id: string;
  type: "strength" | "cardio" | "bodyweight";
  name: string;
  sets?: number;
  reps?: number;
  weight?: number;
  duration?: number;
  calories: number;
  points: number;
}

interface DailyExerciseListProps {
  selectedDate: Date;
}

export function DailyExerciseList({ selectedDate }: DailyExerciseListProps) {
  const [modalState, setModalState] = useState<{
    isOpen: boolean;
    exercise: ExerciseRecord | undefined;
  }>({
    isOpen: false,
    exercise: undefined,
  });

  // 실제로는 selectedDate에 따라 API에서 데이터를 가져옴
  const [exercises, setExercises] = useState<ExerciseRecord[]>([
    {
      id: "1",
      type: "strength",
      name: "벤치프레스",
      sets: 4,
      reps: 10,
      weight: 60,
      calories: 180,
      points: 18,
    },
    {
      id: "2",
      type: "cardio",
      name: "달리기",
      duration: 30,
      calories: 336,
      points: 34,
    },
    {
      id: "3",
      type: "bodyweight",
      name: "푸시업",
      sets: 3,
      reps: 15,
      calories: 95,
      points: 10,
    },
  ]);

  const openModal = (exercise?: ExerciseRecord) => {
    setModalState({ isOpen: true, exercise });
  };

  const closeModal = () => {
    setModalState({ isOpen: false, exercise: undefined });
  };

  // MET 값 (운동별 대사당량)
  const MET_VALUES: Record<string, number> = {
    "벤치프레스": 6.0,
    "스쿼트": 5.0,
    "데드리프트": 6.0,
    "달리기": 8.0,
    "자전거": 7.0,
    "수영": 8.0,
    "푸시업": 3.8,
    "풀업": 8.0,
    "플랭크": 4.0,
    "기본": 5.0,
  };

  const userWeight = 70; // kg (실제로는 프로필에서 가져옴)

  const calculateCalories = (data: any): number => {
    const met = MET_VALUES[data.name] || MET_VALUES["기본"];
    
    if (data.type === "cardio" && data.duration) {
      return Math.round(met * userWeight * (data.duration / 60));
    } else if (
      (data.type === "strength" || data.type === "bodyweight") &&
      data.sets &&
      data.reps
    ) {
      const estimatedMinutes = data.sets * 2;
      return Math.round(met * userWeight * (estimatedMinutes / 60));
    }
    return 0;
  };

  const handleSave = (data: any) => {
    const calories = calculateCalories(data);
    const points = Math.round(calories / 10);

    if (modalState.exercise) {
      // 수정
      setExercises(exercises.map(ex => 
        ex.id === modalState.exercise!.id 
          ? { ...ex, ...data, calories, points }
          : ex
      ));
      console.log("Updating exercise:", modalState.exercise.id);
    } else {
      // 새로 추가
      const newExercise: ExerciseRecord = {
        id: Date.now().toString(),
        type: data.type,
        name: data.name,
        sets: data.sets,
        reps: data.reps,
        weight: data.weight,
        duration: data.duration,
        calories,
        points,
      };
      setExercises([...exercises, newExercise]);
      console.log("Adding new exercise:", newExercise);
    }
    closeModal();
  };

  const handleDelete = () => {
    if (modalState.exercise) {
      setExercises(exercises.filter(ex => ex.id !== modalState.exercise!.id));
      console.log("Deleting exercise:", modalState.exercise.id);
    }
    closeModal();
  };

  const totalCalories = exercises.reduce((sum, ex) => sum + ex.calories, 0);
  const totalPoints = exercises.reduce((sum, ex) => sum + ex.points, 0);

  const getExerciseIcon = (type: string) => {
    switch (type) {
      case "strength":
        return Dumbbell;
      case "cardio":
        return Activity;
      case "bodyweight":
        return Heart;
      default:
        return Dumbbell;
    }
  };

  const getExerciseColor = (type: string) => {
    switch (type) {
      case "strength":
        return "text-secondary";
      case "cardio":
        return "text-primary";
      case "bodyweight":
        return "text-accent";
      default:
        return "text-secondary";
    }
  };

  const getExerciseLabel = (type: string) => {
    switch (type) {
      case "strength":
        return "근력";
      case "cardio":
        return "유산소";
      case "bodyweight":
        return "맨몸";
      default:
        return "";
    }
  };

  return (
    <>
      <Card className="p-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h3 className="mb-1">운동 기록</h3>
            <p className="text-xs text-muted-foreground">
              {selectedDate.toLocaleDateString("ko-KR", {
                month: "long",
                day: "numeric",
                weekday: "long",
              })}
            </p>
          </div>
          <div className="flex gap-3">
            <div className="text-right">
              <div className="text-xs text-muted-foreground mb-1">소모 칼로리</div>
              <div className="text-2xl text-primary">{totalCalories}</div>
              <div className="text-xs text-muted-foreground">kcal</div>
            </div>
            <div className="text-right">
              <div className="text-xs text-muted-foreground mb-1">획득 점수</div>
              <div className="text-2xl text-secondary">{totalPoints}</div>
              <div className="text-xs text-muted-foreground">points</div>
            </div>
          </div>
        </div>

        {exercises.length > 0 ? (
          <div className="space-y-3 mb-4 max-h-[600px] overflow-y-auto pr-2">
            {exercises.map((exercise) => {
              const Icon = getExerciseIcon(exercise.type);
              const colorClass = getExerciseColor(exercise.type);

              return (
                <Card key={exercise.id} className="p-4 bg-muted/20">
                  <div className="flex items-start gap-3">
                    <div
                      className={`w-12 h-12 rounded-lg bg-card border border-border flex items-center justify-center ${colorClass} flex-shrink-0`}
                    >
                      <Icon className="w-6 h-6" />
                    </div>

                    <div className="flex-1 min-w-0">
                      <div className="flex items-center justify-between mb-2">
                        <div className="flex items-center gap-2">
                          <h4 className="text-sm">{exercise.name}</h4>
                          <Badge variant="outline" className="text-xs">
                            {getExerciseLabel(exercise.type)}
                          </Badge>
                        </div>
                        <div className="flex items-center gap-2">
                          <Badge variant="secondary" className="text-xs">
                            {exercise.calories} kcal
                          </Badge>
                          <Badge variant="outline" className="text-xs">
                            {exercise.points}P
                          </Badge>
                        </div>
                      </div>

                      <div className="text-xs text-muted-foreground mb-3">
                        {exercise.type === "strength" && (
                          <span>
                            {exercise.sets}세트 × {exercise.reps}회 × {exercise.weight}
                            kg
                          </span>
                        )}
                        {exercise.type === "cardio" && (
                          <span>{exercise.duration}분</span>
                        )}
                        {exercise.type === "bodyweight" && (
                          <span>
                            {exercise.sets}세트 × {exercise.reps}회
                          </span>
                        )}
                      </div>

                      <Button 
                        size="sm" 
                        variant="outline" 
                        className="w-full"
                        onClick={() => openModal(exercise)}
                      >
                        <Edit className="w-3 h-3 mr-1" />
                        수정하기
                      </Button>
                    </div>
                  </div>
                </Card>
              );
            })}
          </div>
        ) : (
          <div className="flex flex-col items-center py-12">
            <div className="w-16 h-16 rounded-full bg-muted flex items-center justify-center mb-3">
              <Dumbbell className="w-8 h-8 text-muted-foreground" />
            </div>
            <p className="text-sm text-muted-foreground mb-4">
              등록된 운동이 없습니다
            </p>
          </div>
        )}

        <Button onClick={() => openModal()} className="w-full">
          <Plus className="w-4 h-4 mr-2" />
          운동 추가
        </Button>
      </Card>

      <ExerciseRegistrationModal
        isOpen={modalState.isOpen}
        onClose={closeModal}
        date={selectedDate}
        existingExercise={modalState.exercise}
        onSave={handleSave}
        onDelete={handleDelete}
      />
    </>
  );
}
