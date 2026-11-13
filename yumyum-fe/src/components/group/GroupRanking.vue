<script setup>
import { computed } from "vue";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Flame, Trophy } from "lucide-vue-next";

const props = defineProps({
  members: {
    type: Array,
    default: () => [],
  },
  showMyRank: {
    type: Boolean,
    default: true,
  },
});

const rankedMembers = computed(() =>
  [...props.members].sort((a, b) => (b.totalPoints || 0) - (a.totalPoints || 0)),
);

const myRankInfo = computed(() => {
  if (!props.showMyRank) return null;
  const index = rankedMembers.value.findIndex((m) => m.isMe);
  if (index < 0) return null;
  const me = rankedMembers.value[index];
  return {
    rank: index + 1,
    points: me?.totalPoints || 0,
  };
});
</script>

<template>
  <div class="bg-white rounded-3xl p-6 border border-gray-200 shadow-sm">
    <div class="flex items-center justify-between mb-6">
      <div class="flex items-center gap-2">
        <Trophy class="w-5 h-5 text-emerald-500" />
        <h3 class="text-lg font-bold text-gray-900">그룹 랭킹</h3>
      </div>
      <Badge variant="outline" class="text-xs border-emerald-200 text-emerald-700 bg-emerald-50">실시간</Badge>
    </div>

    <div class="flex flex-col gap-2">
      <div
        v-for="(member, index) in rankedMembers"
        :key="member.id || index"
        class="bg-white rounded-2xl p-4 border transition-all flex justify-between items-center"
        :class="[
          member.isMe
            ? 'bg-emerald-50 border-emerald-200 ring-1 ring-emerald-500/20'
            : 'border-gray-100'
        ]"
      >
        <div class="flex items-center gap-4 min-w-0">
          <div
            class="w-8 h-8 rounded-full flex items-center justify-center font-bold text-sm shadow-sm"
            :class="[
              index === 0 ? 'bg-yellow-400 text-white' :
              index === 1 ? 'bg-gray-300 text-white' :
              index === 2 ? 'bg-orange-400 text-white' : 'bg-gray-100 text-gray-500'
            ]"
          >
            {{ index + 1 }}
          </div>
          <Avatar class="w-10 h-10">
            <AvatarFallback class="w-full h-full flex items-center justify-center bg-blue-200 text-gray-900 font-bold text-xs">
              {{ member.name?.[0] }}
            </AvatarFallback>
          </Avatar>

          <div class="min-w-0">
            <div class="flex items-center gap-2">
              <span class="font-bold text-gray-900 text-sm truncate">{{ member.name }}</span>
              <span v-if="member.isMe" class="text-[10px] text-emerald-600 bg-emerald-100 px-1.5 py-0.5 rounded-full">Me</span>
            </div>
            <div class="flex items-center gap-2 text-xs text-gray-500 mt-0.5">
              <Flame class="w-3 h-3 text-orange-500" />
              <span>{{ member.consecutiveDays }}일 연속</span>
            </div>
          </div>
        </div>

        <div class="text-right">
          <span class="block font-bold text-emerald-600">{{ member.totalPoints }} P</span>
        </div>
      </div>
    </div>

    <div v-if="myRankInfo" class="mt-6">
      <div class="bg-emerald-50 rounded-2xl p-4 border border-emerald-200 text-gray-900 shadow-sm flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-white border border-emerald-200 flex items-center justify-center">
            <span class="font-bold text-lg text-emerald-600">
              {{ myRankInfo.rank }}
            </span>
          </div>
          <div>
            <div class="font-bold text-sm text-gray-900">나의 현재 순위</div>
            <div class="text-xs text-gray-500">
              총 {{ myRankInfo.points }}P 획득
            </div>
          </div>
        </div>
        <div class="font-bold text-lg text-emerald-600">
          {{ myRankInfo.points }} P
        </div>
      </div>
    </div>
  </div>
</template>
