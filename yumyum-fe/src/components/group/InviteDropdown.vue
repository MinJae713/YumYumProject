<script setup>
import { onMounted, ref } from "vue";
import { useGroupStore } from "@/stores/group/GroupStore";
import { storeToRefs } from "pinia";
import { Check, X, Users } from "lucide-vue-next";

const groupStore = useGroupStore();
const { invites } = storeToRefs(groupStore);

const isLoading = ref(false);
const isResponding = ref(false);

const loadInvites = async () => {
  isLoading.value = true;
  try {
    await groupStore.fetchInvites();
  } catch (error) {
    console.error("초대 목록 불러오기 실패:", error);
    alert(error?.response?.data?.message || "초대 목록을 불러오지 못했습니다.");
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  loadInvites();
});

const formatDate = (value) => {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleDateString("ko-KR");
};

const handleRespond = async (invite, accept) => {
  if (!invite) return;
  isResponding.value = true;
  try {
    await groupStore.respondToInvite(invite.invitationId, accept);
    await loadInvites();
  } catch (error) {
    console.error("초대 처리 실패:", error);
    alert(error?.response?.data?.message || "초대 처리에 실패했습니다.");
  } finally {
    isResponding.value = false;
  }
};
</script>

<template>
  <div class="w-80 bg-white border border-gray-200 shadow-lg rounded-xl p-3 max-h-96 overflow-y-auto">
    <div class="flex items-center justify-between mb-3">
      <div class="flex items-center gap-2">
        <div class="w-8 h-8 rounded-full bg-emerald-50 text-emerald-600 flex items-center justify-center">
          <Users class="w-4 h-4" />
        </div>
        <span class="text-sm font-semibold text-gray-900">받은 초대</span>
      </div>
      <span class="text-xs text-gray-400">최신 순</span>
    </div>

    <div v-if="isLoading" class="py-6 text-center text-sm text-gray-500">
      초대를 불러오는 중입니다...
    </div>

    <div v-else-if="invites.length === 0" class="py-6 text-center text-sm text-gray-500">
      초대 요청이 없습니다
    </div>

    <ul v-else class="space-y-2">
      <li
        v-for="invite in invites"
        :key="invite.invitationId"
        class="rounded-lg border border-gray-100 bg-gray-50 p-3"
      >
        <div class="flex items-start justify-between gap-2">
          <div class="flex-1">
            <p class="text-sm font-semibold text-gray-900">{{ invite.groupName }}</p>
            <p class="text-xs text-gray-600 mt-0.5">리더: {{ invite.leaderName }}</p>
            <p class="text-[11px] text-gray-400 mt-1">{{ formatDate(invite.invitedAt) }}</p>
          </div>
          <div class="flex flex-col gap-1">
            <button
              class="w-8 h-8 rounded-full bg-emerald-500 hover:bg-emerald-600 text-white flex items-center justify-center shadow-sm disabled:opacity-50"
              :disabled="isResponding"
              @click="handleRespond(invite, true)"
              title="수락"
            >
              <Check class="w-4 h-4" />
            </button>
            <button
              class="w-8 h-8 rounded-full bg-white border border-gray-200 hover:border-red-400 hover:text-red-500 text-gray-500 flex items-center justify-center disabled:opacity-50"
              :disabled="isResponding"
              @click="handleRespond(invite, false)"
              title="거절"
            >
              <X class="w-4 h-4" />
            </button>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>
