<script setup>
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card } from "@/components/ui/card";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { storeToRefs } from "pinia";
import useSignupViewStore from "@/stores/auth/SignupView";
import { onMounted, onUnmounted } from "vue";

const store = useSignupViewStore();
const { formData, isDirty } = storeToRefs(store);
const { handleSubmit, goLogin } = store;

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
    <Card class="w-full max-w-2xl p-8 bg-white rounded-3xl shadow-xl border border-gray-100">
      <div class="text-center mb-8">
        <h1 class="text-2xl font-bold text-gray-900">
          <span class="text-emerald-500">Fit</span>log
        </h1>
        <h2 class="mb-2 text-xl font-semibold text-gray-900">회원가입</h2>
        <p class="text-sm text-gray-500">건강한 식단 관리의 첫 걸음을 시작하세요</p>
      </div>

      <form class="space-y-6" @submit.prevent="handleSubmit">
        <div class="space-y-3">
          <Label class="text-gray-700 font-medium mb-1.5">회원 유형 선택</Label>
          <RadioGroup v-model="formData.userType" class="grid grid-cols-2 gap-4">
            <Label
              for="general"
              :class="[
                'flex items-center space-x-3 border-2 rounded-2xl p-4 cursor-pointer transition-all',
                formData.userType === 'general'
                  ? 'border-emerald-500 bg-emerald-50 text-emerald-700 ring-1 ring-emerald-500'
                  : 'border-gray-200 hover:border-emerald-200 hover:bg-gray-50',
              ]"
            >
              <RadioGroupItem value="general" id="general" class="sr-only" />
              <div class="flex-1">
                <div class="font-medium">일반 회원</div>
                <div class="text-sm text-gray-500">개인 식단 관리를 원하는 사용자</div>
              </div>
            </Label>

            <Label
              for="business"
              :class="[
                'flex items-center space-x-3 border-2 rounded-2xl p-4 cursor-pointer transition-all',
                formData.userType === 'business'
                  ? 'border-emerald-500 bg-emerald-50 text-emerald-700 ring-1 ring-emerald-500'
                  : 'border-gray-200 hover:border-emerald-200 hover:bg-gray-50',
              ]"
            >
              <RadioGroupItem value="business" id="business" class="sr-only" />
              <div class="flex-1">
                <div class="font-medium">비즈니스 회원</div>
                <div class="text-sm text-gray-500">트레이너, 영양사 등 전문가</div>
              </div>
            </Label>
          </RadioGroup>
        </div>

        <div class="grid md:grid-cols-2 gap-4">
          <div class="space-y-2">
            <Label for="name" class="text-gray-700 font-medium mb-1.5">이름</Label>
            <Input id="name" v-model="formData.name" type="text" placeholder="홍길동" required class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl" />
          </div>

          <div class="space-y-2">
            <Label for="signup-email" class="text-gray-700 font-medium mb-1.5">이메일</Label>
            <Input
              id="signup-email"
              v-model="formData.email"
              type="email"
              placeholder="example@email.com"
              required
              class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl"
            />
          </div>
        </div>

        <div class="grid md:grid-cols-2 gap-4">
          <div class="space-y-2">
            <Label for="signup-password" class="text-gray-700 font-medium mb-1.5">비밀번호</Label>
            <Input
              id="signup-password"
              v-model="formData.password"
              type="password"
              placeholder="8자 이상 입력"
              required
              minlength="8"
              class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl"
            />
          </div>

          <div class="space-y-2">
            <Label for="confirm-password" class="text-gray-700 font-medium mb-1.5">비밀번호 확인</Label>
            <Input
              id="confirm-password"
              v-model="formData.confirmPassword"
              type="password"
              placeholder="비밀번호 재입력"
              required
              minlength="8"
              class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl"
            />
          </div>
        </div>

        <div class="flex gap-3 pt-4">
          <Button type="button" variant="outline" class="flex-1 h-12 rounded-xl hover:bg-gray-100" @click="goLogin">취소</Button>
          <Button type="submit" class="flex-1 h-12 rounded-xl font-bold bg-emerald-500 hover:bg-emerald-600 text-white shadow-md shadow-emerald-200">다음 단계로</Button>
        </div>
      </form>

      <div class="mt-6 text-center text-sm">
        <span class="text-gray-500">이미 계정이 있으신가요? </span>
        <button type="button" class="text-emerald-600 font-bold hover:underline" @click="goLogin">
          로그인
        </button>
      </div>
    </Card>
  </div>
</template>
