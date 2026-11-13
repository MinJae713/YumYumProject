import { useState, useEffect } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./ui/alert-dialog";
import { Dumbbell, Flame, TrendingUp, Trash2 } from "lucide-react";

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

interface ExerciseRegistrationModalProps {
  isOpen: boolean;
  onClose: () => void;
  date: Date;
  existingExercise?: ExerciseRecord;
  onSave?: (data: ExerciseData) => void;
  onDelete?: () => void;
}

type ExerciseType = "strength" | "cardio" | "bodyweight" | "";

interface ExerciseData {
  type: ExerciseType;
  name: string;
  sets?: number;
  reps?: number;
  weight?: number;
  duration?: number;
}

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

export function ExerciseRegistrationModal({
  isOpen,
  onClose,
  date,
  existingExercise,
  onSave,
  onDelete,
}: ExerciseRegistrationModalProps) {
  const [exerciseData, setExerciseData] = useState<ExerciseData>({
    type: "",
    name: "",
  });
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);

  // 기존 운동 데이터를 폼에 로드
  useEffect(() => {
    if (existingExercise && isOpen) {
      setExerciseData({
        type: existingExercise.type,
        name: existingExercise.name,
        sets: existingExercise.sets,
        reps: existingExercise.reps,
        weight: existingExercise.weight,
        duration: existingExercise.duration,
      });
    } else if (!existingExercise && isOpen) {
      // 새 운동일 때는 폼 초기화
      resetForm();
    }
  }, [existingExercise, isOpen]);

  // 사용자 체중 (실제로는 프로필에서 가져옴)
  const userWeight = 70; // kg

  const updateField = (field: string, value: any) => {
    setExerciseData((prev) => ({ ...prev, [field]: value }));
  };

  const resetForm = () => {
    setExerciseData({
      type: "",
      name: "",
    });
  };

  // MET 기반 칼로리 계산
  const calculateCalories = (): number => {
    if (!exerciseData.name) return 0;

    const met = MET_VALUES[exerciseData.name] || MET_VALUES["기본"];
    
    if (exerciseData.type === "cardio" && exerciseData.duration) {
      // 유산소: MET × 체중(kg) × 시간(hour)
      return Math.round(met * userWeight * (exerciseData.duration / 60));
    } else if (
      (exerciseData.type === "strength" || exerciseData.type === "bodyweight") &&
      exerciseData.sets &&
      exerciseData.reps
    ) {
      // 근력/맨몸: 대략적인 소요 시간 추정 (1세트당 2분)
      const estimatedMinutes = exerciseData.sets * 2;
      return Math.round(met * userWeight * (estimatedMinutes / 60));
    }

    return 0;
  };

  // 점수 계산 (칼로리 기반)
  const calculatePoints = (): number => {
    const calories = calculateCalories();
    return Math.round(calories / 10);
  };

  const handleSave = () => {
    // 실제로는 여기서 API 호출하여 저장
    if (onSave) {
      onSave(exerciseData);
    }
    console.log(existingExercise ? "Updating exercise:" : "Saving exercise:", { date, ...exerciseData });
    resetForm();
    onClose();
  };

  const handleDelete = () => {
    if (onDelete) {
      onDelete();
    }
    setShowDeleteDialog(false);
    resetForm();
    onClose();
  };

  const calories = calculateCalories();
  const points = calculatePoints();

  return (
    <>
      <Dialog
        open={isOpen}
        onOpenChange={(open) => {
          if (!open) {
            resetForm();
            onClose();
          }
        }}
      >
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>
              {existingExercise ? "운동 수정" : "운동 기록"}
            </DialogTitle>
            <DialogDescription>
              {date.toLocaleDateString("ko-KR", {
                month: "long",
                day: "numeric",
                weekday: "long",
              })}
            </DialogDescription>
          </DialogHeader>

        <div className="space-y-6">
          {/* 운동 종류 선택 */}
          <div className="space-y-2">
            <Label>운동 종류 *</Label>
            <Select
              value={exerciseData.type}
              onValueChange={(value) => updateField("type", value)}
            >
              <SelectTrigger>
                <SelectValue placeholder="운동 종류를 선택하세요" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="strength">근력 운동</SelectItem>
                <SelectItem value="cardio">유산소 운동</SelectItem>
                <SelectItem value="bodyweight">맨몸 운동</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* 운동 이름 */}
          {exerciseData.type && (
            <div className="space-y-2">
              <Label>운동 이름 *</Label>
              <Select
                value={exerciseData.name}
                onValueChange={(value) => updateField("name", value)}
              >
                <SelectTrigger>
                  <SelectValue placeholder="운동을 선택하세요" />
                </SelectTrigger>
                <SelectContent>
                  {exerciseData.type === "strength" && (
                    <>
                      <SelectItem value="벤치프레스">벤치프레스</SelectItem>
                      <SelectItem value="스쿼트">스쿼트</SelectItem>
                      <SelectItem value="데드리프트">데드리프트</SelectItem>
                    </>
                  )}
                  {exerciseData.type === "cardio" && (
                    <>
                      <SelectItem value="달리기">달리기</SelectItem>
                      <SelectItem value="자전거">자전거</SelectItem>
                      <SelectItem value="수영">수영</SelectItem>
                    </>
                  )}
                  {exerciseData.type === "bodyweight" && (
                    <>
                      <SelectItem value="푸시업">푸시업</SelectItem>
                      <SelectItem value="풀업">풀업</SelectItem>
                      <SelectItem value="플랭크">플랭크</SelectItem>
                    </>
                  )}
                </SelectContent>
              </Select>
            </div>
          )}

          {/* 조건부 필드 - 근력 운동 */}
          {exerciseData.type === "strength" && (
            <div className="grid grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label>세트 수 *</Label>
                <Input
                  type="number"
                  placeholder="3"
                  min="1"
                  value={exerciseData.sets || ""}
                  onChange={(e) => updateField("sets", parseInt(e.target.value) || 0)}
                />
              </div>
              <div className="space-y-2">
                <Label>반복 횟수 *</Label>
                <Input
                  type="number"
                  placeholder="10"
                  min="1"
                  value={exerciseData.reps || ""}
                  onChange={(e) => updateField("reps", parseInt(e.target.value) || 0)}
                />
              </div>
              <div className="space-y-2">
                <Label>무게 (kg) *</Label>
                <Input
                  type="number"
                  placeholder="20"
                  min="0"
                  step="0.5"
                  value={exerciseData.weight || ""}
                  onChange={(e) =>
                    updateField("weight", parseFloat(e.target.value) || 0)
                  }
                />
              </div>
            </div>
          )}

          {/* 조건부 필드 - 유산소 운동 */}
          {exerciseData.type === "cardio" && (
            <div className="space-y-2">
              <Label>소요 시간 (분) *</Label>
              <Input
                type="number"
                placeholder="30"
                min="1"
                value={exerciseData.duration || ""}
                onChange={(e) =>
                  updateField("duration", parseInt(e.target.value) || 0)
                }
              />
            </div>
          )}

          {/* 조건부 필드 - 맨몸 운동 */}
          {exerciseData.type === "bodyweight" && (
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>세트 수 *</Label>
                <Input
                  type="number"
                  placeholder="3"
                  min="1"
                  value={exerciseData.sets || ""}
                  onChange={(e) => updateField("sets", parseInt(e.target.value) || 0)}
                />
              </div>
              <div className="space-y-2">
                <Label>반복 횟수 *</Label>
                <Input
                  type="number"
                  placeholder="10"
                  min="1"
                  value={exerciseData.reps || ""}
                  onChange={(e) => updateField("reps", parseInt(e.target.value) || 0)}
                />
              </div>
            </div>
          )}

          {/* 자동 계산 결과 */}
          {exerciseData.name && (
            <div className="space-y-3 pt-4 border-t">
              <Label>자동 계산 결과</Label>
              <div className="grid grid-cols-2 gap-3">
                <div className="bg-primary/10 rounded-lg p-4">
                  <div className="flex items-center gap-2 mb-2">
                    <Flame className="w-4 h-4 text-primary" />
                    <span className="text-xs text-muted-foreground">
                      소모 칼로리
                    </span>
                  </div>
                  <div className="text-2xl text-primary">{calories}</div>
                  <div className="text-xs text-muted-foreground">kcal</div>
                </div>
                <div className="bg-secondary/10 rounded-lg p-4">
                  <div className="flex items-center gap-2 mb-2">
                    <TrendingUp className="w-4 h-4 text-secondary" />
                    <span className="text-xs text-muted-foreground">
                      획득 점수
                    </span>
                  </div>
                  <div className="text-2xl text-secondary">{points}</div>
                  <div className="text-xs text-muted-foreground">points</div>
                </div>
              </div>
              <div className="text-xs text-muted-foreground bg-muted/50 rounded p-3">
                <p>
                  * MET(Metabolic Equivalent of Task) 기반으로 칼로리가 계산됩니다.
                </p>
                <p className="mt-1">
                  * 점수는 소모 칼로리 10kcal당 1점으로 계산됩니다.
                </p>
              </div>
            </div>
          )}

          {/* 버튼 */}
          <div className="flex gap-2 pt-4">
            {existingExercise && (
              <Button
                variant="destructive"
                onClick={() => setShowDeleteDialog(true)}
              >
                <Trash2 className="w-4 h-4 mr-2" />
                삭제
              </Button>
            )}
            <Button
              variant="outline"
              onClick={() => {
                resetForm();
                onClose();
              }}
              className="flex-1"
            >
              취소
            </Button>
            <Button
              onClick={handleSave}
              disabled={!exerciseData.type || !exerciseData.name}
              className="flex-1"
            >
              {existingExercise ? "수정하기" : "저장하기"}
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>

    <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>운동 기록 삭제</AlertDialogTitle>
          <AlertDialogDescription>
            정말로 이 운동 기록을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>취소</AlertDialogCancel>
          <AlertDialogAction 
            onClick={handleDelete} 
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            삭제
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </>
  );
}
