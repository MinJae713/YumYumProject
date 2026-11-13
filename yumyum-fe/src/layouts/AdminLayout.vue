<script setup>
import { RouterLink, RouterView, useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();

const isFoodActive = computed(() => route.path.startsWith("/admin/food"));
const isDietActive = computed(() => route.path.startsWith("/admin/diet"));
</script>

<template>
  <div class="flex h-screen">
    <aside class="w-64 bg-slate-900 text-white flex flex-col">
      <div class="p-6 text-lg font-semibold border-b border-slate-800">
        관리자 페이지
      </div>
      <nav class="flex-1 p-4 space-y-2">
        <RouterLink
          to="/admin/food"
          class="block px-4 py-3 rounded-md transition-colors"
          :class="isFoodActive ? 'bg-slate-800 text-white' : 'text-slate-200 hover:bg-slate-800'"
        >
          음식 매칭 관리
        </RouterLink>
        <RouterLink
          to="/admin/diet"
          class="block px-4 py-3 rounded-md transition-colors"
          :class="isDietActive ? 'bg-slate-800 text-white' : 'text-slate-200 hover:bg-slate-800'"
        >
          추천 식단 조회
        </RouterLink>
      </nav>
    </aside>

    <main class="flex-1 bg-slate-50 overflow-y-auto">
      <div class="p-6">
        <RouterView />
      </div>
    </main>
  </div>
</template>
