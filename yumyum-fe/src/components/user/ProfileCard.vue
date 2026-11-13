<script setup>
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Mail, User2 } from "lucide-vue-next";
import useMyPageView from "@/stores/user/MyPageView";
import { storeToRefs } from "pinia";

const store = useMyPageView();
const { user, userRoleBadge, summary } = storeToRefs(store);
const handleLogout = store.handleLogout;
const handleEditProfile = store.handleEditProfile;
</script>

<template>
  <Card class="p-8 flex flex-col gap-6 bg-white rounded-3xl border border-gray-200 shadow-sm">
    <div class="flex items-center gap-5">
      <Avatar class="w-24 h-24">
        <AvatarFallback class="w-full h-full flex items-center justify-center bg-blue-200 text-gray-900 text-3xl font-bold">
          {{ user.name?.[0] || "U" }}
        </AvatarFallback>
      </Avatar>
      <div class="flex-1 min-w-0">
        <div class="flex items-center gap-2 flex-wrap">
          <h2 class="text-2xl font-bold text-gray-900 truncate">{{ user.name }}</h2>
          <Badge variant="secondary" class="text-xs bg-emerald-50 text-emerald-700 border-emerald-100">
            {{ userRoleBadge }}
          </Badge>
        </div>
        <div class="flex items-center gap-2 text-sm text-gray-500 mt-2 truncate">
          <Mail class="w-4 h-4" />
          <span class="truncate">{{ user.email }}</span>
        </div>
      </div>
    </div>

    <div class="text-sm text-gray-700 bg-gray-50 rounded-2xl p-4 flex items-center gap-2">
      <Badge variant="outline" class="bg-emerald-50 text-emerald-700 border-emerald-100">건강 정보</Badge>
      <span class="truncate">{{ summary }}</span>
    </div>

    <div class="flex gap-3">
      <Button class="flex-1 bg-emerald-500 hover:bg-emerald-600 text-white" @click="handleEditProfile">
        <User2 class="w-4 h-4 mr-2" />
        프로필 수정
      </Button>
      <Button variant="ghost" class="flex-1 text-gray-500 hover:text-red-500" @click="handleLogout">
        로그아웃
      </Button>
    </div>
  </Card>
</template>
