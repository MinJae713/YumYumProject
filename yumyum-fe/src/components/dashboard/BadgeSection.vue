<script setup>
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Award } from "lucide-vue-next";
import useBadgeSection from "@/stores/dashboard/BadgeSection";
import { storeToRefs } from "pinia";

const store = useBadgeSection();
const { achievedBadges } = storeToRefs(store);
</script>

<template>
  <div class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6">
    <div class="flex items-center justify-between mb-6">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
          <Award class="w-5 h-5 text-emerald-600" />
        </div>
        <div>
          <h3 class="text-lg font-bold text-gray-900">획득한 뱃지</h3>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
      <div
        v-for="(badge, index) in achievedBadges"
        :key="badge.id"
        class="group relative bg-gray-50 border border-gray-100 rounded-2xl p-4 hover:border-emerald-200 hover:shadow-md transition-all cursor-pointer"
      >
        <div class="flex flex-col items-center text-center space-y-2">
          <div class="w-16 h-16 rounded-full bg-white shadow-sm flex items-center justify-center p-2">
            <img :src="badge.badgeImageUrl" class="w-full h-full object-contain">
          </div>
          <div>
            <h4 class="text-sm font-semibold text-gray-900 mb-1">{{ badge.name }}</h4>
            <p class="text-xs text-gray-500 line-clamp-2">
              {{ badge.description }}
            </p>
          </div>
          <Badge variant="outline" class="text-xs border-emerald-100 text-emerald-600 bg-emerald-50">
            {{ badge.earnedDate }}
          </Badge>
        </div>

        <div v-if="index == 0" class="absolute -top-2 -right-2">
          <Badge class="bg-red-500 text-white border-none text-[10px] hover:bg-red-600">NEW</Badge>
        </div>
      </div>
      
      <div v-if="achievedBadges.length === 0" class="col-span-full py-8 text-center border-2 border-dashed border-gray-200 rounded-2xl">
        <span class="text-sm text-gray-400">획득한 뱃지가 없습니다.</span>
      </div>
    </div>
  </div>
</template>