<script setup>
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card } from "@/components/ui/card";
import { storeToRefs } from "pinia";
import useLoginViewStore from "@/stores/auth/LoginView";

const store = useLoginViewStore();
const { email, password, isLoading } = storeToRefs(store);
const { handleLogin, goSignup } = store;
</script>

<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <Card class="w-full max-w-md p-8 bg-white rounded-3xl shadow-xl border border-gray-100">
      <div class="text-center mb-8">
        <h1 class="text-2xl font-bold text-gray-900">
          <span class="text-gray-500">Fit</span>
          <span class="text-green-500">log</span>
        </h1>
        <p class="text-sm text-gray-500 mt-1">건강한 식단 관리를 시작하세요</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-4">
        <div class="space-y-2">
          <Label htmlFor="email" class="text-gray-700 font-medium">이메일</Label>
          <Input
            id="email"
            type="email"
            placeholder="example@email.com"
            v-model="email" 
            required
            class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl"
          />
        </div>

        <div class="space-y-2">
          <Label htmlFor="password" class="text-gray-700 font-medium">비밀번호</Label>
          <Input
            id="password"
            type="password"
            placeholder="••••••••"
            v-model="password"
            required
            class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 focus:border-emerald-500 rounded-xl"
          />
        </div>

        <Button
          type="submit"
          class="w-full h-12 rounded-xl font-bold bg-emerald-500 hover:bg-emerald-600 text-white shadow-md shadow-emerald-200"
          :disabled="isLoading"
        >
          {{ isLoading ? '로그인 중...' : '로그인' }}
        </Button>
      </form>
      
      <div class="mt-6 text-center text-sm">
          <span class="text-gray-500">계정이 없으신가요? </span>
          <button
            type="button"
            class="text-emerald-600 font-bold hover:underline"
            @click="goSignup"
          >
            회원가입
          </button>
        </div>
    </Card>
  </div>
</template>
