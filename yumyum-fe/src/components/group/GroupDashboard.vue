<script setup>
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Calendar, Dumbbell, Flame, Users } from "lucide-vue-next";
import { formatGoalLabel, getWeightChangeIcon } from "@/utils/groupUtils";

const props = defineProps({
  members: { type: Array, default: () => [] },
});

const emit = defineEmits(["select-member"]);
</script>

<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-2">
        <Users class="w-5 h-5 text-emerald-500" />
        <h3 class="text-lg font-semibold text-gray-900">전체 멤버</h3>
        <Badge variant="outline" class="text-xs">{{ members.length }}명</Badge>
      </div>
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
      <div
        v-for="(member, idx) in members"
        :key="member.id || idx"
        class="bg-white rounded-2xl p-6 border border-gray-200 shadow-sm hover:border-emerald-400 hover:shadow-md transition-all cursor-pointer relative overflow-hidden group min-h-[230px]"
        @click="emit('select-member', member)"
      >
        <div v-if="member.hasNewActivity" class="absolute top-3 right-3 w-2.5 h-2.5">
          <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
          <span class="relative inline-flex rounded-full h-2.5 w-2.5 bg-red-500"></span>
        </div>

        <div class="flex items-center gap-4 mb-4">
          <Avatar class="w-12 h-12 border border-gray-100">
            <AvatarFallback class="w-full h-full flex items-center justify-center bg-emerald-100 text-gray-900 font-bold text-lg">
              {{ member.name?.[0] }}
            </AvatarFallback>
          </Avatar>
          <div class="min-w-0">
            <div class="flex items-center gap-3">
              <span class="font-semibold text-gray-900 truncate text-sm">{{ member.name }}</span>
              <Badge variant="secondary" class="text-[11px] bg-gray-100 text-gray-600 border-0">
                {{ formatGoalLabel(member.goal) }}
              </Badge>
            </div>
            <div class="text-sm text-gray-500 mt-1 flex items-center gap-2">
              <Calendar class="w-4 h-4" />
              <span>최근: {{ member.lastActivityDate || "-" }}</span>
            </div>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-3 text-sm bg-gray-50 p-4 rounded-xl border border-gray-100">
          <div class="flex items-center gap-2 text-gray-600">
            <Flame class="w-4 h-4 text-orange-500" />
            <span class="font-medium">{{ member.consecutiveDays }}일</span>
          </div>
          <div class="flex items-center gap-2 text-gray-600">
            <Dumbbell class="w-4 h-4 text-blue-500" />
            <span class="font-medium">{{ member.totalPoints }} P</span>
          </div>
          <div class="flex items-center gap-2 text-gray-600">
            <component :is="getWeightChangeIcon(member.currentWeight, member.startWeight)" class="w-4 h-4 text-gray-400" />
            <span class="font-medium">{{ member.currentWeight }}kg</span>
          </div>
           <div class="flex items-center gap-2 text-gray-600">
            <span class="w-4 text-center">🏆</span>
            <span class="font-medium">{{ member.exerciseCount }}회</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
