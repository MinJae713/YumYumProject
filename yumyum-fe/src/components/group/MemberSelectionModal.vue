<script setup>
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { ChevronRight, Users } from "lucide-vue-next";
import { formatGoalLabel, getGoalColorClass } from "@/utils/groupUtils";

const props = defineProps({
  open: { type: Boolean, default: false },
  onOpenChange: { type: Function, default: undefined },
  members: { type: Array, default: () => [] },
});

const emit = defineEmits(["select-member", "update:open"]);

const handleSelect = (member) => {
  emit("select-member", member);
  emit("update:open", false);
  props.onOpenChange?.(false);
};
</script>

<template>
  <Dialog :open="open" @update:open="(val) => emit('update:open', val)">
    <DialogContent class="sm:max-w-2xl max-h-[80vh] rounded-3xl">
      <DialogHeader>
        <div class="flex items-center gap-2 mb-2">
          <div class="w-10 h-10 bg-emerald-50 text-emerald-600 rounded-full flex items-center justify-center">
            <Users class="w-5 h-5" />
          </div>
          <DialogTitle>식단을 입력할 그룹원을 선택하세요</DialogTitle>
        </div>
        <p class="text-sm text-muted-foreground">
          선택한 그룹원의 추천 식단 목록에 추가됩니다
        </p>
      </DialogHeader>

      <div class="bg-gray-50 rounded-2xl p-3 overflow-y-auto max-h-[500px] pr-1 space-y-2">
        <button
          v-for="member in members"
          :key="member.id"
          type="button"
          class="w-full text-left bg-white p-4 rounded-2xl border border-gray-100 transition-all flex items-center justify-between gap-3 hover:border-emerald-200 hover:shadow-sm"
          @click="handleSelect(member)"
        >
          <div class="flex items-center gap-3 flex-1">
            <Avatar class="w-12 h-12">
              <AvatarFallback class="bg-emerald-100 text-emerald-700 font-bold">
                {{ member.name?.[0] }}
              </AvatarFallback>
            </Avatar>

            <div class="flex-1">
              <div class="flex items-center gap-2 mb-1">
                <span class="font-medium text-gray-900">{{ member.name }}</span>
                <Badge variant="secondary" class="text-xs" :class="getGoalColorClass(member.goal)">
                  {{ formatGoalLabel(member.goal) }}
                </Badge>
              </div>
              <div class="flex items-center gap-3 text-xs text-gray-500">
                <span>현재 {{ member.currentWeight }}kg</span>
                <span>•</span>
                <span>목표 {{ member.targetWeight }}kg</span>
                <span>•</span>
                <span>{{ member.consecutiveDays }}일 연속</span>
              </div>
            </div>
          </div>

          <ChevronRight class="w-5 h-5 text-emerald-500" />
        </button>
      </div>

      <div v-if="members.length === 0" class="py-12 text-center">
        <Users class="w-12 h-12 text-muted-foreground mx-auto mb-3" />
        <p class="text-sm text-muted-foreground">아직 그룹원이 없습니다</p>
      </div>
    </DialogContent>
  </Dialog>
</template>
