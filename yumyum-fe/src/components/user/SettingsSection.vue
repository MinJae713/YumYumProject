<script setup>
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Switch } from "@/components/ui/switch";
import { Bell, Settings2, ShieldCheck, Sparkles, User2 } from "lucide-vue-next";
import { Utensils } from "lucide-vue-next";
import { Target } from "lucide-vue-next";
import useMyPageView from "@/stores/user/MyPageView";
import { storeToRefs } from "pinia";
const store = useMyPageView();
const {
  settings,
  dialogOpen,
  profileDraft,
  stats,
  passwordDialogOpen,
  passwordForm,
} = storeToRefs(store);
const handleEdit = store.handleEdit;
const handleUpdateSettings = store.handleUpdateSettings;
const handleSaveProfile = store.handleSaveProfile;
const submitPasswordChange = store.submitPasswordChange;
const handleWithdrawalClick = store.handleWithdrawalClick;
</script>

<template>
  <div class="p-8 bg-white rounded-3xl border border-gray-200 shadow-sm space-y-6">
    <div class="flex items-center gap-3">
      <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center text-gray-600">
        <Settings2 class="w-5 h-5" />
      </div>
      <h3 class="text-lg font-semibold text-gray-900">설정</h3>
    </div>

    <div class="space-y-2">
      <div class="flex items-center justify-between py-4 border-b border-gray-100 last:border-0">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center text-gray-600">
            <User2 class="w-5 h-5" />
          </div>
          <div>
            <div class="text-sm font-medium text-gray-900">회원 정보</div>
            <div class="text-xs text-gray-500">닉네임, 이메일 변경</div>
          </div>
        </div>
        <Button variant="outline" size="sm" class="h-9 rounded-lg" @click="handleEdit">
          수정
        </Button>
      </div>

      <div class="flex items-center justify-between py-4 border-b border-gray-100">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center text-gray-600">
            <Bell class="w-5 h-5" />
          </div>
          <div>
            <div class="text-sm font-medium text-gray-900">알림 설정</div>
            <div class="text-xs text-gray-500">푸시 알림 수신</div>
          </div>
        </div>
        <Switch
          :checked="settings.notifications"
          class="bg-gray-300 data-[state=checked]:bg-emerald-500 data-[state=unchecked]:!bg-gray-300"
          @update:checked="(val) => handleUpdateSettings({ notifications: val })"
        />
      </div>

      <div class="flex items-center justify-between py-4 border-b border-gray-100">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center text-gray-600">
            <Target class="w-5 h-5" />
          </div>
        <div>
          <div class="text-sm font-medium text-gray-900">목표 체중</div>
          <div class="text-xs text-gray-500">목표하는 체중 설정 (kg)</div>
        </div>
      </div>
        <Input
          type="number"
          class="w-[120px] h-10 bg-white border-gray-200 focus:ring-emerald-500 rounded-lg text-right"
          :model-value="stats.targetWeight"
          @update:model-value="(val) => handleUpdateSettings({ targetWeight: val ? Number(val) : null })"
        />
      </div>

      <div class="flex items-center justify-between py-4 border-b border-gray-100">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center text-gray-600">
            <Utensils class="w-5 h-5" />
          </div>
          <div>
            <div class="text-sm font-medium text-gray-900">하루 식사 횟수</div>
            <div class="text-xs text-gray-500">하루 권장 식사 횟수 설정</div>
          </div>
        </div>
        <Select
          :model-value="stats.mealsPerDay?.toString()"
          @update:model-value="(val) => handleUpdateSettings({ mealsPerDay: Number(val) })"
        >
          <SelectTrigger class="w-[120px] h-10 bg-white border-gray-200 focus:ring-emerald-500 rounded-lg text-right">
            <SelectValue placeholder="횟수 선택" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="1">1회</SelectItem>
            <SelectItem value="2">2회</SelectItem>
            <SelectItem value="3">3회</SelectItem>
            <SelectItem value="4">4회</SelectItem>
            <SelectItem value="5">5회</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div class="flex items-center justify-between py-4 border-b border-gray-100">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center text-gray-600">
            <Sparkles class="w-5 h-5" />
          </div>
          <div>
            <div class="text-sm font-medium text-gray-900">리서치 리마인더</div>
            <div class="text-xs text-gray-500">하루 컨디션/스케줄 체크 알림</div>
          </div>
        </div>
        <div class="flex items-center gap-2">
          <Input
            type="time"
            class="w-[120px] h-10 bg-white border-gray-200 focus:ring-emerald-500 rounded-lg text-right"
            :model-value="settings.remindTime"
            @update:model-value="(val) => handleUpdateSettings({ remindTime: val })"
          />
          <Switch
            :checked="settings.researchReminder"
            class="bg-gray-300 data-[state=checked]:bg-emerald-500 data-[state=unchecked]:!bg-gray-300"
            @update:checked="(val) => handleUpdateSettings({ researchReminder: val })"
          />
        </div>
      </div>

      <div class="flex items-center justify-between py-4">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center text-gray-600">
            <ShieldCheck class="w-5 h-5" />
          </div>
          <div>
            <div class="text-sm font-medium text-gray-900">보안</div>
            <div class="text-xs text-gray-500">비밀번호 재설정, 세션 관리</div>
          </div>
        </div>
        <div class="flex gap-2">
          <Button variant="outline" size="sm" class="h-9 rounded-lg" @click="passwordDialogOpen = true">관리</Button>
          <Button
            variant="ghost"
            size="sm"
            class="h-9 rounded-lg text-red-500 hover:bg-red-50"
            @click="handleWithdrawalClick"
          >
            회원 탈퇴
          </Button>
        </div>
      </div>
    </div>
  </div>

  <Dialog v-model:open="dialogOpen">
    <DialogContent class="bg-white rounded-3xl border border-gray-200 shadow-lg">
      <DialogHeader>
        <DialogTitle class="text-lg font-semibold text-gray-900">회원 정보 수정</DialogTitle>
        <DialogDescription class="text-sm text-gray-500">닉네임과 이메일을 변경할 수 있습니다.</DialogDescription>
      </DialogHeader>

      <div class="space-y-4 py-2">
        <div class="space-y-2">
          <Label for="nickname" class="text-gray-700 font-medium">닉네임</Label>
          <Input id="nickname" v-model="profileDraft.name" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
        </div>
        <div class="space-y-2">
          <Label for="email" class="text-gray-700 font-medium">이메일</Label>
          <Input id="email" v-model="profileDraft.email" type="email" disabled readonly class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium">성별</Label>
            <Select v-model="profileDraft.gender">
              <SelectTrigger class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl">
                <SelectValue placeholder="성별을 선택하세요" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="MALE">남자</SelectItem>
                <SelectItem value="FEMALE">여자</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium">생년월일</Label>
            <Input
              type="date"
              v-model="profileDraft.birthDate"
              class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl"
            />
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium">키 (cm)</Label>
            <Input
              type="number"
              step="0.1"
              v-model.number="profileDraft.height"
              class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl"
            />
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium">목표 유형</Label>
            <Select v-model="profileDraft.goalType">
              <SelectTrigger class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl">
                <SelectValue placeholder="목표를 선택하세요" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="lose">다이어트</SelectItem>
                <SelectItem value="maintain">유지</SelectItem>
                <SelectItem value="gain">벌크업</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium">현재 체중 (kg)</Label>
            <Input
              type="number"
              step="0.1"
              :model-value="profileDraft.currentWeight"
              readonly
              disabled
              class="h-12 bg-gray-100 border-gray-200 rounded-xl text-gray-500 cursor-not-allowed"
            />
          </div>
        </div>
      </div>

      <DialogFooter class="flex gap-2">
        <Button variant="outline" class="flex-1 h-12 rounded-xl hover:bg-gray-100" @click="dialogOpen = false">취소</Button>
        <Button class="flex-1 h-12 rounded-xl bg-emerald-500 hover:bg-emerald-600 text-white font-bold shadow-md shadow-emerald-200" @click="handleSaveProfile">저장</Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>

  <Dialog v-model:open="passwordDialogOpen">
    <DialogContent class="bg-white rounded-3xl border border-gray-200 shadow-lg">
      <DialogHeader>
        <DialogTitle class="text-lg font-semibold text-gray-900">비밀번호 변경</DialogTitle>
        <DialogDescription class="text-sm text-gray-500">현재 비밀번호와 새 비밀번호를 입력하세요.</DialogDescription>
      </DialogHeader>

      <div class="space-y-3 py-2">
        <div class="space-y-2">
          <Label for="currentPassword" class="text-gray-700 font-medium">현재 비밀번호</Label>
          <Input
            id="currentPassword"
            type="password"
            v-model="passwordForm.current"
            class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl"
          />
        </div>
        <div class="space-y-2">
          <Label for="newPassword" class="text-gray-700 font-medium">새 비밀번호</Label>
          <Input
            id="newPassword"
            type="password"
            v-model="passwordForm.new"
            class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl"
          />
        </div>
        <div class="space-y-2">
          <Label for="confirmPassword" class="text-gray-700 font-medium">새 비밀번호 확인</Label>
          <Input
            id="confirmPassword"
            type="password"
            v-model="passwordForm.confirm"
            class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl"
          />
        </div>
      </div>

      <DialogFooter class="flex gap-2">
        <Button variant="outline" class="flex-1 h-12 rounded-xl hover:bg-gray-100" @click="passwordDialogOpen = false">취소</Button>
        <Button class="flex-1 h-12 rounded-xl bg-emerald-500 hover:bg-emerald-600 text-white font-bold shadow-md shadow-emerald-200" @click="submitPasswordChange">변경하기</Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
