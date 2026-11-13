<script setup>
import {
  Award,
  BarChart3,
  Dumbbell,
  Home,
  LogOut,
  ShieldAlert,
  User,
  Users,
  Utensils,
} from "lucide-vue-next";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import logoIcon from "@/assets/logo.svg";

const emit = defineEmits({
  navigate: (page) => typeof page === "string",
});

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const navItems = [
  { id: "main", label: "메인 페이지", icon: Home, to: "/" },
  { id: "dashboard", label: "대시보드", icon: BarChart3, to: "/dashboard" },
  { id: "meals", label: "식단", icon: Utensils, to: "/meals" },
  { id: "exercise", label: "운동", icon: Dumbbell, to: "/exercise" },
  { id: "groups", label: "그룹", icon: Users, to: "/group" },
  { id: "badges", label: "뱃지", icon: Award, to: "/badges" },
  { id: "mypage", label: "마이페이지", icon: User, to: "/mypage" },
];

const isActive = (item) => route.path === item.to;

const handleNavigate = (item) => {
  emit("navigate", item.id);
};

const handleLogoClick = () => {
  const dashboard = navItems.find((item) => item.id === "main");
  if (dashboard?.to) {
    handleNavigate(dashboard);
    router.push(dashboard.to);
  }
};
</script>

<template>
  <aside
    class="w-64 h-full flex flex-col bg-white border-r border-gray-200 z-50"
  >
    <div
      class="h-20 flex items-center px-6 cursor-pointer gap-3 group"
      @click="handleLogoClick"
    >
      <img :src="logoIcon" alt="Fitlog Logo" class="w-9 h-9 object-contain hover:scale-105 transition-transform" />
      <div class="text-2xl font-bold tracking-tight">
        <span class="text-gray-900">Fit</span>
        <span class="text-green-600">log</span>
      </div>
    </div>

    <div class="flex-1 overflow-y-auto py-4">
      <nav class="flex flex-col space-y-1">
        <RouterLink
          v-for="item in navItems"
          :key="item.id"
          :to="item.to"
          class="w-full flex items-center gap-3 px-4 py-3 text-sm font-medium text-gray-500 hover:bg-gray-50 hover:text-gray-900 transition-all rounded-r-full mr-2"
          :class="{
            'bg-green-50 text-green-700': isActive(item),
          }"
          @click="handleNavigate(item)"
        >
          <component :is="item.icon" class="w-5 h-5" />
          <span class="flex-1">{{ item.label }}</span>
          <span
            v-if="isActive(item)"
            class="w-1.5 h-1.5 bg-green-600 rounded-full"
          />
        </RouterLink>
      </nav>
    </div>

    <div class="border-t border-gray-100 p-4 flex flex-col gap-2">
      <RouterLink
        v-if="authStore.user?.role === 'ADMIN'"
        to="/admin/food"
        class="w-full flex items-center gap-3 px-4 py-3 text-sm font-medium text-gray-600 hover:bg-gray-50 hover:text-gray-900 transition-all rounded-r-full mr-2"
      >
        <ShieldAlert class="w-5 h-5" />
        <span class="flex-1">관리자 페이지</span>
      </RouterLink>
      <button
        type="button"
        class="w-full flex items-center gap-3 px-4 py-3 text-sm font-medium text-gray-600 hover:bg-gray-50 hover:text-gray-900 transition-all rounded-r-full mr-2 text-left"
        @click="authStore.logout"
      >
        <LogOut class="w-5 h-5" />
        <span class="flex-1">로그아웃</span>
      </button>
    </div>
  </aside>
</template>
