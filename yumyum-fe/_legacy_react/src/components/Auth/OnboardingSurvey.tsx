import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Card } from "./ui/card";
import { RadioGroup, RadioGroupItem } from "./ui/radio-group";
import { Progress } from "./ui/progress";
import { Checkbox } from "./ui/checkbox";

interface OnboardingSurveyProps {
  onNavigate: (page: string) => void;
}

export function OnboardingSurvey({ onNavigate }: OnboardingSurveyProps) {
  const [currentStep, setCurrentStep] = useState(1);
  const totalSteps = 4;

  // 설문 데이터
  const [surveyData, setSurveyData] = useState({
    // Step 1: 신체 정보
    gender: "",
    age: "",
    height: "",
    weight: "",
    
    // Step 2: 식단 목적
    goal: "",
    targetWeight: "",
    
    // Step 3: 생활 습관
    sleepHours: "",
    waterIntake: "",
    mealFrequency: "",
    
    // Step 4: 운동 습관
    exerciseFrequency: "",
    exerciseTypes: [] as string[],
    exerciseIntensity: "",
  });

  const progressPercentage = (currentStep / totalSteps) * 100;

  const handleNext = () => {
    if (currentStep < totalSteps) {
      setCurrentStep(currentStep + 1);
    } else {
      // 온보딩 완료
      console.log("온보딩 완료:", surveyData);
      onNavigate("dashboard");
    }
  };

  const handleBack = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const toggleExerciseType = (type: string) => {
    setSurveyData({
      ...surveyData,
      exerciseTypes: surveyData.exerciseTypes.includes(type)
        ? surveyData.exerciseTypes.filter((t) => t !== type)
        : [...surveyData.exerciseTypes, type],
    });
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="w-full max-w-3xl">
        {/* 프로그레스 헤더 */}
        <div className="mb-8">
          <div className="flex items-center justify-between mb-3">
            <div>
              <h2>초기 설정</h2>
              <p className="text-sm text-muted-foreground">
                맞춤형 식단 관리를 위한 정보를 입력해주세요
              </p>
            </div>
            <div className="text-sm text-muted-foreground">
              {currentStep} / {totalSteps}
            </div>
          </div>
          <Progress value={progressPercentage} className="h-2" />
        </div>

        <Card className="p-8">
          {/* Step 1: 신체 정보 */}
          {currentStep === 1 && (
            <div className="space-y-6">
              <div>
                <h3 className="mb-4">신체 정보</h3>
                <p className="text-sm text-muted-foreground mb-6">
                  정확한 건강 관리를 위해 기본 신체 정보를 입력해주세요
                </p>
              </div>

              <div className="space-y-4">
                <div>
                  <Label className="mb-3 block">성별</Label>
                  <RadioGroup
                    value={surveyData.gender}
                    onValueChange={(value) =>
                      setSurveyData({ ...surveyData, gender: value })
                    }
                    className="grid grid-cols-2 gap-4"
                  >
                    <Label
                      htmlFor="male"
                      className={`flex items-center justify-center border-2 rounded-lg p-4 cursor-pointer transition-all ${
                        surveyData.gender === "male"
                          ? "border-primary bg-primary/5"
                          : "border-border hover:border-primary/50"
                      }`}
                    >
                      <RadioGroupItem value="male" id="male" className="sr-only" />
                      <span>남성</span>
                    </Label>
                    <Label
                      htmlFor="female"
                      className={`flex items-center justify-center border-2 rounded-lg p-4 cursor-pointer transition-all ${
                        surveyData.gender === "female"
                          ? "border-primary bg-primary/5"
                          : "border-border hover:border-primary/50"
                      }`}
                    >
                      <RadioGroupItem value="female" id="female" className="sr-only" />
                      <span>여성</span>
                    </Label>
                  </RadioGroup>
                </div>

                <div className="grid md:grid-cols-3 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="age">나이</Label>
                    <div className="relative">
                      <Input
                        id="age"
                        type="number"
                        placeholder="30"
                        value={surveyData.age}
                        onChange={(e) =>
                          setSurveyData({ ...surveyData, age: e.target.value })
                        }
                      />
                      <span className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                        세
                      </span>
                    </div>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="height">키</Label>
                    <div className="relative">
                      <Input
                        id="height"
                        type="number"
                        placeholder="170"
                        value={surveyData.height}
                        onChange={(e) =>
                          setSurveyData({ ...surveyData, height: e.target.value })
                        }
                      />
                      <span className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                        cm
                      </span>
                    </div>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="weight">체중</Label>
                    <div className="relative">
                      <Input
                        id="weight"
                        type="number"
                        placeholder="70"
                        value={surveyData.weight}
                        onChange={(e) =>
                          setSurveyData({ ...surveyData, weight: e.target.value })
                        }
                      />
                      <span className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                        kg
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Step 2: 식단 목적 */}
          {currentStep === 2 && (
            <div className="space-y-6">
              <div>
                <h3 className="mb-4">식단 목적</h3>
                <p className="text-sm text-muted-foreground mb-6">
                  당신의 건강 목표를 선택해주세요
                </p>
              </div>

              <div className="space-y-4">
                <div>
                  <Label className="mb-3 block">목표</Label>
                  <RadioGroup
                    value={surveyData.goal}
                    onValueChange={(value) =>
                      setSurveyData({ ...surveyData, goal: value })
                    }
                    className="grid gap-3"
                  >
                    {[
                      { value: "diet", label: "다이어트 (체중 감량)", icon: "📉" },
                      { value: "maintain", label: "체중 유지 (현상 유지)", icon: "⚖️" },
                      { value: "bulk", label: "벌크업 (체중 증가)", icon: "📈" },
                      { value: "health", label: "건강 관리 (영양 균형)", icon: "🥗" },
                    ].map((option) => (
                      <Label
                        key={option.value}
                        htmlFor={option.value}
                        className={`flex items-center space-x-3 border-2 rounded-lg p-4 cursor-pointer transition-all ${
                          surveyData.goal === option.value
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/50"
                        }`}
                      >
                        <RadioGroupItem
                          value={option.value}
                          id={option.value}
                          className="sr-only"
                        />
                        <span className="text-2xl">{option.icon}</span>
                        <span className="flex-1">{option.label}</span>
                      </Label>
                    ))}
                  </RadioGroup>
                </div>

                {surveyData.goal && surveyData.goal !== "maintain" && (
                  <div className="space-y-2">
                    <Label htmlFor="targetWeight">목표 체중</Label>
                    <div className="relative">
                      <Input
                        id="targetWeight"
                        type="number"
                        placeholder="65"
                        value={surveyData.targetWeight}
                        onChange={(e) =>
                          setSurveyData({
                            ...surveyData,
                            targetWeight: e.target.value,
                          })
                        }
                      />
                      <span className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                        kg
                      </span>
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Step 3: 생활 습관 */}
          {currentStep === 3 && (
            <div className="space-y-6">
              <div>
                <h3 className="mb-4">생활 습관</h3>
                <p className="text-sm text-muted-foreground mb-6">
                  일상 생활 패턴을 알려주세요
                </p>
              </div>

              <div className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="sleepHours">평균 수면 시간</Label>
                  <div className="relative">
                    <Input
                      id="sleepHours"
                      type="number"
                      placeholder="7"
                      value={surveyData.sleepHours}
                      onChange={(e) =>
                        setSurveyData({ ...surveyData, sleepHours: e.target.value })
                      }
                    />
                    <span className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                      시간
                    </span>
                  </div>
                </div>

                <div>
                  <Label className="mb-3 block">하루 물 섭취량</Label>
                  <RadioGroup
                    value={surveyData.waterIntake}
                    onValueChange={(value) =>
                      setSurveyData({ ...surveyData, waterIntake: value })
                    }
                    className="grid grid-cols-2 gap-3"
                  >
                    {[
                      { value: "low", label: "1L 미만" },
                      { value: "medium", label: "1-2L" },
                      { value: "high", label: "2L 이상" },
                      { value: "veryHigh", label: "3L 이상" },
                    ].map((option) => (
                      <Label
                        key={option.value}
                        htmlFor={`water-${option.value}`}
                        className={`flex items-center justify-center border-2 rounded-lg p-3 cursor-pointer transition-all ${
                          surveyData.waterIntake === option.value
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/50"
                        }`}
                      >
                        <RadioGroupItem
                          value={option.value}
                          id={`water-${option.value}`}
                          className="sr-only"
                        />
                        <span className="text-sm">{option.label}</span>
                      </Label>
                    ))}
                  </RadioGroup>
                </div>

                <div>
                  <Label className="mb-3 block">하루 식사 횟수</Label>
                  <RadioGroup
                    value={surveyData.mealFrequency}
                    onValueChange={(value) =>
                      setSurveyData({ ...surveyData, mealFrequency: value })
                    }
                    className="grid grid-cols-4 gap-3"
                  >
                    {["1", "2", "3", "4+"].map((num) => (
                      <Label
                        key={num}
                        htmlFor={`meal-${num}`}
                        className={`flex items-center justify-center border-2 rounded-lg p-3 cursor-pointer transition-all ${
                          surveyData.mealFrequency === num
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/50"
                        }`}
                      >
                        <RadioGroupItem
                          value={num}
                          id={`meal-${num}`}
                          className="sr-only"
                        />
                        <span>{num}회</span>
                      </Label>
                    ))}
                  </RadioGroup>
                </div>
              </div>
            </div>
          )}

          {/* Step 4: 운동 습관 */}
          {currentStep === 4 && (
            <div className="space-y-6">
              <div>
                <h3 className="mb-4">운동 습관</h3>
                <p className="text-sm text-muted-foreground mb-6">
                  운동 루틴에 대해 알려주세요
                </p>
              </div>

              <div className="space-y-4">
                <div>
                  <Label className="mb-3 block">주간 운동 빈도</Label>
                  <RadioGroup
                    value={surveyData.exerciseFrequency}
                    onValueChange={(value) =>
                      setSurveyData({ ...surveyData, exerciseFrequency: value })
                    }
                    className="grid grid-cols-2 gap-3"
                  >
                    {[
                      { value: "none", label: "운동 안함" },
                      { value: "1-2", label: "주 1-2회" },
                      { value: "3-4", label: "주 3-4회" },
                      { value: "5+", label: "주 5회 이상" },
                    ].map((option) => (
                      <Label
                        key={option.value}
                        htmlFor={`freq-${option.value}`}
                        className={`flex items-center justify-center border-2 rounded-lg p-3 cursor-pointer transition-all ${
                          surveyData.exerciseFrequency === option.value
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/50"
                        }`}
                      >
                        <RadioGroupItem
                          value={option.value}
                          id={`freq-${option.value}`}
                          className="sr-only"
                        />
                        <span className="text-sm">{option.label}</span>
                      </Label>
                    ))}
                  </RadioGroup>
                </div>

                <div>
                  <Label className="mb-3 block">운동 종류 (중복 선택 가능)</Label>
                  <div className="grid grid-cols-2 gap-3">
                    {[
                      { value: "cardio", label: "유산소 운동", icon: "🏃" },
                      { value: "strength", label: "근력 운동", icon: "💪" },
                      { value: "yoga", label: "요가/필라테스", icon: "🧘" },
                      { value: "sports", label: "구기 종목", icon: "⚽" },
                      { value: "swimming", label: "수영", icon: "🏊" },
                      { value: "cycling", label: "자전거", icon: "🚴" },
                    ].map((option) => (
                      <Label
                        key={option.value}
                        htmlFor={`type-${option.value}`}
                        className={`flex items-center space-x-3 border-2 rounded-lg p-3 cursor-pointer transition-all ${
                          surveyData.exerciseTypes.includes(option.value)
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/50"
                        }`}
                      >
                        <Checkbox
                          id={`type-${option.value}`}
                          checked={surveyData.exerciseTypes.includes(option.value)}
                          onCheckedChange={() => toggleExerciseType(option.value)}
                        />
                        <span className="text-lg">{option.icon}</span>
                        <span className="text-sm flex-1">{option.label}</span>
                      </Label>
                    ))}
                  </div>
                </div>

                <div>
                  <Label className="mb-3 block">운동 강도</Label>
                  <RadioGroup
                    value={surveyData.exerciseIntensity}
                    onValueChange={(value) =>
                      setSurveyData({ ...surveyData, exerciseIntensity: value })
                    }
                    className="grid gap-3"
                  >
                    {[
                      { value: "light", label: "가벼운 운동 (땀이 거의 안남)" },
                      { value: "moderate", label: "중간 강도 (약간 땀남)" },
                      { value: "intense", label: "고강도 (땀이 많이 남)" },
                    ].map((option) => (
                      <Label
                        key={option.value}
                        htmlFor={`intensity-${option.value}`}
                        className={`flex items-center space-x-3 border-2 rounded-lg p-4 cursor-pointer transition-all ${
                          surveyData.exerciseIntensity === option.value
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/50"
                        }`}
                      >
                        <RadioGroupItem
                          value={option.value}
                          id={`intensity-${option.value}`}
                          className="sr-only"
                        />
                        <span className="flex-1">{option.label}</span>
                      </Label>
                    ))}
                  </RadioGroup>
                </div>
              </div>
            </div>
          )}

          {/* 네비게이션 버튼 */}
          <div className="flex gap-3 pt-6 mt-6 border-t border-border">
            {currentStep > 1 && (
              <Button
                type="button"
                variant="outline"
                onClick={handleBack}
                className="flex-1"
              >
                이전
              </Button>
            )}
            <Button
              type="button"
              onClick={handleNext}
              className={currentStep === 1 ? "w-full" : "flex-1"}
            >
              {currentStep === totalSteps ? "완료" : "다음"}
            </Button>
          </div>
        </Card>

        {/* 단계 인디케이터 */}
        <div className="mt-6 flex justify-center gap-2">
          {Array.from({ length: totalSteps }).map((_, index) => (
            <div
              key={index}
              className={`h-2 rounded-full transition-all ${
                index + 1 === currentStep
                  ? "w-8 bg-primary"
                  : index + 1 < currentStep
                  ? "w-2 bg-primary"
                  : "w-2 bg-border"
              }`}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
