<script setup>
import { onMounted, ref, watch } from "vue";
import { storeToRefs } from "pinia";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Bell, Check, X, Users } from "lucide-vue-next";
import { useGroupStore } from "@/stores/group/GroupStore";

const props = defineProps({
  open: { type: Boolean, default: false },
  onOpenChange: { type: Function, default: undefined },
});

const emit = defineEmits(["update:open", "accept", "reject"]);
const groupStore = useGroupStore();
const { invites } = storeToRefs(groupStore);
const isLoading = ref(false);
const isResponding = ref(false);

const fetchInviteList = async () => {
  isLoading.value = true;
  try {
    await groupStore.fetchInvites();
  } catch (error) {
    console.error("초대 목록 로드 실패:", error);
    alert(error?.response?.data?.message || "초대 목록을 불러오지 못했습니다.");
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  if (props.open) fetchInviteList();
});

watch(
  () => props.open,
  (val) => {
    props.onOpenChange?.(val);
    if (val) fetchInviteList();
  },
);

const handleAccept = async (invite) => {
  if (!invite) return;
  isResponding.value = true;
  try {
    await groupStore.respondToInvite(invite.invitationId, true);
    emit("accept", invite.invitationId);
  } catch (error) {
    console.error("초대 수락 실패:", error);
    alert(error?.response?.data?.message || "초대 수락에 실패했습니다.");
  } finally {
    isResponding.value = false;
  }
};

const handleReject = async (invite) => {
  if (!invite) return;
  isResponding.value = true;
  try {
    await groupStore.respondToInvite(invite.invitationId, false);
    emit("reject", invite.invitationId);
  } catch (error) {
    console.error("초대 거절 실패:", error);
    alert(error?.response?.data?.message || "초대 거절에 실패했습니다.");
  } finally {
    isResponding.value = false;
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
      }
    "
  >
    <DialogContent class="sm:max-w-md rounded-3xl">
      <DialogHeader>
        <div class="flex items-center gap-2 mb-2">
          <div class="w-10 h-10 bg-emerald-50 text-emerald-600 rounded-full flex items-center justify-center">
            <Bell class="w-5 h-5" />
          </div>
          <DialogTitle>받은 초대 목록</DialogTitle>
        </div>
      </DialogHeader>

      <div class="space-y-4 py-4">
        <div v-if="isLoading" class="text-center py-8 text-muted-foreground text-sm">
          초대를 불러오는 중입니다...
        </div>

        <div v-else-if="invites.length === 0" class="text-center py-8 text-muted-foreground text-sm">
          받은 초대가 없습니다.
        </div>

        <div v-else>
          <div
            v-for="invite in invites"
            :key="invite.invitationId"
            class="bg-white border border-gray-100 rounded-2xl p-4 mb-3 shadow-sm flex items-center justify-between"
          >
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 rounded-full bg-emerald-50 text-emerald-600 flex items-center justify-center">
                <Users class="w-5 h-5" />
              </div>
              <div>
                <h4 class="font-medium text-sm text-gray-900">{{ invite.groupName }}</h4>
                <p class="text-xs text-gray-500">
                  초대자: {{ invite.leaderName }}
                </p>
              </div>
            </div>
            
            <div class="flex items-center gap-2">
              <Button
                size="icon"
                class="bg-white border border-gray-200 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-full w-9 h-9"
                :disabled="isResponding"
                @click="handleReject(invite)"
                title="거절"
              >
                <X class="w-4 h-4" />
              </Button>
              <Button
                size="icon"
                class="bg-emerald-500 hover:bg-emerald-600 text-white rounded-full w-9 h-9 shadow-md shadow-emerald-100"
                :disabled="isResponding"
                @click="handleAccept(invite)"
                title="수락"
              >
                <Check class="w-4 h-4" />
              </Button>
            </div>
          </div>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>
