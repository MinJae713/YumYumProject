<script setup>
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card } from "@/components/ui/card";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Progress } from "@/components/ui/progress";
import { storeToRefs } from "pinia";
import useOnboardingViewStore from "@/stores/auth/OnboardingView";
import { onMounted, onUnmounted } from "vue";

const store = useOnboardingViewStore();
const { currentStep, totalSteps, surveyData, progressPercentage, isDirty } = storeToRefs(store);
const { handleNext, handleBack } = store;

const handleBeforeUnload = (e) => {
  if (!isDirty.value) return;
  e.preventDefault();
  e.returnValue = ""; // ⚠️ 반드시 필요
};

onMounted(() => {
  window.addEventListener("beforeunload", handleBeforeUnload);
});

onUnmounted(() => {
  window.removeEventListener("beforeunload", handleBeforeUnload);
});
</script>

<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <div class="w-full max-w-3xl">
      <div class="mb-8">
        <div class="flex items-center justify-between mb-3">
          <div>
            <h2 class="text-2xl font-bold text-gray-900">초기 설정</h2>
            <p class="text-sm text-gray-500">맞춤형 식단 관리를 위한 정보를 입력해주세요</p>
          </div>
          <div class="text-sm text-gray-500">{{ currentStep }} / {{ totalSteps }}</div>
        </div>
        <Progress :value="progressPercentage" class="h-2 [&>*]:bg-emerald-500" />
      </div>

      <Card class="p-8 bg-white rounded-3xl shadow-xl border border-gray-100">
        <template v-if="currentStep === 1">
          <div class="space-y-6">
            <div>
              <h3 class="mb-4 text-lg font-semibold text-gray-900">기본 신체 정보</h3>
              <p class="text-sm text-gray-500 mb-6">정확한 건강 관리를 위해 기본 정보를 입력해주세요</p>
            </div>

            <div class="space-y-4">
              <div>
                <Label class="mb-3 block text-gray-700 font-medium">성별</Label>
                <RadioGroup v-model="surveyData.gender" class="grid grid-cols-2 gap-4">
                  <Label
                    for="male"
                    :class="[
                      'flex items-center justify-center border-2 rounded-2xl p-4 cursor-pointer transition-all',
                      surveyData.gender === 'male' ? 'border-emerald-500 bg-emerald-50 text-emerald-700 ring-1 ring-emerald-500' : 'border-gray-200 hover:border-emerald-200 hover:bg-gray-50',
                    ]"
                  >
                    <RadioGroupItem value="male" id="male" class="sr-only" />
                    <span>남성</span>
                  </Label>
                  <Label
                    for="female"
                    :class="[
                      'flex items-center justify-center border-2 rounded-2xl p-4 cursor-pointer transition-all',
                      surveyData.gender === 'female' ? 'border-emerald-500 bg-emerald-50 text-emerald-700 ring-1 ring-emerald-500' : 'border-gray-200 hover:border-emerald-200 hover:bg-gray-50',
                    ]"
                  >
                    <RadioGroupItem value="female" id="female" class="sr-only" />
                    <span>여성</span>
                  </Label>
                </RadioGroup>
              </div>

              <div class="grid md:grid-cols-2 gap-4">
                <div class="space-y-2">
                  <Label for="birthDate" class="text-gray-700 font-medium mb-1.5">생년월일</Label>
                  <Input id="birthDate" type="date" v-model="surveyData.birthDate" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl" />
                </div>

                <div class="space-y-2">
                  <Label for="height" class="text-gray-700 font-medium mb-1.5">키</Label>
                  <div class="relative">
                    <Input id="height" type="number" placeholder="170" v-model="surveyData.height" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl" />
                    <span class="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-gray-500">cm</span>
                  </div>
                </div>
              </div>

              <div class="space-y-2">
                <Label for="currentWeight" class="text-gray-700 font-medium mb-1.5">현재 체중</Label>
                <div class="relative">
                  <Input id="currentWeight" type="number" placeholder="70" v-model="surveyData.currentWeight" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl" />
                  <span class="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-gray-500">kg</span>
                </div>
              </div>
            </div>
          </div>
        </template>

        <template v-else-if="currentStep === 2">
          <div class="space-y-6">
            <div>
              <h3 class="mb-4">목표 및 습관</h3>
              <p class="text-sm text-muted-foreground mb-6">식단 목표와 알림을 설정해주세요</p>
            </div>

            <div class="space-y-4">
              <div>
                <Label class="mb-3 block text-gray-700 font-medium">목표</Label>
                <RadioGroup v-model="surveyData.goalType" class="grid gap-3">
                  <Label
                    v-for="option in [
                      { value: 'lose', label: '다이어트 (감량)', icon: '📉' },
                      { value: 'maintain', label: '체중 유지', icon: '⚖️' },
                      { value: 'gain', label: '벌크업 (증량)', icon: '📈' },
                    ]"
                    :key="option.value"
                    :for="option.value"
                    :class="[
                      'flex items-center space-x-3 border-2 rounded-2xl p-4 cursor-pointer transition-all',
                      surveyData.goalType === option.value ? 'border-emerald-500 bg-emerald-50 text-emerald-700 ring-1 ring-emerald-500' : 'border-gray-200 hover:border-emerald-200 hover:bg-gray-50',
                    ]"
                  >
                    <RadioGroupItem :value="option.value" :id="option.value" class="sr-only" />
                    <span class="text-2xl">{{ option.icon }}</span>
                    <span class="flex-1">{{ option.label }}</span>
                  </Label>
                </RadioGroup>
              </div>

              <div class="space-y-2">
                <Label for="targetWeight" class="text-gray-700 font-medium mb-1.5">목표 체중</Label>
                <div class="relative">
                  <Input id="targetWeight" type="number" placeholder="65" v-model="surveyData.targetWeight" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl" />
                  <span class="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-gray-500">kg</span>
                </div>
              </div>

              <div>
                <Label class="mb-3 block text-gray-700 font-medium">하루 식사 횟수</Label>
                <RadioGroup v-model="surveyData.mealsPerDay" class="grid grid-cols-4 gap-3">
                  <Label
                    v-for="num in ['1', '2', '3', '4']"
                    :key="num"
                    :for="`meal-${num}`"
                    :class="[
                      'flex items-center justify-center border-2 rounded-2xl p-3 cursor-pointer transition-all',
                      surveyData.mealsPerDay === num ? 'border-emerald-500 bg-emerald-50 text-emerald-700 ring-1 ring-emerald-500' : 'border-gray-200 hover:border-emerald-200 hover:bg-gray-50',
                    ]"
                  >
                    <RadioGroupItem :value="num" :id="`meal-${num}`" class="sr-only" />
                    <span>{{ num }}회</span>
                  </Label>
                </RadioGroup>
              </div>

              <div class="space-y-2">
                <Label for="remindTime" class="text-gray-700 font-medium mb-1.5">식단 알림 시간</Label>
                <Input id="remindTime" type="time" v-model="surveyData.remindTime" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl" />
              </div>
            </div>
          </div>
        </template>

        <div class="flex gap-3 pt-6 mt-6 border-t border-gray-200">
          <Button v-if="currentStep > 1" type="button" variant="outline" class="flex-1 h-12 rounded-xl hover:bg-gray-100" @click="handleBack">이전</Button>
          <!-- 수정된 부분: 백슬래시 제거 -->
          <Button type="button" :class="currentStep === 1 ? 'w-full h-12 rounded-xl' : 'flex-1 h-12 rounded-xl'" class="bg-emerald-500 hover:bg-emerald-600 text-white font-bold shadow-md shadow-emerald-200" @click="handleNext">
            {{ currentStep === totalSteps ? "완료" : "다음" }}
          </Button>
        </div>
      </Card>

      <div class="mt-6 flex justify-center gap-2">
        <div
          v-for="(_, index) in Array.from({ length: totalSteps })"
          :key="index"
          :class="[
            'h-2 rounded-full transition-all',
            index + 1 === currentStep ? 'w-8 bg-emerald-500' : index + 1 < currentStep ? 'w-2 bg-emerald-500' : 'w-2 bg-border',
          ]"
        />
      </div>
    </div>
  </div>
</template>