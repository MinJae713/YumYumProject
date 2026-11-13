<script setup>
import { ref } from "vue";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Mail, Search, UserPlus } from "lucide-vue-next";
import { useGroupStore } from "@/stores/group/GroupStore";
import { storeToRefs } from "pinia";

const props = defineProps({
  open: { type: Boolean, default: false },
  onOpenChange: { type: Function, default: undefined },
  groupName: { type: String, default: "" },
  groupId: { type: [String, Number], required: true },
});

const emit = defineEmits(["update:open"]);
const groupStore = useGroupStore();
const { searchResults } = storeToRefs(groupStore);

const searchEmail = ref("");
const selectedUser = ref(null);
const isSearching = ref(false);
const isInviting = ref(false);

const closeModal = () => {
  emit("update:open", false);
  props.onOpenChange?.(false);
};

const resetState = () => {
  searchEmail.value = "";
  groupStore.clearSearchResults();
  selectedUser.value = null;
  isSearching.value = false;
  isInviting.value = false;
};

const handleSearch = async () => {
  const keyword = searchEmail.value.trim();
  if (!keyword) {
    groupStore.clearSearchResults();
    selectedUser.value = null;
    return;
  }
  isSearching.value = true;
  try {
    await groupStore.searchUser(keyword);
    selectedUser.value = null;
  } catch (error) {
    console.error("유저 검색 실패:", error);
    alert(error?.response?.data?.message || "유저 검색에 실패했습니다.");
  } finally {
    isSearching.value = false;
  }
};

const handleSelectUser = (user) => {
  selectedUser.value = user;
};

const handleInvite = async () => {
  if (!selectedUser.value) return;
  isInviting.value = true;
  try {
    await groupStore.inviteMember(props.groupId, selectedUser.value.userId);
    alert("초대가 완료되었습니다.");
    resetState();
    closeModal();
  } catch (error) {
    console.error("초대 실패:", error);
    alert(error?.response?.data?.message || "초대에 실패했습니다.");
  } finally {
    isInviting.value = false;
  }
};
</script>

<template>
  <Dialog
    :open="open"
    @update:open="
      (val) => {
        emit('update:open', val);
        props.onOpenChange?.(val);
        if (!val) resetState();
      }
    "
  >
      <DialogContent class="sm:max-w-md rounded-3xl">
        <DialogHeader>
          <div class="flex items-center gap-2 mb-2">
            <div class="w-10 h-10 bg-emerald-50 text-emerald-600 rounded-full flex items-center justify-center">
              <UserPlus class="w-5 h-5" />
          </div>
          <DialogTitle>그룹원 초대하기</DialogTitle>
        </div>
        <p v-if="groupName" class="text-sm text-muted-foreground">
          <span class="font-medium text-foreground">{{ groupName }}</span>
          그룹에 새로운 그룹원을 초대합니다
        </p>
      </DialogHeader>

      <div class="space-y-4 py-4">
        <div class="space-y-2">
          <Label for="member-email">초대할 회원의 이메일</Label>
          <div class="relative">
            <Mail class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <Input
              id="member-email"
              type="email"
              placeholder="example@email.com"
              v-model="searchEmail"
              class="h-12 pl-10 pr-24 bg-gray-50 border-gray-200 rounded-xl focus-visible:ring-emerald-500 focus-visible:border-emerald-400"
              autofocus
              @keydown.enter.prevent="handleSearch"
            />
            <Button
              type="button"
              class="absolute right-2 top-2 h-8 px-3 rounded-lg bg-emerald-500 hover:bg-emerald-600 text-white"
              :disabled="isSearching"
              @click="handleSearch"
            >
              <Search class="w-4 h-4 mr-1" />
              검색
            </Button>
          </div>
          <p class="text-xs text-muted-foreground">이메일로 유저를 검색해 초대하세요.</p>
        </div>

        <div class="bg-gray-50 rounded-2xl p-3 max-h-60 overflow-y-auto">
          <div
            v-if="isSearching"
            class="p-4 text-sm text-muted-foreground text-center"
          >
            검색 중입니다...
          </div>
          <div
            v-else-if="searchResults.length === 0"
            class="p-4 text-sm text-muted-foreground text-center"
          >
            검색 결과가 없습니다.
          </div>
          <button
            v-for="user in searchResults"
            :key="user.userId"
            type="button"
            class="w-full text-left p-4 flex items-center gap-3 rounded-2xl border transition-all"
            :class="[
              selectedUser?.userId === user.userId
                ? 'bg-emerald-50 border-emerald-500 ring-1 ring-emerald-500'
                : 'bg-white border-gray-100 hover:border-emerald-200 hover:shadow-sm',
              user.alreadyInvited ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer',
            ]"
            :disabled="user.alreadyInvited"
            @click="() => !user.alreadyInvited && handleSelectUser(user)"
          >
            <Avatar class="w-10 h-10 mt-1 shrink-0">
              <AvatarFallback class="w-full h-full flex items-center justify-center font-bold text-gray-900">
                {{ user.name?.[0] || "?" }}
              </AvatarFallback>
            </Avatar>
            <div class="flex-1">
              <div class="font-medium text-sm">{{ user.name || "이름 없음" }}</div>
              <div class="text-xs text-muted-foreground">{{ user.email }}</div>
            </div>
            <span
              v-if="selectedUser?.userId === user.userId"
              class="text-emerald-600 text-xs font-medium"
            >
              선택됨
            </span>
            <span
              v-else-if="user.alreadyInvited"
              class="text-xs text-gray-400"
            >
              초대됨
            </span>
          </button>
        </div>
      </div>

      <DialogFooter class="flex gap-2 sm:gap-2">
        <Button variant="outline" class="flex-1" @click="() => { resetState(); closeModal(); }">
          취소
        </Button>
        <Button
          class="flex-1"
          :disabled="!selectedUser || isInviting"
          @click="handleInvite"
        >
          초대
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
