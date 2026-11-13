import { Activity, Dumbbell, Guitar, Heart } from "lucide-vue-next";

export const getExerciseIcon = (type) => {
  switch (type) {
    case "strength":
      return Dumbbell;
    case "cardio":
      return Activity;
    case "bodyweight":
      return Heart;
    case "etc":
      return Guitar;
    default:
      return Dumbbell;
  }
};

export const getExerciseColor = (type) => {
  switch (type) {
    case "strength":
      return "text-secondary";
    case "cardio":
      return "text-primary";
    case "bodyweight":
      return "text-accent";
    case "etc":
      return "text-destructive";
    default:
      return "text-muted-foreground";
  }
};

export const getExerciseLabel = (type) => {
  switch (type) {
    case "strength":
      return "근력";
    case "cardio":
      return "유산소";
    case "bodyweight":
      return "맨몸";
    case "etc":
      return "기타";
    default:
      return "";
  }
};

export const getExerciseKey = (label) => {
  switch (label) {
    case "근력 운동":
      return "strength";
    case "유산소 운동":
      return "cardio";
    case "맨몸 운동":
      return "bodyweight";
    case "기타":
      return "etc";
    default:
      return "";
  }
}