<script setup>
import { onMounted } from "vue";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Calendar, Lock, Award, Star, Trophy } from "lucide-vue-next";
import useBadgeView from "@/stores/badges/BadgesView";
import { storeToRefs } from "pinia";
const store = useBadgeView();
const {
  fullCount,
  achievedCount,
  achievedRate,
  selectedCategory,
  filteredBadges
} = storeToRefs(store);

onMounted(() => {
  store.setBadgeViewData();
});
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-6 space-y-6">
    <div class="flex flex-col gap-1">
      <h1 class="text-2xl font-bold text-gray-900">뱃지 컬렉션</h1>
      <p class="text-sm text-gray-500">목표를 달성하고 뱃지를 모아보세요</p>
    </div>

    <div class="grid md:grid-cols-4 gap-4">
      <Card class="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-blue-50 text-blue-600 flex items-center justify-center">
            <Award class="w-5 h-5" />
          </div>
          <div>
            <div class="text-xs text-gray-500">총 뱃지</div>
            <div class="text-2xl font-bold text-gray-900">{{ fullCount }}</div>
          </div>
        </div>
      </Card>

      <Card class="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-emerald-50 text-emerald-600 flex items-center justify-center">
            <Trophy class="w-5 h-5" />
          </div>
          <div>
            <div class="text-xs text-gray-500">획득한 뱃지</div>
            <div class="text-2xl font-bold text-gray-900">{{ achievedCount }}</div>
          </div>
        </div>
      </Card>

      <Card class="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gray-100 text-gray-500 flex items-center justify-center">
            <Lock class="w-5 h-5" />
          </div>
          <div>
            <div class="text-xs text-gray-500">미획득 뱃지</div>
            <div class="text-2xl font-bold text-gray-900">{{ fullCount - achievedCount }}</div>
          </div>
        </div>
      </Card>

      <Card class="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-orange-50 text-orange-600 flex items-center justify-center">
            <Star class="w-5 h-5" />
          </div>
          <div>
            <div class="text-xs text-gray-500">달성률</div>
            <div class="text-2xl font-bold text-gray-900">
              {{ achievedRate }}%
            </div>
          </div>
        </div>
      </Card>
    </div>

    <Tabs v-model="selectedCategory" class="w-full">
      <TabsList class="grid w-full grid-cols-4 mb-6 bg-gray-100 p-1 rounded-xl">
        <TabsTrigger
          value="all"
          class="data-[state=active]:bg-white data-[state=active]:shadow-sm data-[state=active]:text-gray-900 rounded-lg"
        >전체</TabsTrigger>
        <TabsTrigger
          value="achievement"
          class="data-[state=active]:bg-white data-[state=active]:shadow-sm data-[state=active]:text-gray-900 rounded-lg"
        >업적</TabsTrigger>
        <TabsTrigger
          value="streak"
          class="data-[state=active]:bg-white data-[state=active]:shadow-sm data-[state=active]:text-gray-900 rounded-lg"
        >연속 기록</TabsTrigger>
        <TabsTrigger
          value="social"
          class="data-[state=active]:bg-white data-[state=active]:shadow-sm data-[state=active]:text-gray-900 rounded-lg"
        >소셜</TabsTrigger>
      </TabsList>

      <TabsContent :value="selectedCategory" class="space-y-6">
        <div v-if="filteredBadges.filter((b) => b.achieved).length">
          <h3 class="mb-4 text-lg font-semibold text-gray-900">획득한 뱃지</h3>
          <div class="grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            <Card
              v-for="badge in filteredBadges.filter((b) => b.achieved)"
              :key="badge.id"
              class="p-6 text-center bg-white border border-gray-200 rounded-2xl flex flex-col items-center hover:border-emerald-300 hover:shadow-md transition-all cursor-pointer"
            >
              <div class="w-20 h-20 mx-auto mb-4 rounded-full bg-gray-50 flex items-center justify-center relative">
                <img :src="badge.url" alt="" class="w-20 h-20 object-contain" />
              </div>
              <h4 class="text-sm font-semibold text-gray-900 mb-2">{{ badge.name }}</h4>
              <p class="text-xs text-gray-500 mb-3">{{ badge.description }}</p>
              <Badge variant="secondary" class="text-xs bg-gray-100 text-gray-600 border-none">
                <Calendar class="w-3 h-3 mr-1" />
                {{ badge.achievedDate }}
              </Badge>
            </Card>
          </div>
        </div>

        <div v-if="filteredBadges.filter((b) => !b.achieved).length">
          <h3 class="mb-4 text-lg font-semibold text-gray-900">미획득 뱃지</h3>
          <div class="grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            <Card
              v-for="badge in filteredBadges.filter((b) => !b.achieved)"
              :key="badge.id"
              class="p-6 text-center bg-gray-50 border border-gray-100 rounded-2xl opacity-80"
            >
              <div class="w-20 h-20 mx-auto mb-4 rounded-full bg-gray-100 flex items-center justify-center relative">
                <img :src="badge.url" alt="" class="w-20 h-20 object-contain opacity-70" />
                <div class="absolute inset-0 flex items-center justify-center bg-black/40 rounded-full">
                  <Lock class="w-7 h-7 text-white" />
                </div>
              </div>
              <h4 class="text-sm font-semibold text-gray-700 mb-2">{{ badge.name }}</h4>
              <p class="text-xs text-gray-500 mb-3">{{ badge.description }}</p>
            </Card>
          </div>
        </div>

        <div v-if="!filteredBadges.length" class="text-center py-12">
          <Trophy class="w-16 h-16 mx-auto mb-4 text-gray-300" />
          <h4 class="mb-2 text-gray-800">뱃지가 없습니다</h4>
          <p class="text-sm text-gray-500">선택한 카테고리에 뱃지가 없습니다</p>
        </div>
      </TabsContent>
    </Tabs>
  </div>
</template>
