<script setup>
import Navigation from "@/components/common/Navigation.vue";
import { RouterView, useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useGroupStore } from "@/stores/group/GroupStore";
import InviteDropdown from "@/components/group/InviteDropdown.vue";
import { Bell, User } from "lucide-vue-next";
import { computed, onMounted, ref } from "vue";
import { storeToRefs } from "pinia";

const authStore = useAuthStore();
const groupStore = useGroupStore();
const route = useRoute();
const { invites } = storeToRefs(groupStore);
const isInviteOpen = ref(false);

const pageMap = {
  "/": "메인 페이지",
  "/dashboard": "대시보드",
  "/meals": "식단 관리",
  "/exercise": "운동 기록",
  "/group": "그룹",
  "/badges": "뱃지",
  "/mypage": "마이페이지",
  "/admin/food": "관리자 페이지",
};

const pageTitle = computed(() => pageMap[route.path] || "Fitlog");

onMounted(() => {
  groupStore.fetchInvites();
});

const toggleInvite = () => {
  isInviteOpen.value = !isInviteOpen.value;
};

const closeInvite = () => {
  isInviteOpen.value = false;
};
</script>

<template>
  <div class="flex h-screen bg-gray-50 overflow-hidden">
    <Navigation />

    <main class="flex-1 flex flex-col h-full overflow-hidden relative">
      <header
        class="h-16 bg-white/80 backdrop-blur-sm border-b border-gray-200 flex items-center justify-between px-8"
      >
        <div>
          <p class="text-xs text-gray-500">오늘도 건강하게!</p>
          <h1 class="text-xl font-semibold text-gray-900">{{ pageTitle }}</h1>
        </div>

        <div class="flex items-center gap-4">
          <div class="relative" @click.outside="closeInvite">
            <button
              type="button"
              class="relative p-2 rounded-full hover:bg-gray-100 transition-colors text-gray-500 focus:outline-none"
              @click.stop="toggleInvite"
            >
              <Bell class="w-5 h-5" />
              <span
                v-if="(invites?.length || 0) > 0"
                class="absolute top-2 right-2 block w-2 h-2 rounded-full bg-red-500"
              />
            </button>
            <div
              v-if="isInviteOpen"
              class="absolute right-0 top-full mt-2 z-50"
              @click.stop
            >
              <InviteDropdown />
            </div>
          </div>
          <div class="flex items-center gap-2">
            <div
              class="w-9 h-9 rounded-full bg-gray-200 flex items-center justify-center text-gray-600"
              v-if="!authStore.user"
            >
              <User class="w-5 h-5" />
            </div>
            <div
              v-else
              class="w-9 h-9 rounded-full bg-green-100 text-green-700 flex items-center justify-center font-semibold"
            >
              {{ authStore.user.name?.[0] || "U" }}
            </div>
            <div class="flex flex-col leading-tight">
              <span class="text-sm font-medium text-gray-900">
                {{ authStore.user?.name || "Guest" }}
              </span>
              <span class="text-xs text-gray-500">
                {{ authStore.user?.email || "Welcome back" }}
              </span>
            </div>
          </div>
        </div>
      </header>

      <div class="flex-1 overflow-y-auto p-8">
        <RouterView />
      </div>
    </main>
  </div>
</template>
